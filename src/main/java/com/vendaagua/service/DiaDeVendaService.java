package com.vendaagua.service;

import com.vendaagua.dto.DiaDeVendaDtos.CriarDiaDeVendaRequest;
import com.vendaagua.exception.RecursoNaoEncontradoException;
import com.vendaagua.exception.RegraNegocioException;
import com.vendaagua.model.DiaDeVenda;
import com.vendaagua.model.Usuario;
import com.vendaagua.repository.DiaDeVendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaDeVendaService {

    private final DiaDeVendaRepository diaDeVendaRepository;

    public List<DiaDeVenda> listarTodos() {
        return diaDeVendaRepository.findAllByOrderByDataAsc();
    }

    public List<DiaDeVenda> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return diaDeVendaRepository.findByDataBetweenOrderByDataAsc(inicio, fim);
    }

    public DiaDeVenda criar(Usuario admin, CriarDiaDeVendaRequest request) {
        if (diaDeVendaRepository.findByData(request.data()).isPresent()) {
            throw new RegraNegocioException("Ja existe um dia de venda marcado nessa data.");
        }
        DiaDeVenda dia = DiaDeVenda.builder()
                .data(request.data())
                .criadoPor(admin)
                .build();
        return diaDeVendaRepository.save(dia);
    }

    public void excluir(Long id) {
        DiaDeVenda dia = diaDeVendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Dia de venda nao encontrado"));
        diaDeVendaRepository.delete(dia);
    }

    /** Ultima data de venda marcada no calendario, ate hoje (inclusive). */
    public Optional<DiaDeVenda> ultimaDataMarcada() {
        return diaDeVendaRepository.findFirstByDataLessThanEqualOrderByDataDesc(LocalDate.now());
    }

    /** Proxima data de venda marcada no calendario, a partir de hoje (inclusive). */
    public Optional<DiaDeVenda> proximaDataMarcada() {
        return diaDeVendaRepository.findFirstByDataGreaterThanEqualOrderByDataAsc(LocalDate.now());
    }
}
