package com.vendaagua.dto;

import com.vendaagua.model.Presenca;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PresencaResponse(
        Long id,
        LocalDate data,
        Long usuarioId,
        String usuarioNome,
        Boolean presente,
        String justificativa,
        BigDecimal taxaValor,
        Boolean taxaPaga
) {
    public static PresencaResponse from(Presenca p) {
        return new PresencaResponse(
                p.getId(), p.getData(), p.getUsuario().getId(), p.getUsuario().getNome(),
                p.getPresente(), p.getJustificativa(), p.getTaxaValor(), p.getTaxaPaga()
        );
    }
}
