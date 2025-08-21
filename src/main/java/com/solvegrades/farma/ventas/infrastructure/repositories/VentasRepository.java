package com.solvegrades.farma.ventas.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.ventas.domain.entities.Ventas;
import com.solvegrades.farma.ventas.domain.repositories.IVentasRepository;
import com.solvegrades.farma.ventas.infrastructure.model.VentasModel;

@Component
public class VentasRepository {

    @Autowired
    private IVentasRepository iVentasRepository;

    public List<Ventas> findAll() {
        return iVentasRepository.findAll();
    }

    public Optional<Ventas> findById(Integer id) {
        return iVentasRepository.findById(id);
    }

    public Ventas save(Ventas venta) {
        return iVentasRepository.save(venta);
    }

    public void deleteById(Integer id) {
        iVentasRepository.deleteById(id);
    }

    public List<Ventas> findByFechaBetween(java.time.LocalDateTime desde, java.time.LocalDateTime hasta) {
        return iVentasRepository.findByFechaBetween(desde, hasta);
    }

    public boolean existsById(Integer id) {
        return iVentasRepository.existsById(id);
    }

    public long count() {
        return iVentasRepository.count();
    }

    // Mapping methods (opcional)
    public VentasModel toModel(Ventas v) {
        return new VentasModel(
            v.getId(),
            v.getFecha(),
            v.getCliente() != null ? v.getCliente().getId() : null,
            v.getEmpleado() != null ? v.getEmpleado().getId() : null,
            v.getTotal()
        );
    }

    public Ventas toEntity(VentasModel m) {
        Ventas v = new Ventas();
        v.setId(m.getId());
        v.setFecha(m.getFecha());
        // Relaciones se asignan en el service
        v.setTotal(m.getTotal());
        return v;
    }
}