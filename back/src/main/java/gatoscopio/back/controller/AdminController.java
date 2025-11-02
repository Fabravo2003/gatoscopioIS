package gatoscopio.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gatoscopio.back.dto.RolesUpdateRequest;
import gatoscopio.back.service.ServiceAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
    public ResponseEntity<?> setUserRoles(@PathVariable Integer id, @RequestBody RolesUpdateRequest req) {
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

    private static java.util.Map<String, Object> err(String code, String message) {
        java.util.Map<String, Object> m = new java.util.HashMap<>();
        m.put("code", code);
        m.put("message", message);
        return m;
    }
}
