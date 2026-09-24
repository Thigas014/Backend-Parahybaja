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
    private final DiaDeVendaService diaDeVendaService;

    public DashboardResponse gerarDashboard() {
        Configuracao configuracao = configuracaoService.obterConfiguracao();
        BigDecimal meta = configuracao.getMetaFinanceira();

        // A "semana atual" agora e sempre uma semana de calendario FIXA
        // (domingo a sabado), calculada a partir de hoje — nao depende de
        // nenhuma data marcada no calendario. Isso garante que a meta so
        // muda de semana quando o domingo realmente chega, e nao no momento
        // em que o admin marca uma data nova (bug corrigido).
        LocalDate hoje = LocalDate.now();
        int diasDesdeDomingo = hoje.getDayOfWeek().getValue() % 7; // domingo=0, segunda=1 ... sabado=6
        LocalDate inicioSemana = hoje.minusDays(diasDesdeDomingo);
        LocalDate fimSemana = inicioSemana.plusDays(6);

        // A "proxima venda marcada" continua vindo do calendario — usada so
        // pra mostrar avisos ("venda de hoje", "proxima venda dia X"), sem
        // influenciar o calculo da meta em si.
        LocalDate proximaVendaMarcada = diaDeVendaService.proximaDataMarcada()
                .map(DiaDeVenda::getData)
                .orElse(null);

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
