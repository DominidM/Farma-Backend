package com.solvegrades.farma.compras.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.compras.domain.entities.Compras;
import com.solvegrades.farma.compras.domain.repositories.IComprasRepository;
import com.solvegrades.farma.compras.infrastructure.model.ComprasModel;

@Component
public class ComprasRepository {

    @Autowired
    private IComprasRepository iComprasRepository;

    public List<Compras> findAll() {
        return iComprasRepository.findAll();
    }

    public Optional<Compras> findById(Integer id) {
        return iComprasRepository.findById(id);
    }

    public Compras save(Compras compra) {
        return iComprasRepository.save(compra);
    }

    public void deleteById(Integer id) {
        iComprasRepository.deleteById(id);
    }

    public List<Compras> findByFechaBetween(java.time.LocalDateTime desde, java.time.LocalDateTime hasta) {
        return iComprasRepository.findByFechaBetween(desde, hasta);
    }

    public boolean existsById(Integer id) {
        return iComprasRepository.existsById(id);
    }

    public long count() {
        return iComprasRepository.count();
    }

    // Mapping methods (opcional)
    public ComprasModel toModel(Compras c) {
        return new ComprasModel(
            c.getId(),
            c.getFecha(),
            c.getProveedor() != null ? c.getProveedor().getId() : null,
            c.getTotal()
        );
    }

    public Compras toEntity(ComprasModel m) {
        Compras c = new Compras();
        c.setId(m.getId());
        c.setFecha(m.getFecha());
        // Relación proveedor se asigna en el service si es necesario
        c.setTotal(m.getTotal());
        return c;
    }
}