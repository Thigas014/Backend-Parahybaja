package com.vendaagua.controller;

import com.vendaagua.dto.AuthDtos.AtualizarUsuarioRequest;
import com.vendaagua.dto.AuthDtos.CriarUsuarioRequest;
import com.vendaagua.dto.UsuarioResponse;
import com.vendaagua.model.Usuario;
import com.vendaagua.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsuarioResponse> listar() {
        return usuarioService.listarTodos().stream().map(UsuarioResponse::from).toList();
    }

    @GetMapping("/me")
    public UsuarioResponse me(Authentication authentication) {
        Usuario usuario = usuarioService.buscarPorEmail(authentication.getName());
        return UsuarioResponse.from(usuario);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse criar(@Valid @RequestBody CriarUsuarioRequest request) {
        return UsuarioResponse.from(usuarioService.criar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody AtualizarUsuarioRequest request) {
        return UsuarioResponse.from(usuarioService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void excluir(@PathVariable Long id) {
        usuarioService.excluir(id);
    }
}
