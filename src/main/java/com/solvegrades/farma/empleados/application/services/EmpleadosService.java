package com.solvegrades.farma.empleados.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.empleados.application.dto.EmpleadosDTO;
import com.solvegrades.farma.empleados.domain.entities.Empleados;
import com.solvegrades.farma.empleados.domain.repositories.IEmpleadosRepository;

@Service
@Transactional
public class EmpleadosService {

    @Autowired
    private IEmpleadosRepository empleadosRepository;

    @Transactional(readOnly = true)
    public List<EmpleadosDTO> findAll() {
        return empleadosRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public EmpleadosDTO findById(Integer id) {
        return empleadosRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<EmpleadosDTO> findByNombreContaining(String nombre) {
        return empleadosRepository.findByNombreContainingIgnoreCase(nombre)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countEmpleados() {
        return empleadosRepository.count();
    }

    public EmpleadosDTO save(EmpleadosDTO dto) {
        validateEmpleado(dto);
        Optional<Empleados> existing = empleadosRepository.findByUsuario(dto.getUsuario());
        if (existing.isPresent()) {
            throw new RuntimeException("Ya existe un empleado con el usuario: " + dto.getUsuario());
        }
        Empleados empleado = toEntity(dto);
        Empleados saved = empleadosRepository.save(empleado);
        return toDTO(saved);
    }

    public EmpleadosDTO update(Integer id, EmpleadosDTO dto) {
        validateEmpleado(dto);
        Empleados existing = empleadosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + id));
        Optional<Empleados> empleadoConMismoUsuario = empleadosRepository.findByUsuario(dto.getUsuario());
        if (empleadoConMismoUsuario.isPresent() && !empleadoConMismoUsuario.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe otro empleado con el usuario: " + dto.getUsuario());
        }
        existing.setUsuario(dto.getUsuario());
        existing.setContrasena(dto.getContrasena());
        existing.setNombre(dto.getNombre());
        Empleados updated = empleadosRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!empleadosRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con id: " + id);
        }
        empleadosRepository.deleteById(id);
    }

    private void validateEmpleado(EmpleadosDTO dto) {
        if (dto.getUsuario() == null || dto.getUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        if (dto.getUsuario().length() > 50) {
            throw new IllegalArgumentException("El usuario no puede exceder 50 caracteres");
        }
        if (dto.getContrasena() == null || dto.getContrasena().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        if (dto.getContrasena().length() > 100) {
            throw new IllegalArgumentException("La contraseña no puede exceder 100 caracteres");
        }
        if (dto.getNombre() != null && dto.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
    }

    private EmpleadosDTO toDTO(Empleados empleado) {
        return new EmpleadosDTO(empleado.getId(), empleado.getUsuario(), empleado.getContrasena(), empleado.getNombre());
    }

    private Empleados toEntity(EmpleadosDTO dto) {
        Empleados empleado = new Empleados();
        empleado.setId(dto.getId());
        empleado.setUsuario(dto.getUsuario() != null ? dto.getUsuario().trim() : null);
        empleado.setContrasena(dto.getContrasena() != null ? dto.getContrasena().trim() : null);
        empleado.setNombre(dto.getNombre() != null ? dto.getNombre().trim() : null);
        return empleado;
    }
}