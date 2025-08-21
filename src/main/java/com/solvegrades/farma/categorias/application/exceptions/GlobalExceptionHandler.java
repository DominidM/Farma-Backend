package com.solvegrades.farma.categorias.application.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            "Validación",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            "Error de aplicación",
            ex.getMessage(),
            LocalDateTime.now(),
            request.getDescription(false)
        );
        
        if (ex.getMessage().contains("no encontrada")) {
            return ResponseEntity.notFound().build();
        } else if (ex.getMessage().contains("Ya existe")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
            "Error interno",
            "Ha ocurrido un error inesperado en el servidor",
            LocalDateTime.now(),
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    public static class ErrorResponse {
        private String tipo;
        private String mensaje;
        private LocalDateTime timestamp;
        private String path;

        public ErrorResponse(String tipo, String mensaje, LocalDateTime timestamp, String path) {
            this.tipo = tipo;
            this.mensaje = mensaje;
            this.timestamp = timestamp;
            this.path = path;
        }

        // Getters y setters
        public String getTipo() { return tipo; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        public String getPath() { return path; }
        public void setPath(String path) { this.path = path; }
    }
}