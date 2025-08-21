package com.solvegrades.farma.detalle_compras.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.solvegrades.farma.detalle_compras.domain.entities.DetalleCompras;

@Repository
public interface IDetalleComprasRepository extends JpaRepository<DetalleCompras, Integer> {
    List<DetalleCompras> findByCompraId(Integer idCompra);
}