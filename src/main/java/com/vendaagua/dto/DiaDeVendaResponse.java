package com.vendaagua.dto;

import com.vendaagua.model.DiaDeVenda;

import java.time.LocalDate;

public record DiaDeVendaResponse(
        Long id,
        LocalDate data,
        String criadoPorNome
) {
    public static DiaDeVendaResponse from(DiaDeVenda d) {
        return new DiaDeVendaResponse(d.getId(), d.getData(), d.getCriadoPor().getNome());
    }
}
