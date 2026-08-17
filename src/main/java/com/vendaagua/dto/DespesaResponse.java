package com.vendaagua.dto;

import com.vendaagua.model.Despesa;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DespesaResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        String registradoPorNome,
        LocalDateTime dataHora
) {
    public static DespesaResponse from(Despesa d) {
        return new DespesaResponse(d.getId(), d.getDescricao(), d.getValor(),
                d.getRegistradoPor().getNome(), d.getDataHora());
    }
}
