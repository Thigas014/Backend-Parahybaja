package com.vendaagua.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Data marcada pelo admin no calendario como um dia oficial de venda.
 */
@Entity
@Table(name = "dias_de_venda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaDeVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "criado_por_id", nullable = false)
    private Usuario criadoPor;
}
