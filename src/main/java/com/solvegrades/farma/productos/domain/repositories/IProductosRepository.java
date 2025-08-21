package com.solvegrades.farma.productos.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.productos.domain.entities.Productos;

@Repository
public interface IProductosRepository extends JpaRepository<Productos, Integer> {
    Optional<Productos> findByCodigoBarras(String codigoBarras);
    List<Productos> findByNombreContainingIgnoreCase(String nombre);
}