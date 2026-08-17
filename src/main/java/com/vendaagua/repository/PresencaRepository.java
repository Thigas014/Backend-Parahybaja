package com.vendaagua.repository;

import com.vendaagua.model.Presenca;
import com.vendaagua.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

    List<Presenca> findByDataOrderByUsuarioNomeAsc(LocalDate data);

    Optional<Presenca> findByDataAndUsuario(LocalDate data, Usuario usuario);

    @Query("SELECT COALESCE(SUM(p.taxaValor),0) FROM Presenca p " +
           "WHERE p.taxaPaga = true AND p.data BETWEEN :inicio AND :fim")
    BigDecimal totalTaxasPagasPorPeriodo(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);
}
