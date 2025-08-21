package com.solvegrades.farma.detalle_ventas.presentation.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.solvegrades.farma.detalle_ventas.application.dto.DetalleVentasDTO;
import com.solvegrades.farma.detalle_ventas.application.services.DetalleVentasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/detalle_ventas")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "DetalleVentas", description = "API para gestión de detalle de ventas")
public class DetalleVentasController {

    @Autowired
    private DetalleVentasService detalleVentasService;

    @Operation(summary = "Obtener todos los detalles de ventas")
    @GetMapping
    public ResponseEntity<List<DetalleVentasDTO>> getAll() {
        try {
            List<DetalleVentasDTO> detalles = detalleVentasService.findAll();
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener detalle de venta por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVentasDTO> getById(
            @Parameter(description = "ID del detalle", required = true)
            @PathVariable Integer id) {
        try {
            DetalleVentasDTO detalle = detalleVentasService.findById(id);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener detalles por venta")
    @GetMapping("/por_venta/{idVenta}")
    public ResponseEntity<List<DetalleVentasDTO>> getByVentaId(
            @Parameter(description = "ID de la venta", required = true)
            @PathVariable Integer idVenta) {
        try {
            List<DetalleVentasDTO> detalles = detalleVentasService.findByVentaId(idVenta);
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Conteo de detalles de ventas")
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        try {
            long count = detalleVentasService.countDetalleVentas();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear detalle de venta")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DetalleVentasDTO dto) {
        try {
            dto.setId(null);
            DetalleVentasDTO created = detalleVentasService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validación", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    @Operation(summary = "Actualizar detalle de venta")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID del detalle", required = true)
            @PathVariable Integer id,
            @RequestBody DetalleVentasDTO dto) {
        try {
            DetalleVentasDTO updated = detalleVentasService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validación", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    @Operation(summary = "Eliminar detalle de venta")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del detalle", required = true)
            @PathVariable Integer id) {
        try {
            detalleVentasService.delete(id);
            return ResponseEntity.ok(new SuccessResponse("Eliminación exitosa", "Detalle eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    public static class ErrorResponse {
        private String tipo;
        private String mensaje;

        public ErrorResponse(String tipo, String mensaje) {
            this.tipo = tipo;
            this.mensaje = mensaje;
        }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }

    public static class SuccessResponse {
        private String tipo;
        private String mensaje;

        public SuccessResponse(String tipo, String mensaje) {
            this.tipo = tipo;
            this.mensaje = mensaje;
        }
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}