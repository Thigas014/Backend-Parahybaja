package com.vendaagua.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DashboardResponse(
        BigDecimal metaSemanal,
        LocalDate inicioSemana,
        LocalDate fimSemana,

        BigDecimal totalBruto,       // vendas + aportes (inclui taxas pagas) da semana
        BigDecimal totalLiquido,     // totalBruto - despesas da semana
        double percentualBruto,
        double percentualLiquido,

        BigDecimal totalVendasSemana,
        BigDecimal totalAportesSemana,
        BigDecimal totalDespesasSemana,

        LocalDate dataUltimaVenda,
        LocalDate proximaVendaMarcada // proxima data marcada no calendario (null se nao houver nenhuma futura)
) {}
