package com.vendaagua.controller;

import com.vendaagua.dto.PresencaDtos.JustificarAusenciaRequest;
import com.vendaagua.dto.PresencaDtos.MarcarPresencaRequest;
import com.vendaagua.dto.PresencaResponse;
import com.vendaagua.model.Usuario;
import com.vendaagua.service.PresencaService;
import com.vendaagua.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/presencas")
@RequiredArgsConstructor
public class PresencaController {

    private final PresencaService presencaService;
    private final UsuarioService usuarioService;

    /** Lista de presenca de uma data especifica. Todos podem ver. */
    @GetMapping
    public List<PresencaResponse> listar(@RequestParam LocalDate data) {
        return presencaService.listarPorData(data).stream().map(PresencaResponse::from).toList();
    }

    /** Membro justifica a propria ausencia. */
    @PostMapping("/justificativa")
    public PresencaResponse justificarAusencia(@Valid @RequestBody JustificarAusenciaRequest request,
                                                 Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
        return PresencaResponse.from(presencaService.justificarAusencia(usuario, request));
    }

    /** Admin marca presenca/ausencia oficial de um membro numa data. */
    @PostMapping("/marcar")
    @PreAuthorize("hasRole('ADMIN')")
    public PresencaResponse marcarPresenca(@Valid @RequestBody MarcarPresencaRequest request) {
        Usuario membro = usuarioService.buscarPorId(request.usuarioId());
        return PresencaResponse.from(presencaService.marcarPresenca(request, membro));
    }

    /** Admin marca a taxa de ausencia como paga (ou desfaz). */
    @PatchMapping("/{id}/taxa-paga")
    @PreAuthorize("hasRole('ADMIN')")
    public PresencaResponse marcarTaxaPaga(@PathVariable Long id, @RequestParam boolean paga) {
        return PresencaResponse.from(presencaService.marcarTaxaPaga(id, paga));
    }
}
