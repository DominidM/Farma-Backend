package com.solvegrades.farma.categorias.domain.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.categorias.domain.entities.Categorias;

@Repository
public interface ICategoriasRepository extends JpaRepository<Categorias, Integer> {
    
    @Query("SELECT c FROM Categorias c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Categorias> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    @Query("SELECT c FROM Categorias c WHERE c.nombre = :nombre")
    Optional<Categorias> findByNombre(@Param("nombre") String nombre);
}
