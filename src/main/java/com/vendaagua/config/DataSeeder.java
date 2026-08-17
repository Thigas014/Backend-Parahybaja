package com.vendaagua.config;

import com.vendaagua.model.Configuracao;
import com.vendaagua.model.Perfil;
import com.vendaagua.model.Usuario;
import com.vendaagua.repository.ConfiguracaoRepository;
import com.vendaagua.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Cria um administrador padrao e uma configuracao inicial de meta na primeira
 * inicializacao, caso o banco esteja vazio. Facilita o primeiro acesso.
 *
 * Login padrao: admin@vendaagua.com / admin123
 * IMPORTANTE: altere a senha assim que possivel em producao.
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ConfiguracaoRepository configuracaoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            Usuario admin = Usuario.builder()
                    .nome("Administrador")
                    .email("admin@vendaagua.com")
                    .senha(passwordEncoder.encode("admin123"))
                    .perfil(Perfil.ADMIN)
                    .ativo(true)
                    .build();
            usuarioRepository.save(admin);
        }

        if (configuracaoRepository.count() == 0) {
            configuracaoRepository.save(Configuracao.builder()
                    .metaFinanceira(BigDecimal.valueOf(1000))
                    .build());
        }
    }
}
