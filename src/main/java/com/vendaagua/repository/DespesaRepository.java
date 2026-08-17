package com.vendaagua.repository;

import com.vendaagua.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findAllByOrderByDataHoraDesc();

    @Query("SELECT COALESCE(SUM(d.valor),0) FROM Despesa d")
    BigDecimal totalDespesas();

    @Query("SELECT COALESCE(SUM(d.valor),0) FROM Despesa d WHERE d.dataHora BETWEEN :inicio AND :fim")
    BigDecimal totalPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
