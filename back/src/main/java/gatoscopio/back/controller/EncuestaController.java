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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/encuestas")
@Tag(name = "Encuestas")
public class EncuestaController {

    @Autowired
    private ServiceEncuesta service;

    @Operation(summary = "Obtener detalle de encuesta (respuestas + metadatos)")
    @GetMapping("/{id}")
    public ResponseEntity<EncuestaDetalleResponse> get(@PathVariable Integer id) {
        return ResponseEntity.ok(service.getDetalle(id));
    }

    @Operation(summary = "Editar respuestas de encuesta incompleta (upsert)")
    @PutMapping("/{id}/respuestas")
    public ResponseEntity<?> upsert(@PathVariable Integer id,
                                    @RequestBody(
                                        description = "Lista de respuestas a crear/actualizar",
                                        required = true,
                                        content = @Content(mediaType = "application/json",
                                            examples = @ExampleObject(value = "[ {\\n  \\\"preguntaId\\\": 1, \\\"valor\\\": \\\"34\\\" }, { \\\"preguntaId\\\": 2, \\\"valor\\\": \\\"si\\\" } ]"))
                                    ) @jakarta.validation.Valid List<RespuestaUpsertRequest> items,
                                    @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
                                    @RequestParam(value = "userId", required = false) Integer queryUserId) {
        Integer userId = headerUserId != null ? headerUserId : queryUserId;
        int n = service.upsertRespuestas(id, items, userId);
        java.util.Map<String,Object> body = new java.util.LinkedHashMap<>();
        body.put("encuestaId", id);
        body.put("actualizadas", n);
        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Actualizar estado de encuesta")
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Integer id,
                                          @RequestBody(
                                            description = "Nuevo estado de la encuesta",
                                            required = true,
                                            content = @Content(mediaType = "application/json",
                                                examples = @ExampleObject(value = "{ \\\"estado\\\": \\\"Completo\\\" }"))
                                          ) @jakarta.validation.Valid UpdateEstadoEncuestaRequest req,
                                          @RequestHeader(value = "X-User-Id", required = false) Integer headerUserId,
                                          @RequestParam(value = "userId", required = false) Integer queryUserId) {
        Integer userId = headerUserId != null ? headerUserId : queryUserId;
        service.updateEstado(id, req.getEstado(), userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Listar encuestas (filtros opcionales: estado, paciente)")
    @GetMapping
    public ResponseEntity<?> list(@RequestParam(required = false) String estado,
                                  @RequestParam(name = "paciente", required = false) String pacienteCodigo,
                                  @org.springframework.data.web.PageableDefault(size = 20, sort = "id") org.springframework.data.domain.Pageable pageable) {
        return ResponseEntity.ok(service.list(estado, pacienteCodigo, pageable));
    }
}
