package com.solvegrades.farma.detalle_ventas.application.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.detalle_ventas.application.dto.DetalleVentasDTO;
import com.solvegrades.farma.detalle_ventas.domain.entities.DetalleVentas;
import com.solvegrades.farma.detalle_ventas.domain.repositories.IDetalleVentasRepository;
import com.solvegrades.farma.productos.domain.entities.Productos;
import com.solvegrades.farma.productos.domain.repositories.IProductosRepository;
import com.solvegrades.farma.ventas.domain.entities.Ventas;
import com.solvegrades.farma.ventas.domain.repositories.IVentasRepository;

@Service
@Transactional
public class DetalleVentasService {

    @Autowired
    private IDetalleVentasRepository detalleVentasRepository;

    @Autowired
    private IVentasRepository ventasRepository;

    @Autowired
    private IProductosRepository productosRepository;

    @Transactional(readOnly = true)
    public List<DetalleVentasDTO> findAll() {
        return detalleVentasRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public DetalleVentasDTO findById(Integer id) {
        return detalleVentasRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<DetalleVentasDTO> findByVentaId(Integer idVenta) {
        return detalleVentasRepository.findByVentaId(idVenta).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countDetalleVentas() {
        return detalleVentasRepository.count();
    }

    public DetalleVentasDTO save(DetalleVentasDTO dto) {
        validateDetalleVenta(dto);
        DetalleVentas entity = toEntity(dto);
        DetalleVentas saved = detalleVentasRepository.save(entity);
        return toDTO(saved);
    }

    public DetalleVentasDTO update(Integer id, DetalleVentasDTO dto) {
        validateDetalleVenta(dto);
        DetalleVentas existing = detalleVentasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detalle de venta no encontrado con id: " + id));
        if (dto.getIdVenta() != null) {
            Ventas venta = ventasRepository.findById(dto.getIdVenta())
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + dto.getIdVenta()));
            existing.setVenta(venta);
        }
        if (dto.getIdProducto() != null) {
            Productos producto = productosRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + dto.getIdProducto()));
            existing.setProducto(producto);
        }
        existing.setCantidad(dto.getCantidad());
        existing.setPrecioUnitario(dto.getPrecioUnitario());
        existing.setSubtotal(dto.getSubtotal());
        DetalleVentas updated = detalleVentasRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!detalleVentasRepository.existsById(id)) {
            throw new RuntimeException("Detalle de venta no encontrado con id: " + id);
        }
        detalleVentasRepository.deleteById(id);
    }

    private void validateDetalleVenta(DetalleVentasDTO dto) {
        if (dto.getIdVenta() == null) {
            throw new IllegalArgumentException("La venta es obligatoria.");
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

    private DetalleVentasDTO toDTO(DetalleVentas d) {
        DetalleVentasDTO dto = new DetalleVentasDTO();
        dto.setId(d.getId());
        if (d.getVenta() != null) {
            dto.setIdVenta(d.getVenta().getId());
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

    private DetalleVentas toEntity(DetalleVentasDTO dto) {
        DetalleVentas d = new DetalleVentas();
        d.setId(dto.getId());
        if (dto.getIdVenta() != null) {
            Ventas venta = ventasRepository.findById(dto.getIdVenta())
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + dto.getIdVenta()));
            d.setVenta(venta);
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