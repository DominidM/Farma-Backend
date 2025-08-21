package com.solvegrades.farma.ventas.infrastructure.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentasModel {
    private Integer id;
    private LocalDateTime fecha;
    private Integer idCliente;
    private Integer idEmpleado;
    private BigDecimal total;

    public VentasModel() {}

    public VentasModel(Integer id, LocalDateTime fecha, Integer idCliente, Integer idEmpleado, BigDecimal total) {
        this.id = id;
        this.fecha = fecha;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
        this.total = total;
    }

    // Getters y Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
    public Integer getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Integer idEmpleado) { this.idEmpleado = idEmpleado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}