package com.solvegrades.farma.compras.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.compras.domain.entities.Compras;

@Repository
public interface IComprasRepository extends JpaRepository<Compras, Integer> {
    List<Compras> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta);
}