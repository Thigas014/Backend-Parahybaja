package com.vendaagua.repository;

import com.vendaagua.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findAllByOrderByDataDesc();

    List<Venda> findByDataBetweenOrderByDataDesc(LocalDate inicio, LocalDate fim);

    @Query("SELECT COALESCE(SUM(" +
           "COALESCE(v.notas2,0) + COALESCE(v.notas5,0) + COALESCE(v.notas10,0) + COALESCE(v.notas20,0) + COALESCE(v.notas50,0) + " +
           "COALESCE(v.moedas1,0) + COALESCE(v.moedas050,0) + COALESCE(v.moedas025,0) + COALESCE(v.moedas010,0) + COALESCE(v.moedas005,0) + " +
           "COALESCE(v.valorPix,0)" +
           "),0) FROM Venda v WHERE v.data BETWEEN :inicio AND :fim")
    BigDecimal totalPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    @Query("SELECT MAX(v.data) FROM Venda v")
    LocalDate ultimaData();
}
