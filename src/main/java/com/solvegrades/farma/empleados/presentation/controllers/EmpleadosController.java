package com.solvegrades.farma.empleados.presentation.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.solvegrades.farma.empleados.application.dto.EmpleadosDTO;
import com.solvegrades.farma.empleados.application.services.EmpleadosService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/empleados")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Empleados", description = "API para gestión de empleados")
public class EmpleadosController {

    @Autowired
    private EmpleadosService empleadosService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Operation(summary = "Obtener todos los empleados")
    @GetMapping
    public ResponseEntity<List<EmpleadosDTO>> getAll() {
        try {
            List<EmpleadosDTO> empleados = empleadosService.findAll();
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener empleado por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadosDTO> getById(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Integer id) {
        try {
            EmpleadosDTO empleado = empleadosService.findById(id);
            return ResponseEntity.ok(empleado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar empleados por nombre")
    @GetMapping("/buscar")
    public ResponseEntity<List<EmpleadosDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre", required = true)
            @RequestParam String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            List<EmpleadosDTO> empleados = empleadosService.findByNombreContaining(nombre);
            return ResponseEntity.ok(empleados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener conteo de empleados")
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        try {
            long count = empleadosService.countEmpleados();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Crear nuevo empleado")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody EmpleadosDTO dto) {
        try {
            dto.setId(null);
            // Hashea la contraseña antes de guardar
            if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
                dto.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            }
            EmpleadosDTO created = empleadosService.save(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validación", e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Ya existe")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Conflicto", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    @Operation(summary = "Actualizar empleado")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Integer id,
            @RequestBody EmpleadosDTO dto) {
        try {
            // Si viene contraseña, la hashea
            if (dto.getContrasena() != null && !dto.getContrasena().isEmpty()) {
                dto.setContrasena(passwordEncoder.encode(dto.getContrasena()));
            }
            EmpleadosDTO updated = empleadosService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Validación", e.getMessage()));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("Ya existe")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Conflicto", e.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    @Operation(summary = "Eliminar empleado")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @Parameter(description = "ID del empleado", required = true)
            @PathVariable Integer id) {
        try {
            empleadosService.delete(id);
            return ResponseEntity.ok(new SuccessResponse("Eliminación exitosa", "Empleado eliminado correctamente"));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Error interno", "Ha ocurrido un error en el servidor"));
        }
    }

    // --- LOGIN ENDPOINT ---
    @Operation(summary = "Login de empleado")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Buscar empleado por usuario (correo)
            EmpleadosDTO empleado = empleadosService.findByUsuario(loginRequest.getUsuario());
            if (empleado == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciales", "Usuario o contraseña incorrectos"));
            }

            // Validar contraseña usando bcrypt
            boolean passwordMatch = passwordEncoder.matches(loginRequest.getContrasena(), empleado.getContrasena());
            if (!passwordMatch) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Credenciales", "Usuario o contraseña incorrectos"));
            }

            // Aquí podrías generar un JWT, pero por ahora solo regresa los datos del usuario
            return ResponseEntity.ok(new LoginResponse(null, empleado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Error interno", "Ha ocurrido un error inesperado en el servidor"));
        }
    }

    // --- DTOs internos para login y respuestas ---
    public static class LoginRequest {
        private String usuario;
        private String contrasena;

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }
        public String getContrasena() { return contrasena; }
        public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    }

    public static class LoginResponse {
        private String token;
        private EmpleadosDTO user;

        public LoginResponse(String token, EmpleadosDTO user) {
            this.token = token;
            this.user = user;
        }
        public String getToken() { return token; }
        public EmpleadosDTO getUser() { return user; }
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