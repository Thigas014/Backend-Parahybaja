package com.vendaagua.service;

import com.vendaagua.config.JwtService;
import com.vendaagua.dto.AuthDtos.LoginRequest;
import com.vendaagua.dto.AuthDtos.LoginResponse;
import com.vendaagua.model.Usuario;
import com.vendaagua.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        Map<String, Object> claims = new HashMap<>();
        claims.put("perfil", usuario.getPerfil().name());
        claims.put("nome", usuario.getNome());
        claims.put("id", usuario.getId());

        String token = jwtService.generateToken(userDetails, claims);

        return new LoginResponse(token, usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil());
    }
}
