package com.vendaagua.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PresencaDtos {

    /** Membro justifica a propria ausencia em um dia de venda. */
    public record JustificarAusenciaRequest(
            @NotNull LocalDate data,
            @NotBlank String justificativa
    ) {}

    /** Admin marca presenca/ausencia oficial de um membro. */
    public record MarcarPresencaRequest(
            @NotNull Long usuarioId,
            @NotNull LocalDate data,
            @NotNull Boolean presente
    ) {}
}
