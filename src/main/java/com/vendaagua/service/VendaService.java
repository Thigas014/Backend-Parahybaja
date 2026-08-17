package com.vendaagua.service;

import com.vendaagua.dto.VendaDtos.RegistrarFechamentoRequest;
import com.vendaagua.exception.RecursoNaoEncontradoException;
import com.vendaagua.model.Usuario;
import com.vendaagua.model.Venda;
import com.vendaagua.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;

    /** Apenas administrador registra o fechamento de caixa do dia (por denominacao + pix). */
    public Venda registrarFechamento(Usuario admin, RegistrarFechamentoRequest request) {
        Venda venda = Venda.builder()
                .data(request.data())
                .notas2(request.notas2())
                .notas5(request.notas5())
                .notas10(request.notas10())
                .notas20(request.notas20())
                .notas50(request.notas50())
                .moedas1(request.moedas1())
                .moedas050(request.moedas050())
                .moedas025(request.moedas025())
                .moedas010(request.moedas010())
                .moedas005(request.moedas005())
                .valorPix(request.valorPix())
                .registradoPor(admin)
                .build();
        return vendaRepository.save(venda);
    }

    public Venda atualizarFechamento(Long id, RegistrarFechamentoRequest request) {
        Venda venda = buscarPorId(id);
        venda.setData(request.data());
        venda.setNotas2(request.notas2());
        venda.setNotas5(request.notas5());
        venda.setNotas10(request.notas10());
        venda.setNotas20(request.notas20());
        venda.setNotas50(request.notas50());
        venda.setMoedas1(request.moedas1());
        venda.setMoedas050(request.moedas050());
        venda.setMoedas025(request.moedas025());
        venda.setMoedas010(request.moedas010());
        venda.setMoedas005(request.moedas005());
        venda.setValorPix(request.valorPix());
        return vendaRepository.save(venda);
    }

    public void excluir(Long id) {
        Venda venda = buscarPorId(id);
        vendaRepository.delete(venda);
    }

    public Venda buscarPorId(Long id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fechamento nao encontrado"));
    }

    public List<Venda> listarTodas() {
        return vendaRepository.findAllByOrderByDataDesc();
    }

    public List<Venda> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.findByDataBetweenOrderByDataDesc(inicio, fim);
    }

    public BigDecimal totalPorPeriodo(LocalDate inicio, LocalDate fim) {
        return vendaRepository.totalPorPeriodo(inicio, fim);
    }

    public LocalDate ultimaData() {
        return vendaRepository.ultimaData();
    }

    /** Sabado mais recente (hoje, se hoje for sabado). Usado como fim da "semana" atual. */
    public LocalDate ultimoSabado() {
        LocalDate hoje = LocalDate.now();
        int diff = hoje.getDayOfWeek().getValue() - DayOfWeek.SATURDAY.getValue();
        if (diff < 0) diff += 7;
        return hoje.minusDays(diff);
    }

    /** Proximo sabado (hoje, se hoje for sabado). Usado pro calendario sugerir a proxima data. */
    public LocalDate proximoSabado() {
        LocalDate hoje = LocalDate.now();
        int diff = DayOfWeek.SATURDAY.getValue() - hoje.getDayOfWeek().getValue();
        if (diff < 0) diff += 7;
        return hoje.plusDays(diff);
    }
}
