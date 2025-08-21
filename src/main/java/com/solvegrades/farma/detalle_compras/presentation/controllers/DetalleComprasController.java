package com.solvegrades.farma.detalle_compras.presentation.controllers;

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

import com.solvegrades.farma.detalle_compras.application.dto.DetalleComprasDTO;
import com.solvegrades.farma.detalle_compras.application.services.DetalleComprasService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/detalle_compras")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "DetalleCompras", description = "API para gestión de detalle de compras")
public class DetalleComprasController {

    @Autowired
    private DetalleComprasService detalleComprasService;

    @Operation(summary = "Obtener todos los detalles de compras")
    @GetMapping
    public ResponseEntity<List<DetalleComprasDTO>> getAll() {
        try {
            List<DetalleComprasDTO> detalles = detalleComprasService.findAll();
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener detalle de compra por ID")
    @GetMapping("/{id}")
    public ResponseEntity<DetalleComprasDTO> getById(
            @Parameter(description = "ID del detalle", required = true)
            @PathVariable Integer id) {
        try {
            DetalleComprasDTO detalle = detalleComprasService.findById(id);
            return ResponseEntity.ok(detalle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener detalles por compra")
    @GetMapping("/por_compra/{idCompra}")
    public ResponseEntity<List<DetalleComprasDTO>> getByCompraId(
            @Parameter(description = "ID de la compra", required = true)
            @PathVariable Integer idCompra) {
        try {
            List<DetalleComprasDTO> detalles = detalleComprasService.findByCompraId(idCompra);
            return ResponseEntity.ok(detalles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Conteo de detalles de compras")
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        try {
            long count = detalleComprasService.countDetalleCompras();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear detalle de compra")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DetalleComprasDTO dto) {
        try {
            dto.setId(null);
            DetalleComprasDTO created = detalleComprasService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validación", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    @Operation(summary = "Actualizar detalle de compra")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID del detalle", required = true)
            @PathVariable Integer id,
            @RequestBody DetalleComprasDTO dto) {
        try {
            DetalleComprasDTO updated = detalleComprasService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validación", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    @Operation(summary = "Eliminar detalle de compra")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del detalle", required = true)
            @PathVariable Integer id) {
        try {
            detalleComprasService.delete(id);
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