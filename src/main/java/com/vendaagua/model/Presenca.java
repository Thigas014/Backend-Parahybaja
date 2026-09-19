package com.vendaagua.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Registro de presenca de um membro em um dia de venda especifico.
 *
 * O membro so pode escrever a justificativa (antes da data acontecer); ele
 * NUNCA marca o proprio status. Quem decide o status final e sempre o admin,
 * que ve a justificativa (se houver) e escolhe entre:
 *  - PRESENTE: participou, sem taxa.
 *  - JUSTIFICADO: faltou mas a justificativa foi aceita, taxa reduzida.
 *  - AUSENTE: faltou sem justificativa aceita, taxa cheia.
 *
 * O valor da taxa e travado no momento em que o admin marca o status (nao
 * muda retroativamente se o admin alterar os valores configurados depois).
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

    /** null = ainda nao decidido pelo admin. Sem "nullable = false" de proposito (coluna nova). */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPresenca status;

    /** Justificativa escrita pelo proprio membro, antes da data acontecer. */
    @Column(columnDefinition = "TEXT")
    private String justificativa;

    /** Valor da taxa aplicada (travado no momento em que o admin definiu o status). */
    @Column(name = "taxa_valor", precision = 10, scale = 2)
    private BigDecimal taxaValor;

    @Column(name = "taxa_paga", nullable = false)
    @Builder.Default
    private Boolean taxaPaga = false;
}
