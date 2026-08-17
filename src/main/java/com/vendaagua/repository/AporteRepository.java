package com.vendaagua.repository;

import com.vendaagua.model.Aporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AporteRepository extends JpaRepository<Aporte, Long> {

    List<Aporte> findAllByOrderByDataHoraDesc();

    @Query("SELECT COALESCE(SUM(a.valor),0) FROM Aporte a")
    BigDecimal totalAportes();

    @Query("SELECT COALESCE(SUM(a.valor),0) FROM Aporte a WHERE a.dataHora BETWEEN :inicio AND :fim")
    BigDecimal totalPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
}
