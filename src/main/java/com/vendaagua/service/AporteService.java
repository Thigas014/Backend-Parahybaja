package com.vendaagua.service;

import com.vendaagua.dto.AporteDtos.CriarAporteRequest;
import com.vendaagua.exception.RecursoNaoEncontradoException;
import com.vendaagua.model.Aporte;
import com.vendaagua.model.Usuario;
import com.vendaagua.repository.AporteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AporteService {

    private final AporteRepository aporteRepository;

    public List<Aporte> listarTodos() {
        return aporteRepository.findAllByOrderByDataHoraDesc();
    }

    public Aporte criar(Usuario admin, CriarAporteRequest request) {
        Aporte aporte = Aporte.builder()
                .descricao(request.descricao())
                .valor(request.valor())
                .registradoPor(admin)
                .build();
        return aporteRepository.save(aporte);
    }

    public void excluir(Long id) {
        Aporte aporte = aporteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aporte nao encontrado"));
        aporteRepository.delete(aporte);
    }

    public BigDecimal totalAportes() {
        return aporteRepository.totalAportes();
    }
}
