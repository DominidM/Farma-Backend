package com.solvegrades.farma.productos.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.productos.domain.entities.Productos;
import com.solvegrades.farma.productos.domain.repositories.IProductosRepository;
import com.solvegrades.farma.productos.infrastructure.model.ProductosModel;

@Component
public class ProductosRepository {

    @Autowired
    private IProductosRepository iProductosRepository;

    public List<Productos> findAll() {
        return iProductosRepository.findAll();
    }

    public Optional<Productos> findById(Integer id) {
        return iProductosRepository.findById(id);
    }

    public Productos save(Productos producto) {
        return iProductosRepository.save(producto);
    }

    public void deleteById(Integer id) {
        iProductosRepository.deleteById(id);
    }

    public List<Productos> findByNombreContaining(String nombre) {
        return iProductosRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Productos> findByCodigoBarras(String codigoBarras) {
        return iProductosRepository.findByCodigoBarras(codigoBarras);
    }

    public boolean existsById(Integer id) {
        return iProductosRepository.existsById(id);
    }

    public long count() {
        return iProductosRepository.count();
    }

    // Mapping methods (opcional)
    public ProductosModel toModel(Productos p) {
        return new ProductosModel(
            p.getId(),
            p.getCodigoBarras(),
            p.getNombre(),
            p.getDescripcion(),
            p.getPrecioCompra(),
            p.getPrecioVenta(),
            p.getStock(),
            p.getCategoria() != null ? p.getCategoria().getId() : null,
            p.getProveedor() != null ? p.getProveedor().getId() : null,
            p.getFechaCaducidad()
        );
    }

    public Productos toEntity(ProductosModel m) {
        Productos p = new Productos();
        p.setId(m.getId());
        p.setCodigoBarras(m.getCodigoBarras());
        p.setNombre(m.getNombre());
        p.setDescripcion(m.getDescripcion());
        p.setPrecioCompra(m.getPrecioCompra());
        p.setPrecioVenta(m.getPrecioVenta());
        p.setStock(m.getStock());
        // Las relaciones de categoria y proveedor se asignan en el Service
        p.setFechaCaducidad(m.getFechaCaducidad());
        return p;
    }
}