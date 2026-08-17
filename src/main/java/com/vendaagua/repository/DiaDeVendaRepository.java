package com.vendaagua.repository;

import com.vendaagua.model.DiaDeVenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaDeVendaRepository extends JpaRepository<DiaDeVenda, Long> {

    List<DiaDeVenda> findByDataBetweenOrderByDataAsc(LocalDate inicio, LocalDate fim);

    List<DiaDeVenda> findAllByOrderByDataAsc();

    Optional<DiaDeVenda> findByData(LocalDate data);

    /** Data de venda marcada mais recente, ate hoje (inclusive). */
    Optional<DiaDeVenda> findFirstByDataLessThanEqualOrderByDataDesc(LocalDate hoje);

    /** Proxima data de venda marcada, a partir de hoje (inclusive). */
    Optional<DiaDeVenda> findFirstByDataGreaterThanEqualOrderByDataAsc(LocalDate hoje);
}
