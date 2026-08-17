package com.vendaagua.service;

import com.vendaagua.dto.PresencaDtos.JustificarAusenciaRequest;
import com.vendaagua.dto.PresencaDtos.MarcarPresencaRequest;
import com.vendaagua.exception.RecursoNaoEncontradoException;
import com.vendaagua.exception.RegraNegocioException;
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
     * registro dele. So pode justificar ANTES da data da venda acontecer —
     * depois que o dia passou, so o admin pode corrigir a lista de presenca.
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
        // Se o admin ainda nao marcou nada, a justificativa ja indica ausencia.
        if (presenca.getPresente() == null) {
            presenca.setPresente(false);
        }
        return presencaRepository.save(presenca);
    }

    /** Admin marca presenca/ausencia oficial. Se ausente, aplica a taxa configurada (travada nesse momento). */
    public Presenca marcarPresenca(MarcarPresencaRequest request, Usuario membro) {
        Presenca presenca = presencaRepository.findByDataAndUsuario(request.data(), membro)
                .orElseGet(() -> Presenca.builder()
                        .data(request.data())
                        .usuario(membro)
                        .build());

        presenca.setPresente(request.presente());

        if (!request.presente()) {
            if (presenca.getTaxaValor() == null) {
                presenca.setTaxaValor(configuracaoService.obterConfiguracao().getValorTaxaAusencia());
            }
        } else {
            // Virou presente: remove taxa pendente (mas mantem se ja tiver sido paga, para nao sumir do historico).
            if (!Boolean.TRUE.equals(presenca.getTaxaPaga())) {
                presenca.setTaxaValor(null);
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
