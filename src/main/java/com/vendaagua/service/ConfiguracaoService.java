package com.vendaagua.service;

import com.vendaagua.dto.ConfiguracaoDtos.MetaRequest;
import com.vendaagua.dto.ConfiguracaoDtos.TaxasRequest;
import com.vendaagua.model.Configuracao;
import com.vendaagua.repository.ConfiguracaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ConfiguracaoService {

    private final ConfiguracaoRepository configuracaoRepository;

    public Configuracao obterConfiguracao() {
        Configuracao configuracao = configuracaoRepository.findAll().stream().findFirst()
                .orElseGet(() -> configuracaoRepository.save(
                        Configuracao.builder()
                                .metaFinanceira(BigDecimal.ZERO)
                                .valorTaxaJustificado(BigDecimal.ZERO)
                                .valorTaxaSemJustificativa(BigDecimal.ZERO)
                                .build()));

        // Registro criado antes dessas colunas existirem: conserta e salva.
        boolean precisaSalvar = false;
        if (configuracao.getValorTaxaJustificado() == null) {
            configuracao.setValorTaxaJustificado(BigDecimal.ZERO);
            precisaSalvar = true;
        }
        if (configuracao.getValorTaxaSemJustificativa() == null) {
            configuracao.setValorTaxaSemJustificativa(BigDecimal.ZERO);
            precisaSalvar = true;
        }
        if (precisaSalvar) {
            configuracao = configuracaoRepository.save(configuracao);
        }

        return configuracao;
    }

    public Configuracao atualizarMeta(MetaRequest request) {
        Configuracao configuracao = obterConfiguracao();
        configuracao.setMetaFinanceira(request.metaFinanceira());
        return configuracaoRepository.save(configuracao);
    }

    public Configuracao atualizarTaxas(TaxasRequest request) {
        Configuracao configuracao = obterConfiguracao();
        configuracao.setValorTaxaJustificado(request.valorTaxaJustificado());
        configuracao.setValorTaxaSemJustificativa(request.valorTaxaSemJustificativa());
        return configuracaoRepository.save(configuracao);
    }
}
