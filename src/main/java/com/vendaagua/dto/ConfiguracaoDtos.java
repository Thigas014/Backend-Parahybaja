package com.vendaagua.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public class ConfiguracaoDtos {
    public record MetaRequest(
            @NotNull @PositiveOrZero BigDecimal metaFinanceira
    ) {}

    public record TaxasRequest(
            @NotNull @PositiveOrZero BigDecimal valorTaxaJustificado,
            @NotNull @PositiveOrZero BigDecimal valorTaxaSemJustificativa
    ) {}
}
