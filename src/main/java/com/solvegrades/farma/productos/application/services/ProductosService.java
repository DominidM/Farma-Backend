package com.solvegrades.farma.productos.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.categorias.domain.entities.Categorias;
import com.solvegrades.farma.categorias.domain.repositories.ICategoriasRepository;
import com.solvegrades.farma.productos.application.dto.ProductosDTO;
import com.solvegrades.farma.productos.domain.entities.Productos;
import com.solvegrades.farma.productos.domain.repositories.IProductosRepository;
import com.solvegrades.farma.proveedores.domain.entities.Proveedores;
import com.solvegrades.farma.proveedores.domain.repositories.IProveedoresRepository;

@Service
@Transactional
public class ProductosService {

    @Autowired
    private IProductosRepository productosRepository;

    @Autowired
    private ICategoriasRepository categoriasRepository;

    @Autowired
    private IProveedoresRepository proveedoresRepository;

    @Transactional(readOnly = true)
    public List<ProductosDTO> findAll() {
        return productosRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProductosDTO findById(Integer id) {
        return productosRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProductosDTO> findByNombreContaining(String nombre) {
        return productosRepository.findByNombreContainingIgnoreCase(nombre)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countProductos() {
        return productosRepository.count();
    }

    public ProductosDTO save(ProductosDTO dto) {
        validateProducto(dto);
        Optional<Productos> existing = productosRepository.findByCodigoBarras(dto.getCodigoBarras());
        if (existing.isPresent()) {
            throw new RuntimeException("Ya existe un producto con el código de barras: " + dto.getCodigoBarras());
        }
        Productos entity = toEntity(dto);
        Productos saved = productosRepository.save(entity);
        return toDTO(saved);
    }

    public ProductosDTO update(Integer id, ProductosDTO dto) {
        validateProducto(dto);
        Productos existing = productosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        Optional<Productos> productoConMismoCodigo = productosRepository.findByCodigoBarras(dto.getCodigoBarras());
        if (productoConMismoCodigo.isPresent() && !productoConMismoCodigo.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe otro producto con el código de barras: " + dto.getCodigoBarras());
        }
        // Actualizar campos
        existing.setCodigoBarras(dto.getCodigoBarras());
        existing.setNombre(dto.getNombre());
        existing.setDescripcion(dto.getDescripcion());
        existing.setPrecioCompra(dto.getPrecioCompra());
        existing.setPrecioVenta(dto.getPrecioVenta());
        existing.setStock(dto.getStock());
        existing.setFechaCaducidad(dto.getFechaCaducidad());
        if (dto.getIdCategoria() != null) {
            Categorias categoria = categoriasRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getIdCategoria()));
            existing.setCategoria(categoria);
        } else {
            existing.setCategoria(null);
        }
        if (dto.getIdProveedor() != null) {
            Proveedores proveedor = proveedoresRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + dto.getIdProveedor()));
            existing.setProveedor(proveedor);
        } else {
            existing.setProveedor(null);
        }
        Productos updated = productosRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!productosRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productosRepository.deleteById(id);
    }

    private void validateProducto(ProductosDTO dto) {
        if (dto.getCodigoBarras() == null || dto.getCodigoBarras().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de barras es obligatorio");
        }
        if (dto.getCodigoBarras().length() > 50) {
            throw new IllegalArgumentException("El código de barras no puede exceder 50 caracteres");
        }
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        if (dto.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del producto no puede exceder 100 caracteres");
        }
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    private ProductosDTO toDTO(Productos p) {
        ProductosDTO dto = new ProductosDTO();
        dto.setId(p.getId());
        dto.setCodigoBarras(p.getCodigoBarras());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecioCompra(p.getPrecioCompra());
        dto.setPrecioVenta(p.getPrecioVenta());
        dto.setStock(p.getStock());
        if (p.getCategoria() != null) {
            dto.setIdCategoria(p.getCategoria().getId());
            dto.setNombreCategoria(p.getCategoria().getNombre());
        }
        if (p.getProveedor() != null) {
            dto.setIdProveedor(p.getProveedor().getId());
            dto.setNombreProveedor(p.getProveedor().getNombre());
        }
        dto.setFechaCaducidad(p.getFechaCaducidad());
        return dto;
    }

    private Productos toEntity(ProductosDTO dto) {
        Productos p = new Productos();
        p.setId(dto.getId());
        p.setCodigoBarras(dto.getCodigoBarras());
        p.setNombre(dto.getNombre());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecioCompra(dto.getPrecioCompra());
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setStock(dto.getStock());
        p.setFechaCaducidad(dto.getFechaCaducidad());
        if (dto.getIdCategoria() != null) {
            Categorias categoria = categoriasRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + dto.getIdCategoria()));
            p.setCategoria(categoria);
        }
        if (dto.getIdProveedor() != null) {
            Proveedores proveedor = proveedoresRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + dto.getIdProveedor()));
            p.setProveedor(proveedor);
        }
        return p;
    }
}