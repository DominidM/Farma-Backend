package com.solvegrades.farma.detalle_compras.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.detalle_compras.domain.entities.DetalleCompras;
import com.solvegrades.farma.detalle_compras.domain.repositories.IDetalleComprasRepository;
import com.solvegrades.farma.detalle_compras.infrastructure.model.DetalleComprasModel;

@Component
public class DetalleComprasRepository {

    @Autowired
    private IDetalleComprasRepository iDetalleComprasRepository;

    public List<DetalleCompras> findAll() {
        return iDetalleComprasRepository.findAll();
    }

    public Optional<DetalleCompras> findById(Integer id) {
        return iDetalleComprasRepository.findById(id);
    }

    public DetalleCompras save(DetalleCompras detalle) {
        return iDetalleComprasRepository.save(detalle);
    }

    public void deleteById(Integer id) {
        iDetalleComprasRepository.deleteById(id);
    }

    public List<DetalleCompras> findByCompraId(Integer idCompra) {
        return iDetalleComprasRepository.findByCompraId(idCompra);
    }

    public boolean existsById(Integer id) {
        return iDetalleComprasRepository.existsById(id);
    }

    public long count() {
        return iDetalleComprasRepository.count();
    }

    // Mapping (opcional)
    public DetalleComprasModel toModel(DetalleCompras d) {
        return new DetalleComprasModel(
            d.getId(),
            d.getCompra() != null ? d.getCompra().getId() : null,
            d.getProducto() != null ? d.getProducto().getId() : null,
            d.getCantidad(),
            d.getPrecioUnitario(),
            d.getSubtotal()
        );
    }

    public DetalleCompras toEntity(DetalleComprasModel m) {
        DetalleCompras d = new DetalleCompras();
        d.setId(m.getId());
        d.setCantidad(m.getCantidad());
        d.setPrecioUnitario(m.getPrecioUnitario());
        d.setSubtotal(m.getSubtotal());
        // Las relaciones se asignan en el service si es necesario
        return d;
    }
}