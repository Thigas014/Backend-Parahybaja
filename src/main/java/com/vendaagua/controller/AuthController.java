package com.vendaagua.controller;

import com.vendaagua.dto.AuthDtos.LoginRequest;
import com.vendaagua.dto.AuthDtos.LoginResponse;
import com.vendaagua.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
