package com.vendaagua.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "configuracoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configuracao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Meta semanal (mesmo valor toda semana, reseta a cada sabado). */
    @Column(name = "meta_financeira", nullable = false, precision = 12, scale = 2)
    private BigDecimal metaFinanceira;

    /**
     * Valor da taxa cobrada de quem falta no dia de venda. Sem "nullable = false"
     * de proposito (evita quebrar ALTER TABLE em bancos com dados existentes).
     */
    @Column(name = "valor_taxa_ausencia")
    @Builder.Default
    private BigDecimal valorTaxaAusencia = BigDecimal.ZERO;
}
