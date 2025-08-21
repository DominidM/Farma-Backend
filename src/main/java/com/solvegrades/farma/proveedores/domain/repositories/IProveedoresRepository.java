package com.solvegrades.farma.proveedores.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.proveedores.domain.entities.Proveedores;

@Repository
public interface IProveedoresRepository extends JpaRepository<Proveedores, Integer> {
    List<Proveedores> findByNombreContainingIgnoreCase(String nombre);
    Optional<Proveedores> findByNombre(String nombre);
}