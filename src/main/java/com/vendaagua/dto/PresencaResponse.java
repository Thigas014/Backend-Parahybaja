package com.vendaagua.dto;

import com.vendaagua.model.Presenca;
import com.vendaagua.model.StatusPresenca;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PresencaResponse(
        Long id,
        LocalDate data,
        Long usuarioId,
        String usuarioNome,
        StatusPresenca status,
        String justificativa,
        BigDecimal taxaValor,
        Boolean taxaPaga
) {
    public static PresencaResponse from(Presenca p) {
        return new PresencaResponse(
                p.getId(), p.getData(), p.getUsuario().getId(), p.getUsuario().getNome(),
                p.getStatus(), p.getJustificativa(), p.getTaxaValor(), p.getTaxaPaga()
        );
    }
}
