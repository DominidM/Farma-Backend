package com.solvegrades.farma.clientes.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.clientes.application.dto.ClientesDTO;
import com.solvegrades.farma.clientes.domain.entities.Clientes;
import com.solvegrades.farma.clientes.domain.repositories.IClientesRepository;

@Service
@Transactional
public class ClientesService {

    @Autowired
    private IClientesRepository clientesRepository;

    @Transactional(readOnly = true)
    public List<ClientesDTO> findAll() {
        return clientesRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ClientesDTO findById(Integer id) {
        return clientesRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ClientesDTO> findByNombreContaining(String nombre) {
        return clientesRepository.findByNombreContainingIgnoreCase(nombre)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countClientes() {
        return clientesRepository.count();
    }

    public ClientesDTO save(ClientesDTO dto) {
        validateCliente(dto);
        Optional<Clientes> existing = clientesRepository.findByNombre(dto.getNombre());
        if (existing.isPresent()) {
            throw new RuntimeException("Ya existe un cliente con el nombre: " + dto.getNombre());
        }
        Clientes cliente = toEntity(dto);
        Clientes saved = clientesRepository.save(cliente);
        return toDTO(saved);
    }

    public ClientesDTO update(Integer id, ClientesDTO dto) {
        validateCliente(dto);
        Clientes existing = clientesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
        Optional<Clientes> clienteConMismoNombre = clientesRepository.findByNombre(dto.getNombre());
        if (clienteConMismoNombre.isPresent() && !clienteConMismoNombre.get().getId().equals(id)) {
            throw new RuntimeException("Ya existe otro cliente con el nombre: " + dto.getNombre());
        }
        existing.setNombre(dto.getNombre());
        existing.setTelefono(dto.getTelefono());
        Clientes updated = clientesRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!clientesRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
        clientesRepository.deleteById(id);
    }

    private void validateCliente(ClientesDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio");
        }
        if (dto.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre del cliente no puede exceder 100 caracteres");
        }
        if (dto.getTelefono() != null && dto.getTelefono().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }
    }

    private ClientesDTO toDTO(Clientes cliente) {
        return new ClientesDTO(cliente.getId(), cliente.getNombre(), cliente.getTelefono());
    }

    private Clientes toEntity(ClientesDTO dto) {
        Clientes cliente = new Clientes();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre() != null ? dto.getNombre().trim() : null);
        cliente.setTelefono(dto.getTelefono() != null ? dto.getTelefono().trim() : null);
        return cliente;
    }
}