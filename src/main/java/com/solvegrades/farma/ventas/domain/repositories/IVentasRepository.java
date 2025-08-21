package com.solvegrades.farma.ventas.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.ventas.domain.entities.Ventas;

@Repository
public interface IVentasRepository extends JpaRepository<Ventas, Integer> {
    List<Ventas> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}