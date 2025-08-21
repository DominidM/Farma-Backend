package com.solvegrades.farma.categorias.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.categorias.application.dto.CategoriasDTO;
import com.solvegrades.farma.categorias.domain.entities.Categorias;
import com.solvegrades.farma.categorias.domain.repositories.ICategoriasRepository;

@Service
@Transactional
public class CategoriasService {

    @Autowired
    private ICategoriasRepository categoriasRepository;

    @Transactional(readOnly = true)
    public List<CategoriasDTO> findAll() {
        return categoriasRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriasDTO findById(Integer id) {
        return categoriasRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<CategoriasDTO> findByNombreContaining(String nombre) {
        return categoriasRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::toDTO)
                .toList();
    }
    
    @Transactional(readOnly = true)
    public long countCategorias() {
        return categoriasRepository.count(); 
    }

    public CategoriasDTO save(CategoriasDTO dto) {
        validateCategoria(dto);
        
        // Verificar si ya existe una categoría con el mismo nombre
        Optional<Categorias> existingCategoria = categoriasRepository.findByNombre(dto.getNombre());
        if (existingCategoria.isPresent()) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + dto.getNombre());
        }

        Categorias categoria = toEntity(dto);
        Categorias saved = categoriasRepository.save(categoria);
        return toDTO(saved);
    }

    public CategoriasDTO update(Integer id, CategoriasDTO dto) {
        validateCategoria(dto);
        
        Categorias existingCategoria = categoriasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
        
        // Verificar si ya existe otra categoría con el mismo nombre
        Optional<Categorias> categoriaConMismoNombre = categoriasRepository.findByNombre(dto.getNombre());
        if (categoriaConMismoNombre.isPresent() && !categoriaConMismoNombre.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + dto.getNombre());
        }

        existingCategoria.setNombre(dto.getNombre());
        Categorias updated = categoriasRepository.save(existingCategoria);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!categoriasRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con id: " + id);
        }
        categoriasRepository.deleteById(id);
    }

    private void validateCategoria(CategoriasDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        if (dto.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre de la categoría no puede exceder 100 caracteres");
        }
    }

    private CategoriasDTO toDTO(Categorias categoria) {
        return new CategoriasDTO(categoria.getId(), categoria.getNombre());
    }

    private Categorias toEntity(CategoriasDTO dto) {
        Categorias categorias = new Categorias();
        categorias.setId(dto.getId());
        categorias.setNombre(dto.getNombre() != null ? dto.getNombre().trim() : null);
        return categorias;
    }
}