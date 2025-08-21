package com.solvegrades.farma.ventas.application.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solvegrades.farma.clientes.domain.entities.Clientes;
import com.solvegrades.farma.clientes.domain.repositories.IClientesRepository;
import com.solvegrades.farma.empleados.domain.entities.Empleados;
import com.solvegrades.farma.empleados.domain.repositories.IEmpleadosRepository;
import com.solvegrades.farma.ventas.application.dto.VentasDTO;
import com.solvegrades.farma.ventas.domain.entities.Ventas;
import com.solvegrades.farma.ventas.domain.repositories.IVentasRepository;

@Service
@Transactional
public class VentasService {

    @Autowired
    private IVentasRepository ventasRepository;

    @Autowired
    private IClientesRepository clientesRepository;

    @Autowired
    private IEmpleadosRepository empleadosRepository;

    @Transactional(readOnly = true)
    public List<VentasDTO> findAll() {
        return ventasRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public VentasDTO findById(Integer id) {
        return ventasRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
    }

    @Transactional(readOnly = true)
    public List<VentasDTO> findByFechaBetween(LocalDateTime desde, LocalDateTime hasta) {
        return ventasRepository.findByFechaBetween(desde, hasta)
                .stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public long countVentas() {
        return ventasRepository.count();
    }

    public VentasDTO save(VentasDTO dto) {
        validateVenta(dto);
        Ventas venta = toEntity(dto);
        venta.setFecha(LocalDateTime.now());
        Ventas saved = ventasRepository.save(venta);
        return toDTO(saved);
    }

    public VentasDTO update(Integer id, VentasDTO dto) {
        validateVenta(dto);
        Ventas existing = ventasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));

        if (dto.getIdCliente() != null) {
            Clientes cliente = clientesRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + dto.getIdCliente()));
            existing.setCliente(cliente);
        }
        if (dto.getIdEmpleado() != null) {
            Empleados empleado = empleadosRepository.findById(dto.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + dto.getIdEmpleado()));
            existing.setEmpleado(empleado);
        }
        if (dto.getTotal() != null) {
            existing.setTotal(dto.getTotal());
        }
        Ventas updated = ventasRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Integer id) {
        if (!ventasRepository.existsById(id)) {
            throw new RuntimeException("Venta no encontrada con id: " + id);
        }
        ventasRepository.deleteById(id);
    }

    private void validateVenta(VentasDTO dto) {
        if (dto.getIdCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio.");
        }
        if (dto.getIdEmpleado() == null) {
            throw new IllegalArgumentException("El empleado es obligatorio.");
        }
        if (dto.getTotal() == null || dto.getTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El total debe ser un valor positivo.");
        }
    }

    private VentasDTO toDTO(Ventas v) {
        VentasDTO dto = new VentasDTO();
        dto.setId(v.getId());
        dto.setFecha(v.getFecha());
        if (v.getCliente() != null) {
            dto.setIdCliente(v.getCliente().getId());
            dto.setNombreCliente(v.getCliente().getNombre());
        }
        if (v.getEmpleado() != null) {
            dto.setIdEmpleado(v.getEmpleado().getId());
            dto.setNombreEmpleado(v.getEmpleado().getNombre());
        }
        dto.setTotal(v.getTotal());
        return dto;
    }

    private Ventas toEntity(VentasDTO dto) {
        Ventas v = new Ventas();
        v.setId(dto.getId());
        v.setFecha(dto.getFecha());
        if (dto.getIdCliente() != null) {
            Clientes cliente = clientesRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + dto.getIdCliente()));
            v.setCliente(cliente);
        }
        if (dto.getIdEmpleado() != null) {
            Empleados empleado = empleadosRepository.findById(dto.getIdEmpleado())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado con id: " + dto.getIdEmpleado()));
            v.setEmpleado(empleado);
        }
        v.setTotal(dto.getTotal());
        return v;
    }
}