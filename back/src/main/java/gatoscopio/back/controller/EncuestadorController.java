package gatoscopio.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import gatoscopio.back.model.Paciente;
import gatoscopio.back.model.Muestra;
import gatoscopio.back.service.impl.ServiceEncuestadorImpl;

@RestController
@RequestMapping("/api")
public class EncuestadorController {
    @Autowired
    private ServiceEncuestadorImpl service;

    @PostMapping("/pacientes")
    public ResponseEntity<?> crearPaciente(@RequestBody Paciente paciente) {
        try {
            service.createPaciente(paciente);
            return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err("conflict", e.getMessage()));
        }
    }

    @GetMapping("/pacientes/{codigo}")
    public ResponseEntity<?> obtenerPaciente(@PathVariable String codigo) {
        try {
            var p = service.getPaciente(codigo);
            return ResponseEntity.ok(p);
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

    @GetMapping("/pacientes")
    public ResponseEntity<?> listarPacientes(@PageableDefault(size = 20, sort = "codigo") Pageable pageable) {
        var page = service.listPacientes(pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/muestras")
    public ResponseEntity<?> crearMuestra(@RequestBody Muestra muestra) {
        try {
            service.createMuestra(muestra);
            return ResponseEntity.status(HttpStatus.CREATED).body(muestra);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err("conflict", e.getMessage()));
        } catch (java.util.NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", e.getMessage()));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err("bad_request", "FK inválida o datos inconsistentes"));
        }
    }
}   
