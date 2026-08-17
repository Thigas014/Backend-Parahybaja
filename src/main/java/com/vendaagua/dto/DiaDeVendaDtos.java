package com.vendaagua.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class DiaDeVendaDtos {
    public record CriarDiaDeVendaRequest(
            @NotNull LocalDate data
    ) {}
}
