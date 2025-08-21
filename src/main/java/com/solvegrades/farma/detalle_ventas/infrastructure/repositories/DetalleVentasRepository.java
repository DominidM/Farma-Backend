package com.solvegrades.farma.detalle_ventas.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.detalle_ventas.domain.entities.DetalleVentas;
import com.solvegrades.farma.detalle_ventas.domain.repositories.IDetalleVentasRepository;
import com.solvegrades.farma.detalle_ventas.infrastructure.model.DetalleVentasModel;

@Component
public class DetalleVentasRepository {

    @Autowired
    private IDetalleVentasRepository iDetalleVentasRepository;

    public List<DetalleVentas> findAll() {
        return iDetalleVentasRepository.findAll();
    }

    public Optional<DetalleVentas> findById(Integer id) {
        return iDetalleVentasRepository.findById(id);
    }

    public DetalleVentas save(DetalleVentas detalle) {
        return iDetalleVentasRepository.save(detalle);
    }

    public void deleteById(Integer id) {
        iDetalleVentasRepository.deleteById(id);
    }

    public List<DetalleVentas> findByVentaId(Integer idVenta) {
        return iDetalleVentasRepository.findByVentaId(idVenta);
    }

    public boolean existsById(Integer id) {
        return iDetalleVentasRepository.existsById(id);
    }

    public long count() {
        return iDetalleVentasRepository.count();
    }

    // Mapping (opcional)
    public DetalleVentasModel toModel(DetalleVentas d) {
        return new DetalleVentasModel(
            d.getId(),
            d.getVenta() != null ? d.getVenta().getId() : null,
            d.getProducto() != null ? d.getProducto().getId() : null,
            d.getCantidad(),
            d.getPrecioUnitario(),
            d.getSubtotal()
        );
    }

    public DetalleVentas toEntity(DetalleVentasModel m) {
        DetalleVentas d = new DetalleVentas();
        d.setId(m.getId());
        d.setCantidad(m.getCantidad());
        d.setPrecioUnitario(m.getPrecioUnitario());
        d.setSubtotal(m.getSubtotal());
        // Las relaciones se asignan en el service
        return d;
    }
}