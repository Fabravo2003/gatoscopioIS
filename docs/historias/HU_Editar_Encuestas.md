# HU: Editar encuestas

Estado: Completada (backend-only)

## Objetivo
Como administrador, editar encuestas incompletas para corregir errores.

## Endpoints
- `GET /api/encuestas/{id}` → 200 detalle con respuestas
- `PUT /api/encuestas/{id}/respuestas` → 200 upsert de respuestas
  - Body: `[{ "preguntaId": 1, "valor": "34" }, ...]`
  - Reglas: solo si `estado != 'Completo'`; valida pregunta, tipo_dato y valores válidos (si existen).
- `PUT /api/encuestas/{id}/estado` → 204 cambia estado (`Incompleto`/`Completo`/otros definidos)

## Validaciones
- Encuesta debe existir.
- No permite editar si `estado` es `Completo` → 409.
- Cada `preguntaId` debe existir en `preguntas`.
- Si existen valores válidos en `valor_valido_pregunta`, el `valor` debe pertenecer a ese conjunto.
- Validación simple por `tipo_dato`: `numero` (BigDecimal), `fecha` (ISO yyyy-MM-dd), `bool`/`boolean` (true/false/si/no/0/1).

## Archivos clave
- Controlador: `back/src/main/java/gatoscopio/back/controller/EncuestaController.java`
- Servicio: `back/src/main/java/gatoscopio/back/service/impl/ServiceEncuestaImpl.java`
- Repos: `EncuestaRepository`, `RespuestaRepository`, `PreguntaRepository`, `ValorValidoPreguntaRepository`
- DTOs: `RespuestaUpsertRequest`, `EncuestaDetalleResponse`, `UpdateEstadoEncuestaRequest`
- Modelos: `Encuesta`, `Respuesta`, `Pregunta`, `ValorValidoPregunta`

## Pruebas rápidas
```
# Detalle
curl http://localhost:8080/api/encuestas/1

# Editar respuestas
curl -X PUT http://localhost:8080/api/encuestas/1/respuestas \
  -H "Content-Type: application/json" \
  -d '[{"preguntaId":1,"valor":"34"},{"preguntaId":2,"valor":"si"}]'

# Cambiar estado
curl -X PUT http://localhost:8080/api/encuestas/1/estado \
  -H "Content-Type: application/json" \
  -d '{"estado":"Completo"}' -i
```

