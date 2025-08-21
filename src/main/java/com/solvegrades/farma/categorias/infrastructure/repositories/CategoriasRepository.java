package com.solvegrades.farma.categorias.infrastructure.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.solvegrades.farma.categorias.domain.entities.Categorias;
import com.solvegrades.farma.categorias.domain.repositories.ICategoriasRepository;
import com.solvegrades.farma.categorias.infrastructure.model.CategoriasModel;

@Component
public class CategoriasRepository {

    @Autowired
    private ICategoriasRepository iCategoriasRepository;

    public List<Categorias> findAll() {
        return iCategoriasRepository.findAll();
    }

    public Optional<Categorias> findById(Integer id) {
        return iCategoriasRepository.findById(id);
    }

    public Categorias save(Categorias categoria) {
        return iCategoriasRepository.save(categoria);
    }

    public void deleteById(Integer id) {
        iCategoriasRepository.deleteById(id);
    }

    public List<Categorias> findByNombreContaining(String nombre) {
        return iCategoriasRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Optional<Categorias> findByNombre(String nombre) {
        return iCategoriasRepository.findByNombre(nombre);
    }

    public boolean existsById(Integer id) {
        return iCategoriasRepository.existsById(id);
    }

    public long count() {
        return iCategoriasRepository.count();
    }

    // Método para convertir de Entity a Model
    public CategoriasModel toModel(Categorias categoria) {
        return new CategoriasModel(categoria.getId(), categoria.getNombre());
    }

    // Método para convertir de Model a Entity
    public Categorias toEntity(CategoriasModel model) {
        return new Categorias(model.getId(), model.getNombre());
    }
}