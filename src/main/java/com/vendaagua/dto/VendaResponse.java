package com.vendaagua.dto;

import com.vendaagua.model.Venda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record VendaResponse(
        Long id,
        LocalDate data,

        BigDecimal notas2,
        BigDecimal notas5,
        BigDecimal notas10,
        BigDecimal notas20,
        BigDecimal notas50,

        BigDecimal moedas1,
        BigDecimal moedas050,
        BigDecimal moedas025,
        BigDecimal moedas010,
        BigDecimal moedas005,

        BigDecimal valorPix,

        BigDecimal valorNotas,
        BigDecimal valorMoedas,
        BigDecimal valorTotal,

        String registradoPorNome,
        LocalDateTime dataHora
) {
    public static VendaResponse from(Venda v) {
        return new VendaResponse(
                v.getId(),
                v.getData(),
                v.getNotas2(), v.getNotas5(), v.getNotas10(), v.getNotas20(), v.getNotas50(),
                v.getMoedas1(), v.getMoedas050(), v.getMoedas025(), v.getMoedas010(), v.getMoedas005(),
                v.getValorPix(),
                v.getValorNotas(),
                v.getValorMoedas(),
                v.getValorTotal(),
                v.getRegistradoPor().getNome(),
                v.getDataHora()
        );
    }
}
