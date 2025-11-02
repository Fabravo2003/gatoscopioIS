package gatoscopio.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gatoscopio.back.dto.RolesUpdateRequest;
import gatoscopio.back.dto.CreateUserRequest;
import gatoscopio.back.dto.UpdateUserRequest;
import gatoscopio.back.dto.UpdatePasswordRequest;
import gatoscopio.back.dto.CreateRoleRequest;
import gatoscopio.back.service.ServiceAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api")
@Tag(name = "Roles")
public class AdminController {
    @Autowired
    private ServiceAdmin service;

    @Operation(summary = "Listar roles disponibles")
    @GetMapping("/roles")
    public ResponseEntity<?> listRoles() {
        return ResponseEntity.ok(service.listRoles());
    }

    @Operation(summary = "Crear nuevo rol")
    @PostMapping("/roles")
    public ResponseEntity<?> createRole(@RequestBody @jakarta.validation.Valid CreateRoleRequest req) {
        try {
            var r = service.createRole(req.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("nombre", r.getNombre()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err("conflict", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar rol (si no está asignado)")
    @DeleteMapping("/roles/{nombre}")
    public ResponseEntity<?> deleteRole(@PathVariable String nombre) {
        try {
            service.deleteRole(nombre);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err("conflict", e.getMessage()));
        }
    }

    @Operation(summary = "Obtener roles de un usuario")
    @GetMapping("/usuarios/{id}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(service.getUserRoles(id));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        }
    }

    @Operation(summary = "Asignar conjunto de roles a un usuario (reemplaza)")
    @PutMapping("/usuarios/{id}/roles")
    public ResponseEntity<?> setUserRoles(@PathVariable Integer id, @RequestBody @jakarta.validation.Valid RolesUpdateRequest req) {
        try {
            if (req == null || req.getRoles() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", "roles requeridos"));
            }
            var u = service.setUserRoles(id, req.getRoles());
            java.util.Map<String,Object> body = new java.util.LinkedHashMap<>();
            body.put("id", u.getId());
            body.put("nombre", u.getNombre());
            body.put("correo", u.getCorreo());
            body.put("roles", service.getUserRoles(u.getId()));
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        }
    }

    @Operation(summary = "Crear usuario con roles iniciales")
    @PostMapping("/usuarios")
    public ResponseEntity<?> createUser(@RequestBody @jakarta.validation.Valid CreateUserRequest req) {
        try {
            var u = service.createUser(req);
            java.util.Map<String,Object> body = new java.util.LinkedHashMap<>();
            body.put("id", u.getId());
            body.put("nombre", u.getNombre());
            body.put("correo", u.getCorreo());
            body.put("roles", service.getUserRoles(u.getId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err("conflict", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        }
    }

    @Operation(summary = "Actualizar datos básicos del usuario (nombre/correo)")
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @jakarta.validation.Valid UpdateUserRequest req) {
        try {
            var u = service.updateUser(id, req);
            java.util.Map<String,Object> body = new java.util.LinkedHashMap<>();
            body.put("id", u.getId());
            body.put("nombre", u.getNombre());
            body.put("correo", u.getCorreo());
            body.put("roles", service.getUserRoles(u.getId()));
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err("conflict", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        }
    }

    @Operation(summary = "Cambiar contraseña del usuario")
    @PutMapping("/usuarios/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Integer id, @RequestBody @jakarta.validation.Valid UpdatePasswordRequest req) {
        try {
            service.changePassword(id, req);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        }
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            service.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        }
    }

    @Operation(summary = "Listar usuarios (paginado) con sus roles")
    @GetMapping("/usuarios")
    public ResponseEntity<?> listUsers(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.pageUsers(pageable));
    }

    @Operation(summary = "Listar usuarios por rol (paginado)")
    @GetMapping("/roles/{nombre}/usuarios")
    public ResponseEntity<?> listUsersByRole(@PathVariable String nombre, @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        try {
            return ResponseEntity.ok(service.usersByRole(nombre, pageable));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err("not_found", e.getMessage()));
        }
    }

    private static java.util.Map<String, Object> err(String code, String message) {
        java.util.Map<String, Object> m = new java.util.HashMap<>();
        m.put("code", code);
        m.put("message", message);
        return m;
    }
}
