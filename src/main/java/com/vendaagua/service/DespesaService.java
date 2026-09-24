package com.vendaagua.service;

import com.vendaagua.dto.DespesaDtos.CriarDespesaRequest;
import com.vendaagua.exception.RecursoNaoEncontradoException;
import com.vendaagua.model.Despesa;
import com.vendaagua.model.Usuario;
import com.vendaagua.repository.DespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaRepository despesaRepository;

    public List<Despesa> listarTodas() {
        return despesaRepository.findAllByOrderByDataHoraDesc();
    }

    public Despesa criar(Usuario admin, CriarDespesaRequest request) {
        Despesa.DespesaBuilder builder = Despesa.builder()
                .descricao(request.descricao())
                .valor(request.valor())
                .registradoPor(admin);

        if (request.data() != null) {
            // Fixa no meio-dia da data informada, so pra manter a data
            // correta independente de fuso — nao usamos hora especifica.
            builder.dataHora(request.data().atTime(12, 0));
        }

        return despesaRepository.save(builder.build());
    }

    public void excluir(Long id) {
        Despesa despesa = despesaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Despesa nao encontrada"));
        despesaRepository.delete(despesa);
    }

    public BigDecimal totalDespesas() {
        return despesaRepository.totalDespesas();
    }
}
