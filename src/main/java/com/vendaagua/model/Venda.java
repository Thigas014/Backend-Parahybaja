package com.vendaagua.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa o fechamento de caixa de um dia de venda (sabado): quanto foi
 * arrecadado, detalhado por denominacao de nota e moeda, mais pix.
 * Registrado apenas por administradores, ja que a equipe conta o dinheiro
 * toda junta no final do dia.
 *
 * Os campos por denominacao guardam o SUBTOTAL em reais daquela denominacao
 * (ex: notas2 = 328,00 significa R$328 somados em notas de R$2), igual a
 * planilha que a equipe ja usa - nao a quantidade de cedulas/moedas.
 *
 * Sem "nullable = false" nos campos novos de proposito: evita quebrar o
 * ALTER TABLE em bancos que ja tem fechamentos registrados com o modelo
 * antigo (valor_moedas/valor_notas unicos).
 */
@Entity
@Table(name = "vendas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Data do dia de venda (normalmente um sabado). Sem "nullable = false"
     * de proposito, mesmo motivo do restante do arquivo: evita quebrar o
     * ALTER TABLE se um dia essa tabela ja tiver dados de um schema anterior.
     * A obrigatoriedade real e garantida na validacao do DTO (@NotNull).
     */
    @Column
    private LocalDate data;

    // ---- Notas, subtotal em reais por denominacao ----
    @Column(name = "notas_2")
    @Builder.Default
    private BigDecimal notas2 = BigDecimal.ZERO;

    @Column(name = "notas_5")
    @Builder.Default
    private BigDecimal notas5 = BigDecimal.ZERO;

    @Column(name = "notas_10")
    @Builder.Default
    private BigDecimal notas10 = BigDecimal.ZERO;

    @Column(name = "notas_20")
    @Builder.Default
    private BigDecimal notas20 = BigDecimal.ZERO;

    @Column(name = "notas_50")
    @Builder.Default
    private BigDecimal notas50 = BigDecimal.ZERO;

    // ---- Moedas, subtotal em reais por denominacao ----
    @Column(name = "moedas_1")
    @Builder.Default
    private BigDecimal moedas1 = BigDecimal.ZERO;

    @Column(name = "moedas_050")
    @Builder.Default
    private BigDecimal moedas050 = BigDecimal.ZERO;

    @Column(name = "moedas_025")
    @Builder.Default
    private BigDecimal moedas025 = BigDecimal.ZERO;

    @Column(name = "moedas_010")
    @Builder.Default
    private BigDecimal moedas010 = BigDecimal.ZERO;

    @Column(name = "moedas_005")
    @Builder.Default
    private BigDecimal moedas005 = BigDecimal.ZERO;

    @Column(name = "valor_pix")
    @Builder.Default
    private BigDecimal valorPix = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_id", nullable = false)
    private Usuario registradoPor;

    @Column(name = "data_hora", nullable = false)
    @Builder.Default
    private LocalDateTime dataHora = LocalDateTime.now();

    @Transient
    public BigDecimal getValorNotas() {
        return nz(notas2).add(nz(notas5)).add(nz(notas10)).add(nz(notas20)).add(nz(notas50));
    }

    @Transient
    public BigDecimal getValorMoedas() {
        return nz(moedas1).add(nz(moedas050)).add(nz(moedas025)).add(nz(moedas010)).add(nz(moedas005));
    }

    @Transient
    public BigDecimal getValorTotal() {
        return getValorNotas().add(getValorMoedas()).add(nz(valorPix));
    }

    private static BigDecimal nz(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }
}
