package com.vendaagua.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VendaDtos {

    /**
     * Fechamento de caixa de um dia de venda, detalhado por denominacao
     * (subtotal em reais de cada tipo de nota/moeda, igual a planilha da
     * equipe) mais o valor recebido via pix. Somente admin registra.
     */
    public record RegistrarFechamentoRequest(
            @NotNull LocalDate data,

            @PositiveOrZero BigDecimal notas2,
            @PositiveOrZero BigDecimal notas5,
            @PositiveOrZero BigDecimal notas10,
            @PositiveOrZero BigDecimal notas20,
            @PositiveOrZero BigDecimal notas50,

            @PositiveOrZero BigDecimal moedas1,
            @PositiveOrZero BigDecimal moedas050,
            @PositiveOrZero BigDecimal moedas025,
            @PositiveOrZero BigDecimal moedas010,
            @PositiveOrZero BigDecimal moedas005,

            @NotNull @PositiveOrZero BigDecimal valorPix
    ) {}
}
