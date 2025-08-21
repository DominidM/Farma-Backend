package com.solvegrades.farma.detalle_compras.application.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.compras.domain.entities.Compras;
import com.solvegrades.farma.compras.domain.repositories.IComprasRepository;
import com.solvegrades.farma.detalle_compras.application.dto.DetalleComprasDTO;
import com.solvegrades.farma.detalle_compras.domain.entities.DetalleCompras;
import com.solvegrades.farma.detalle_compras.domain.repositories.IDetalleComprasRepository;
import com.solvegrades.farma.productos.domain.entities.Productos;
import com.solvegrades.farma.productos.domain.repositories.IProductosRepository;

@Service
@Transactional
public class DetalleComprasService {

    @Autowired
    private IDetalleComprasRepository detalleComprasRepository;

    @Autowired
    private IComprasRepository comprasRepository;

    @Autowired
    private IProductosRepository productosRepository;

    @Transactional(readOnly = true)
    public List<DetalleComprasDTO> findAll() {
        return detalleComprasRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public DetalleComprasDTO findById(Integer id) {
        return detalleComprasRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Detalle de compra no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<DetalleComprasDTO> findByCompraId(Integer idCompra) {
        return detalleComprasRepository.findByCompraId(idCompra).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countDetalleCompras() {
        return detalleComprasRepository.count();
    }

    public DetalleComprasDTO save(DetalleComprasDTO dto) {
        validateDetalleCompra(dto);
        DetalleCompras entity = toEntity(dto);
        DetalleCompras saved = detalleComprasRepository.save(entity);
        return toDTO(saved);
    }

    public DetalleComprasDTO update(Integer id, DetalleComprasDTO dto) {
        validateDetalleCompra(dto);
        DetalleCompras existing = detalleComprasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de compra no encontrado con id: " + id));
        if (dto.getIdCompra() != null) {
            Compras compra = comprasRepository.findById(dto.getIdCompra())
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + dto.getIdCompra()));
            existing.setCompra(compra);
        }
        if (dto.getIdProducto() != null) {
            Productos producto = productosRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + dto.getIdProducto()));
            existing.setProducto(producto);
        }
        existing.setCantidad(dto.getCantidad());
        existing.setPrecioUnitario(dto.getPrecioUnitario());
        existing.setSubtotal(dto.getSubtotal());
        DetalleCompras updated = detalleComprasRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!detalleComprasRepository.existsById(id)) {
            throw new RuntimeException("Detalle de compra no encontrado con id: " + id);
        }
        detalleComprasRepository.deleteById(id);
    }

    private void validateDetalleCompra(DetalleComprasDTO dto) {
        if (dto.getIdCompra() == null) {
            throw new IllegalArgumentException("La compra es obligatoria.");
        }
        if (dto.getIdProducto() == null) {
            throw new IllegalArgumentException("El producto es obligatorio.");
        }
        if (dto.getCantidad() == null || dto.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        if (dto.getPrecioUnitario() == null || dto.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser un valor positivo.");
        }
        if (dto.getSubtotal() == null || dto.getSubtotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El subtotal debe ser un valor positivo.");
        }
    }

    private DetalleComprasDTO toDTO(DetalleCompras d) {
        DetalleComprasDTO dto = new DetalleComprasDTO();
        dto.setId(d.getId());
        if (d.getCompra() != null) {
            dto.setIdCompra(d.getCompra().getId());
        }
        if (d.getProducto() != null) {
            dto.setIdProducto(d.getProducto().getId());
            dto.setNombreProducto(d.getProducto().getNombre());
        }
        dto.setCantidad(d.getCantidad());
        dto.setPrecioUnitario(d.getPrecioUnitario());
        dto.setSubtotal(d.getSubtotal());
        return dto;
    }

    private DetalleCompras toEntity(DetalleComprasDTO dto) {
        DetalleCompras d = new DetalleCompras();
        d.setId(dto.getId());
        if (dto.getIdCompra() != null) {
            Compras compra = comprasRepository.findById(dto.getIdCompra())
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + dto.getIdCompra()));
            d.setCompra(compra);
        }
        if (dto.getIdProducto() != null) {
            Productos producto = productosRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + dto.getIdProducto()));
            d.setProducto(producto);
        }
        d.setCantidad(dto.getCantidad());
        d.setPrecioUnitario(dto.getPrecioUnitario());
        d.setSubtotal(dto.getSubtotal());
        return d;
    }
}