package com.vendaagua.service;

import com.vendaagua.dto.ConfiguracaoDtos.MetaRequest;
import com.vendaagua.dto.ConfiguracaoDtos.TaxaAusenciaRequest;
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
                                .valorTaxaAusencia(BigDecimal.ZERO)
                                .build()));

        // Registro criado antes da coluna valor_taxa_ausencia existir: conserta e salva.
        if (configuracao.getValorTaxaAusencia() == null) {
            configuracao.setValorTaxaAusencia(BigDecimal.ZERO);
            configuracao = configuracaoRepository.save(configuracao);
        }

        return configuracao;
    }

    public Configuracao atualizarMeta(MetaRequest request) {
        Configuracao configuracao = obterConfiguracao();
        configuracao.setMetaFinanceira(request.metaFinanceira());
        return configuracaoRepository.save(configuracao);
    }

    public Configuracao atualizarTaxaAusencia(TaxaAusenciaRequest request) {
        Configuracao configuracao = obterConfiguracao();
        configuracao.setValorTaxaAusencia(request.valorTaxaAusencia());
        return configuracaoRepository.save(configuracao);
    }
}
