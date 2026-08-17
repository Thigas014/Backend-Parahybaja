package com.vendaagua.controller;

import com.vendaagua.dto.AporteDtos.CriarAporteRequest;
import com.vendaagua.dto.AporteResponse;
import com.vendaagua.model.Usuario;
import com.vendaagua.service.AporteService;
import com.vendaagua.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aportes")
@RequiredArgsConstructor
public class AporteController {

    private final AporteService aporteService;
    private final UsuarioService usuarioService;

    /** Qualquer usuario autenticado pode ver os aportes (transparencia da arrecadacao). */
    @GetMapping
    public List<AporteResponse> listar() {
        return aporteService.listarTodos().stream().map(AporteResponse::from).toList();
    }

    /** Apenas administrador pode registrar um aporte (doacao, patrocinio, etc). */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public AporteResponse criar(@Valid @RequestBody CriarAporteRequest request, Authentication authentication) {
        Usuario admin = usuarioService.buscarPorEmail(authentication.getName());
        return AporteResponse.from(aporteService.criar(admin, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id) {
        aporteService.excluir(id);
    }
}
