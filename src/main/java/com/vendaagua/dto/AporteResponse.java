package com.vendaagua.dto;

import com.vendaagua.model.Aporte;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AporteResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        String registradoPorNome,
        LocalDateTime dataHora
) {
    public static AporteResponse from(Aporte a) {
        return new AporteResponse(a.getId(), a.getDescricao(), a.getValor(),
                a.getRegistradoPor().getNome(), a.getDataHora());
    }
}
