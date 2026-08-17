package com.vendaagua.controller;

import com.vendaagua.dto.DiaDeVendaDtos.CriarDiaDeVendaRequest;
import com.vendaagua.dto.DiaDeVendaResponse;
import com.vendaagua.model.Usuario;
import com.vendaagua.service.DiaDeVendaService;
import com.vendaagua.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dias-de-venda")
@RequiredArgsConstructor
public class DiaDeVendaController {

    private final DiaDeVendaService diaDeVendaService;
    private final UsuarioService usuarioService;

    /** Todos veem o calendario. */
    @GetMapping
    public List<DiaDeVendaResponse> listar(
            @RequestParam(required = false) LocalDate inicio,
            @RequestParam(required = false) LocalDate fim) {

        var dias = (inicio != null && fim != null)
                ? diaDeVendaService.listarPorPeriodo(inicio, fim)
                : diaDeVendaService.listarTodos();

        return dias.stream().map(DiaDeVendaResponse::from).toList();
    }

    /** Apenas admin marca datas de venda no calendario. */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DiaDeVendaResponse criar(@Valid @RequestBody CriarDiaDeVendaRequest request, Authentication authentication) {
        Usuario admin = usuarioService.buscarPorEmail(authentication.getName());
        return DiaDeVendaResponse.from(diaDeVendaService.criar(admin, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id) {
        diaDeVendaService.excluir(id);
    }
}
