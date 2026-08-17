package com.vendaagua.service;

import com.vendaagua.dto.DashboardResponse;
import com.vendaagua.model.Configuracao;
import com.vendaagua.model.DiaDeVenda;
import com.vendaagua.repository.AporteRepository;
import com.vendaagua.repository.DespesaRepository;
import com.vendaagua.repository.PresencaRepository;
import com.vendaagua.repository.VendaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VendaRepository vendaRepository;
    private final AporteRepository aporteRepository;
    private final DespesaRepository despesaRepository;
    private final PresencaRepository presencaRepository;
    private final ConfiguracaoService configuracaoService;
    private final VendaService vendaService;
    private final DiaDeVendaService diaDeVendaService;

    public DashboardResponse gerarDashboard() {
        Configuracao configuracao = configuracaoService.obterConfiguracao();
        BigDecimal meta = configuracao.getMetaFinanceira();

        // A "semana atual" e ancorada na PROXIMA venda marcada no calendario
        // (a que ainda vai acontecer) — assim, tudo que entra ANTES do sabado
        // (aportes, taxas pagas, etc.) ja conta pra meta daquele sabado que
        // esta por vir, sem precisar esperar o dia chegar.
        // So cai pro ultimo sabado que ja passou se nao houver nenhuma data
        // futura marcada (ex: semana ja fechada e a proxima ainda nao foi
        // agendada). Se nao houver nenhuma data marcada nunca, cai no fallback
        // por calculo de calendario civil.
        LocalDate proximaVendaMarcada = diaDeVendaService.proximaDataMarcada()
                .map(DiaDeVenda::getData)
                .orElse(null);

        LocalDate fimSemana = proximaVendaMarcada != null
                ? proximaVendaMarcada
                : diaDeVendaService.ultimaDataMarcada()
                        .map(DiaDeVenda::getData)
                        .orElseGet(vendaService::ultimoSabado);

        LocalDate inicioSemana = fimSemana.minusDays(6);

        LocalDateTime inicioDateTime = inicioSemana.atStartOfDay();
        LocalDateTime fimDateTime = fimSemana.atTime(LocalTime.MAX);

        BigDecimal totalVendas = vendaRepository.totalPorPeriodo(inicioSemana, fimSemana);
        BigDecimal totalAportes = aporteRepository.totalPorPeriodo(inicioDateTime, fimDateTime);
        BigDecimal totalDespesas = despesaRepository.totalPorPeriodo(inicioDateTime, fimDateTime);
        BigDecimal totalTaxasPagas = presencaRepository.totalTaxasPagasPorPeriodo(inicioSemana, fimSemana);

        // Bruto = vendas + aportes + taxas de ausencia pagas (tudo que entrou).
        // Liquido = bruto - despesas (gastos de reposicao da semana).
        BigDecimal totalBruto = totalVendas.add(totalAportes).add(totalTaxasPagas);
        BigDecimal totalLiquido = totalBruto.subtract(totalDespesas);

        double percentualBruto = calcularPercentual(totalBruto, meta);
        double percentualLiquido = calcularPercentual(totalLiquido, meta);

        return new DashboardResponse(
                meta,
                inicioSemana,
                fimSemana,
                totalBruto,
                totalLiquido,
                percentualBruto,
                percentualLiquido,
                totalVendas,
                totalAportes,
                totalDespesas,
                vendaRepository.ultimaData(),
                proximaVendaMarcada
        );
    }

    private double calcularPercentual(BigDecimal valor, BigDecimal meta) {
        if (meta.compareTo(BigDecimal.ZERO) <= 0) return 0;
        double percentual = valor
                .divide(meta, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
        if (percentual > 100) percentual = 100;
        if (percentual < 0) percentual = 0;
        return percentual;
    }
}
