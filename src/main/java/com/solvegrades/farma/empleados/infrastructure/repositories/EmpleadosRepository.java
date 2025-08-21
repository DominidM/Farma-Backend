package com.solvegrades.farma.empleados.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.empleados.domain.entities.Empleados;
import com.solvegrades.farma.empleados.domain.repositories.IEmpleadosRepository;
import com.solvegrades.farma.empleados.infrastructure.model.EmpleadosModel;

@Component
public class EmpleadosRepository {

    @Autowired
    private IEmpleadosRepository iEmpleadosRepository;

    public List<Empleados> findAll() {
        return iEmpleadosRepository.findAll();
    }

    public Optional<Empleados> findById(Integer id) {
        return iEmpleadosRepository.findById(id);
    }

    public Empleados save(Empleados empleado) {
        return iEmpleadosRepository.save(empleado);
    }

    public void deleteById(Integer id) {
        iEmpleadosRepository.deleteById(id);
    }

    public List<Empleados> findByNombreContaining(String nombre) {
        return iEmpleadosRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Empleados> findByUsuario(String usuario) {
        return iEmpleadosRepository.findByUsuario(usuario);
    }

    public boolean existsById(Integer id) {
        return iEmpleadosRepository.existsById(id);
    }

    public long count() {
        return iEmpleadosRepository.count();
    }

    // Model mapping
    public EmpleadosModel toModel(Empleados empleado) {
        return new EmpleadosModel(empleado.getId(), empleado.getUsuario(), empleado.getContrasena(), empleado.getNombre());
    }

    public Empleados toEntity(EmpleadosModel model) {
        return new Empleados(model.getId(), model.getUsuario(), model.getContrasena(), model.getNombre());
    }
}