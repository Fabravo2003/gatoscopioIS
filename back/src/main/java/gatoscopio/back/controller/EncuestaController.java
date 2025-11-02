package gatoscopio.back.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gatoscopio.back.dto.EncuestaDetalleResponse;
import gatoscopio.back.dto.RespuestaUpsertRequest;
import gatoscopio.back.dto.UpdateEstadoEncuestaRequest;
import gatoscopio.back.service.ServiceEncuesta;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/encuestas")
@Tag(name = "Encuestas")
public class EncuestaController {

    @Autowired
    private ServiceEncuesta service;

    @Operation(summary = "Obtener detalle de encuesta (respuestas)")
    @GetMapping("/{id}")
    public ResponseEntity<EncuestaDetalleResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getDetalle(id));
    }

    @Operation(summary = "Editar respuestas de encuesta incompleta (upsert)")
    @PutMapping("/{id}/respuestas")
    public ResponseEntity<?> upsert(@PathVariable Integer id, @RequestBody @jakarta.validation.Valid List<RespuestaUpsertRequest> items) {
        int n = service.upsertRespuestas(id, items);
        java.util.Map<String,Object> body = new java.util.LinkedHashMap<>();
        body.put("encuestaId", id);
        body.put("actualizadas", n);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Actualizar estado de encuesta")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Integer id, @RequestBody @jakarta.validation.Valid UpdateEstadoEncuestaRequest req) {
        service.updateEstado(id, req.getEstado());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

