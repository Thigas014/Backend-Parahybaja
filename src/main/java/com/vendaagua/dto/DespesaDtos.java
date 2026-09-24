package com.vendaagua.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaDtos {

    public record CriarDespesaRequest(
            @NotBlank String descricao,
            @NotNull @Positive BigDecimal valor,
            LocalDate data // opcional: se vier, o gasto fica datado desse dia (ex: dia do fechamento)
    ) {}
}
