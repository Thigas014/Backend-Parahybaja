package com.vendaagua.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Registro de presenca/ausencia de um membro em um dia de venda especifico.
 * Um membro ausente pode justificar (texto livre); o admin marca presente/
 * ausente oficialmente e, se ausente, uma taxa e aplicada (valor travado no
 * momento em que a ausencia foi marcada, para nao mudar retroativamente se o
 * admin alterar o valor da taxa depois).
 */
@Entity
@Table(name = "presencas", uniqueConstraints = @UniqueConstraint(columnNames = {"data", "usuario_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Presenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** null = ainda nao marcado pelo admin; true = presente; false = ausente. */
    @Column
    private Boolean presente;

    /** Justificativa escrita pelo proprio membro ausente. */
    @Column(columnDefinition = "TEXT")
    private String justificativa;

    /** Valor da taxa aplicada (travado no momento em que foi marcado ausente). */
    @Column(name = "taxa_valor", precision = 10, scale = 2)
    private BigDecimal taxaValor;

    @Column(name = "taxa_paga", nullable = false)
    @Builder.Default
    private Boolean taxaPaga = false;
}
