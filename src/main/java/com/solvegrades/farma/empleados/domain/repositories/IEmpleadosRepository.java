package com.solvegrades.farma.empleados.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.empleados.domain.entities.Empleados;

@Repository
public interface IEmpleadosRepository extends JpaRepository<Empleados, Integer> {
    List<Empleados> findByNombreContainingIgnoreCase(String nombre);
    Optional<Empleados> findByUsuario(String usuario);
}