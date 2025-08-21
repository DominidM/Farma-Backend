package com.solvegrades.farma.compras.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ComprasDTO {
    private Integer id;
    private LocalDateTime fecha;
    private Integer idProveedor;
    private String nombreProveedor;
    private BigDecimal total;

    public ComprasDTO() {}

    // Getters y Setters

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Integer getIdProveedor() { return idProveedor; }
    public void setIdProveedor(Integer idProveedor) { this.idProveedor = idProveedor; }

    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}