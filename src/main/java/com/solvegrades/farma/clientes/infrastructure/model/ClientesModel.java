package com.solvegrades.farma.clientes.infrastructure.model;

public class ClientesModel {
    private Integer id;
    private String nombre;
    private String telefono;

    public ClientesModel() {}

    public ClientesModel(Integer id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}