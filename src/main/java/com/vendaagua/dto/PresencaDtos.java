package com.vendaagua.dto;

import com.vendaagua.model.StatusPresenca;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class PresencaDtos {

    /** Membro justifica a propria ausencia em um dia de venda. */
    public record JustificarAusenciaRequest(
            @NotNull LocalDate data,
            @NotBlank String justificativa
    ) {}

    /** Admin define o status oficial (PRESENTE, JUSTIFICADO ou AUSENTE) de um membro. */
    public record MarcarPresencaRequest(
            @NotNull Long usuarioId,
            @NotNull LocalDate data,
            @NotNull StatusPresenca status
    ) {}
}
