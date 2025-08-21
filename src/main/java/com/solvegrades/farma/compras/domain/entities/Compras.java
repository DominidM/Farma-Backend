package com.solvegrades.farma.compras.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.solvegrades.farma.proveedores.domain.entities.Proveedores;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "compras")
public class Compras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedores proveedor;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    public Compras() {}

    public Compras(Integer id, LocalDateTime fecha, Proveedores proveedor, BigDecimal total) {
        this.id = id;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.total = total;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Proveedores getProveedor() { return proveedor; }
    public void setProveedor(Proveedores proveedor) { this.proveedor = proveedor; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}