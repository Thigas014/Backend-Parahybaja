package com.vendaagua.service;

import com.vendaagua.dto.PresencaDtos.JustificarAusenciaRequest;
import com.vendaagua.dto.PresencaDtos.MarcarPresencaRequest;
import com.vendaagua.exception.RecursoNaoEncontradoException;
import com.vendaagua.exception.RegraNegocioException;
import com.vendaagua.model.Configuracao;
import com.vendaagua.model.Presenca;
import com.vendaagua.model.Usuario;
import com.vendaagua.repository.PresencaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PresencaService {

    private final PresencaRepository presencaRepository;
    private final ConfiguracaoService configuracaoService;

    public List<Presenca> listarPorData(LocalDate data) {
        return presencaRepository.findByDataOrderByUsuarioNomeAsc(data);
    }

    /**
     * O proprio membro justifica sua ausencia em uma data. Cria ou atualiza o
     * registro dele, deixando SOMENTE a justificativa preenchida — o status
     * fica em branco (null), esperando o admin avaliar e decidir entre
     * JUSTIFICADO ou AUSENTE. So pode justificar ANTES da data da venda
     * acontecer — depois que o dia passou, so o admin pode corrigir.
     */
    public Presenca justificarAusencia(Usuario usuario, JustificarAusenciaRequest request) {
        if (request.data().isBefore(LocalDate.now())) {
            throw new RegraNegocioException(
                    "Não é possível justificar ausência de uma data que já passou. Fale com o administrador.");
        }

        Presenca presenca = presencaRepository.findByDataAndUsuario(request.data(), usuario)
                .orElseGet(() -> Presenca.builder()
                        .data(request.data())
                        .usuario(usuario)
                        .build());

        presenca.setJustificativa(request.justificativa());
        // Nao mexe no status: fica pendente (null) ate o admin avaliar,
        // mesmo que ja exista um status anterior (o admin pode reavaliar).
        return presencaRepository.save(presenca);
    }

    /**
     * Admin define o status oficial de um membro numa data: PRESENTE,
     * JUSTIFICADO ou AUSENTE. A taxa aplicada depende do status:
     *  - JUSTIFICADO -> valorTaxaJustificado
     *  - AUSENTE -> valorTaxaSemJustificativa
     *  - PRESENTE -> sem taxa (remove taxa pendente, mantem se ja paga)
     * O valor e travado no momento da decisao (nao muda se o admin alterar
     * a configuracao das taxas depois).
     */
    public Presenca marcarPresenca(MarcarPresencaRequest request, Usuario membro) {
        Presenca presenca = presencaRepository.findByDataAndUsuario(request.data(), membro)
                .orElseGet(() -> Presenca.builder()
                        .data(request.data())
                        .usuario(membro)
                        .build());

        presenca.setStatus(request.status());

        Configuracao configuracao = configuracaoService.obterConfiguracao();

        switch (request.status()) {
            case JUSTIFICADO -> {
                if (presenca.getTaxaValor() == null || !Boolean.TRUE.equals(presenca.getTaxaPaga())) {
                    presenca.setTaxaValor(configuracao.getValorTaxaJustificado());
                }
            }
            case AUSENTE -> {
                if (presenca.getTaxaValor() == null || !Boolean.TRUE.equals(presenca.getTaxaPaga())) {
                    presenca.setTaxaValor(configuracao.getValorTaxaSemJustificativa());
                }
            }
            case PRESENTE -> {
                // Vira presente: remove taxa pendente (mas mantem se ja tiver sido paga, pra nao sumir do historico).
                if (!Boolean.TRUE.equals(presenca.getTaxaPaga())) {
                    presenca.setTaxaValor(null);
                }
            }
        }

        return presencaRepository.save(presenca);
    }

    public Presenca marcarTaxaPaga(Long id, boolean paga) {
        Presenca presenca = presencaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Registro de presenca nao encontrado"));
        presenca.setTaxaPaga(paga);
        return presencaRepository.save(presenca);
    }

    public BigDecimal totalTaxasPagasPorPeriodo(LocalDate inicio, LocalDate fim) {
        return presencaRepository.totalTaxasPagasPorPeriodo(inicio, fim);
    }
}
