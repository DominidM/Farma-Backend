package com.solvegrades.farma.empleados.infrastructure.model;

public class EmpleadosModel {
    private Integer id;
    private String usuario;
    private String contrasena;
    private String nombre;

    public EmpleadosModel() {}

    public EmpleadosModel(Integer id, String usuario, String contrasena, String nombre) {
        this.id = id;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.nombre = nombre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}