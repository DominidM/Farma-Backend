package com.solvegrades.farma.proveedores.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.proveedores.application.dto.ProveedoresDTO;
import com.solvegrades.farma.proveedores.domain.entities.Proveedores;
import com.solvegrades.farma.proveedores.domain.repositories.IProveedoresRepository;

@Service
@Transactional
public class ProveedoresService {

    @Autowired
    private IProveedoresRepository proveedoresRepository;

    @Transactional(readOnly = true)
    public List<ProveedoresDTO> findAll() {
        return proveedoresRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ProveedoresDTO findById(Integer id) {
        return proveedoresRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ProveedoresDTO> findByNombreContaining(String nombre) {
        return proveedoresRepository.findByNombreContainingIgnoreCase(nombre)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countProveedores() {
        return proveedoresRepository.count();
    }

    public ProveedoresDTO save(ProveedoresDTO dto) {
        validateProveedor(dto);
        Optional<Proveedores> existing = proveedoresRepository.findByNombre(dto.getNombre());
        if (existing.isPresent()) {
            throw new RuntimeException("Ya existe un proveedor con el nombre: " + dto.getNombre());
        }
        Proveedores proveedor = toEntity(dto);
        Proveedores saved = proveedoresRepository.save(proveedor);
        return toDTO(saved);
    }

    public ProveedoresDTO update(Integer id, ProveedoresDTO dto) {
        validateProveedor(dto);
        Proveedores existing = proveedoresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con id: " + id));
        Optional<Proveedores> proveedorConMismoNombre = proveedoresRepository.findByNombre(dto.getNombre());
        if (proveedorConMismoNombre.isPresent() && !proveedorConMismoNombre.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe otro proveedor con el nombre: " + dto.getNombre());
        }
        existing.setNombre(dto.getNombre());
        existing.setTelefono(dto.getTelefono());
        existing.setDireccion(dto.getDireccion());
        Proveedores updated = proveedoresRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!proveedoresRepository.existsById(id)) {
            throw new RuntimeException("Proveedor no encontrado con id: " + id);
        }
        proveedoresRepository.deleteById(id);
    }

    private void validateProveedor(ProveedoresDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del proveedor es obligatorio");
        }
        if (dto.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del proveedor no puede exceder 100 caracteres");
        }
        if (dto.getTelefono() != null && dto.getTelefono().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }
        if (dto.getDireccion() != null && dto.getDireccion().length() > 200) {
            throw new IllegalArgumentException("La dirección no puede exceder 200 caracteres");
        }
    }

    private ProveedoresDTO toDTO(Proveedores proveedor) {
        return new ProveedoresDTO(proveedor.getId(), proveedor.getNombre(), proveedor.getTelefono(), proveedor.getDireccion());
    }

    private Proveedores toEntity(ProveedoresDTO dto) {
        Proveedores proveedor = new Proveedores();
        proveedor.setId(dto.getId());
        proveedor.setNombre(dto.getNombre() != null ? dto.getNombre().trim() : null);
        proveedor.setTelefono(dto.getTelefono() != null ? dto.getTelefono().trim() : null);
        proveedor.setDireccion(dto.getDireccion() != null ? dto.getDireccion().trim() : null);
        return proveedor;
    }
}