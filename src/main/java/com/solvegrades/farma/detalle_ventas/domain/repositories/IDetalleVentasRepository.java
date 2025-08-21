package com.solvegrades.farma.detalle_ventas.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.detalle_ventas.domain.entities.DetalleVentas;

@Repository
public interface IDetalleVentasRepository extends JpaRepository<DetalleVentas, Integer> {
    List<DetalleVentas> findByVentaId(Integer idVenta);
}