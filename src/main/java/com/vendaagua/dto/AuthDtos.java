package com.vendaagua.dto;

import com.vendaagua.model.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    public record LoginRequest(
            @NotBlank @Email String email,
            @NotBlank String senha
    ) {}

    public record LoginResponse(
            String token,
            Long id,
            String nome,
            String email,
            Perfil perfil
    ) {}

    public record CriarUsuarioRequest(
            @NotBlank String nome,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres") String senha,
            @NotBlank String perfil
    ) {}

    public record AtualizarUsuarioRequest(
            String nome,
            String email,
            String senha,
            String perfil,
            Boolean ativo
    ) {}
}
