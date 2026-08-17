package com.vendaagua.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Contribuicao financeira que conta para a meta mas nao vem de venda de
 * garrafas (ex: doacao, patrocinio, sobra de caixa de outro evento).
 * Registrada apenas por administradores.
 */
@Entity
@Table(name = "aportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registrado_por_id", nullable = false)
    private Usuario registradoPor;

    @Column(name = "data_hora", nullable = false)
    @Builder.Default
    private LocalDateTime dataHora = LocalDateTime.now();
}
