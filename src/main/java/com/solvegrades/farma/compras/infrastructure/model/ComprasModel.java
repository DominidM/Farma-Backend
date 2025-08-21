package com.solvegrades.farma.compras.infrastructure.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComprasModel {
    private Integer id;
    private LocalDateTime fecha;
    private Integer idProveedor;
    private BigDecimal total;

    public ComprasModel() {}

    public ComprasModel(Integer id, LocalDateTime fecha, Integer idProveedor, BigDecimal total) {
        this.id = id;
        this.fecha = fecha;
        this.idProveedor = idProveedor;
        this.total = total;
    }

    // Getters y Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}