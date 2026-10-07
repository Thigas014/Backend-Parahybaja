package com.vendaagua.service;

import com.vendaagua.dto.AuthDtos.AtualizarUsuarioRequest;
import com.vendaagua.dto.AuthDtos.CriarUsuarioRequest;
import com.vendaagua.exception.RecursoNaoEncontradoException;
import com.vendaagua.exception.RegraNegocioException;
import com.vendaagua.model.Perfil;
import com.vendaagua.model.Usuario;
import com.vendaagua.repository.AporteRepository;
import com.vendaagua.repository.DespesaRepository;
import com.vendaagua.repository.DiaDeVendaRepository;
import com.vendaagua.repository.PresencaRepository;
import com.vendaagua.repository.UsuarioRepository;
import com.vendaagua.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PresencaRepository presencaRepository;
    private final VendaRepository vendaRepository;
    private final AporteRepository aporteRepository;
    private final DespesaRepository despesaRepository;
    private final DiaDeVendaRepository diaDeVendaRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado"));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado"));
    }

    public Usuario criar(CriarUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new RegraNegocioException("Ja existe um usuario com este e-mail.");
        }
        Perfil perfil = parsePerfil(request.perfil());

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .perfil(perfil)
                .ativo(true)
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, AtualizarUsuarioRequest request) {
        Usuario usuario = buscarPorId(id);

        if (request.nome() != null && !request.nome().isBlank()) {
            usuario.setNome(request.nome());
        }
        if (request.email() != null && !request.email().isBlank()) {
            usuario.setEmail(request.email());
        }
        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }
        if (request.perfil() != null && !request.perfil().isBlank()) {
            usuario.setPerfil(parsePerfil(request.perfil()));
        }
        if (request.ativo() != null) {
            usuario.setAtivo(request.ativo());
        }

        return usuarioRepository.save(usuario);
    }

    /**
     * Exclui o usuario de verdade SOMENTE se ele nao tiver nenhum historico
     * vinculado (presenca, fechamento, aporte, despesa ou data de calendario
     * registrados por ele). Se tiver, em vez de apagar (o que quebraria essas
     * referencias ou exigiria apagar o historico junto), apenas desativa a
     * conta — assim o nome continua aparecendo certinho no Historico e na
     * lista de presenca, mas a pessoa nao consegue mais fazer login.
     */
    public boolean excluir(Long id) {
        Usuario usuario = buscarPorId(id);

        boolean temHistorico = presencaRepository.existsByUsuario(usuario)
                || vendaRepository.existsByRegistradoPor(usuario)
                || aporteRepository.existsByRegistradoPor(usuario)
                || despesaRepository.existsByRegistradoPor(usuario)
                || diaDeVendaRepository.existsByCriadoPor(usuario);

        if (temHistorico) {
            usuario.setAtivo(false);
            usuarioRepository.save(usuario);
            return false; // nao foi excluido de verdade, so desativado
        }

        usuarioRepository.delete(usuario);
        return true; // excluido de verdade
    }

    private Perfil parsePerfil(String valor) {
        try {
            return Perfil.valueOf(valor.trim().toUpperCase());
        } catch (Exception e) {
            throw new RegraNegocioException("Perfil invalido. Use ADMIN ou MEMBRO.");
        }
    }
}
