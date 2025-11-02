package gatoscopio.back.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gatoscopio.back.model.Paciente;
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

    private static java.util.Map<String, Object> err(String code, String message) {
        java.util.Map<String, Object> m = new java.util.HashMap<>();
        m.put("code", code);
        m.put("message", message);
        return m;
    }
}   
