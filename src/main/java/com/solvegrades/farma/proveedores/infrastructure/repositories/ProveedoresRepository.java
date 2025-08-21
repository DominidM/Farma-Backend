package com.solvegrades.farma.proveedores.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.proveedores.domain.entities.Proveedores;
import com.solvegrades.farma.proveedores.domain.repositories.IProveedoresRepository;
import com.solvegrades.farma.proveedores.infrastructure.model.ProveedoresModel;

@Component
public class ProveedoresRepository {

    @Autowired
    private IProveedoresRepository iProveedoresRepository;

    public List<Proveedores> findAll() {
        return iProveedoresRepository.findAll();
    }

    public Optional<Proveedores> findById(Integer id) {
        return iProveedoresRepository.findById(id);
    }

    public Proveedores save(Proveedores proveedor) {
        return iProveedoresRepository.save(proveedor);
    }

    public void deleteById(Integer id) {
        iProveedoresRepository.deleteById(id);
    }

    public List<Proveedores> findByNombreContaining(String nombre) {
        return iProveedoresRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Proveedores> findByNombre(String nombre) {
        return iProveedoresRepository.findByNombre(nombre);
    }

    public boolean existsById(Integer id) {
        return iProveedoresRepository.existsById(id);
    }

    public long count() {
        return iProveedoresRepository.count();
    }

    // Métodos para model mapping (opcional)
    public ProveedoresModel toModel(Proveedores proveedor) {
        return new ProveedoresModel(proveedor.getId(), proveedor.getNombre(), proveedor.getTelefono(), proveedor.getDireccion());
    }

    public Proveedores toEntity(ProveedoresModel model) {
        return new Proveedores(model.getId(), model.getNombre(), model.getTelefono(), model.getDireccion());
    }
}