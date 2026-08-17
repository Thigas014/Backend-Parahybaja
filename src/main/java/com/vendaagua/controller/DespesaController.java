package com.vendaagua.controller;

import com.vendaagua.dto.DespesaDtos.CriarDespesaRequest;
import com.vendaagua.dto.DespesaResponse;
import com.vendaagua.model.Usuario;
import com.vendaagua.service.DespesaService;
import com.vendaagua.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;
    private final UsuarioService usuarioService;

    /** Qualquer usuario autenticado pode ver os gastos (transparencia). */
    @GetMapping
    public List<DespesaResponse> listar() {
        return despesaService.listarTodas().stream().map(DespesaResponse::from).toList();
    }

    /** Apenas administrador pode registrar um gasto (compra de agua, gelo, etc). */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DespesaResponse criar(@Valid @RequestBody CriarDespesaRequest request, Authentication authentication) {
        Usuario admin = usuarioService.buscarPorEmail(authentication.getName());
        return DespesaResponse.from(despesaService.criar(admin, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id) {
        despesaService.excluir(id);
    }
}
