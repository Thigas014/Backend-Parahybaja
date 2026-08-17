package com.vendaagua.controller;

import com.vendaagua.dto.ConfiguracaoDtos.MetaRequest;
import com.vendaagua.dto.ConfiguracaoDtos.TaxaAusenciaRequest;
import com.vendaagua.model.Configuracao;
import com.vendaagua.service.ConfiguracaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/configuracao")
@RequiredArgsConstructor
public class ConfiguracaoController {

    private final ConfiguracaoService configuracaoService;

    @GetMapping
    public Configuracao obter() {
        return configuracaoService.obterConfiguracao();
    }

    @PutMapping("/meta")
    @PreAuthorize("hasRole('ADMIN')")
    public Configuracao atualizarMeta(@Valid @RequestBody MetaRequest request) {
        return configuracaoService.atualizarMeta(request);
    }

    @PutMapping("/taxa-ausencia")
    @PreAuthorize("hasRole('ADMIN')")
    public Configuracao atualizarTaxaAusencia(@Valid @RequestBody TaxaAusenciaRequest request) {
        return configuracaoService.atualizarTaxaAusencia(request);
    }
}
