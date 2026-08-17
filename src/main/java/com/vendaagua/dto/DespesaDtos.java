package com.vendaagua.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class DespesaDtos {

    public record CriarDespesaRequest(
            @NotBlank String descricao,
            @NotNull @Positive BigDecimal valor
    ) {}
}
