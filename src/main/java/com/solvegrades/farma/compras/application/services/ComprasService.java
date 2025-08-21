package com.solvegrades.farma.compras.application.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.compras.application.dto.ComprasDTO;
import com.solvegrades.farma.compras.domain.entities.Compras;
import com.solvegrades.farma.compras.domain.repositories.IComprasRepository;
import com.solvegrades.farma.proveedores.domain.entities.Proveedores;
import com.solvegrades.farma.proveedores.domain.repositories.IProveedoresRepository;

@Service
@Transactional
public class ComprasService {

    @Autowired
    private IComprasRepository comprasRepository;

    @Autowired
    private IProveedoresRepository proveedoresRepository;

    @Transactional(readOnly = true)
    public List<ComprasDTO> findAll() {
        return comprasRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ComprasDTO findById(Integer id) {
        return comprasRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ComprasDTO> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta) {
        return comprasRepository.findByFechaBetween(desde, hasta)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countCompras() {
        return comprasRepository.count();
    }

    public ComprasDTO save(ComprasDTO dto) {
        validateCompra(dto);
        Compras compra = toEntity(dto);
        compra.setFecha(LocalDateTime.now());
        Compras saved = comprasRepository.save(compra);
        return toDTO(saved);
    }

    public ComprasDTO update(Integer id, ComprasDTO dto) {
        validateCompra(dto);
        Compras existing = comprasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + id));
        if (dto.getIdProveedor() != null) {
            Proveedores proveedor = proveedoresRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + dto.getIdProveedor()));
            existing.setProveedor(proveedor);
        }
        if (dto.getTotal() != null) {
            existing.setTotal(dto.getTotal());
        }
        Compras updated = comprasRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!comprasRepository.existsById(id)) {
            throw new RuntimeException("Compra no encontrada con id: " + id);
        }
        comprasRepository.deleteById(id);
    }

    private void validateCompra(ComprasDTO dto) {
        if (dto.getIdProveedor() == null) {
            throw new IllegalArgumentException("El proveedor es obligatorio.");
        }
        if (dto.getTotal() == null || dto.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total debe ser un valor positivo.");
        }
    }

    private ComprasDTO toDTO(Compras c) {
        ComprasDTO dto = new ComprasDTO();
        dto.setId(c.getId());
        dto.setFecha(c.getFecha());
        if (c.getProveedor() != null) {
            dto.setIdProveedor(c.getProveedor().getId());
            dto.setNombreProveedor(c.getProveedor().getNombre());
        }
        dto.setTotal(c.getTotal());
        return dto;
    }

    private Compras toEntity(ComprasDTO dto) {
        Compras c = new Compras();
        c.setId(dto.getId());
        c.setFecha(dto.getFecha());
        if (dto.getIdProveedor() != null) {
            Proveedores proveedor = proveedoresRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + dto.getIdProveedor()));
            c.setProveedor(proveedor);
        }
        c.setTotal(dto.getTotal());
        return c;
    }
}