# HU: Registrar una nueva muestra

Estado: Completada (backend-only)

## Objetivo
Como investigador, registrar una nueva muestra con metadatos (paciente, tipo, volumen y observación/ubicación) para organizar el inventario del laboratorio.

## Endpoints

1) Crear muestra
- `POST /api/muestras`
- Request JSON:
  ```json
  {
    "codigo": "MX-001",
    "tipoMuestraId": 1,
    "pacienteCodigo": "PAC001",
    "cantidadDonada": 10.0,
    "observacion": "Ubicación: Freezer A - Caja 2"
  }
  ```
- Respuestas:
  - 201 Created: cuerpo = muestra creada
  - 400 Bad Request: validaciones/FK inválida (`paciente no existe`, `tipoMuestraId es requerido`, etc.)
  - 409 Conflict: `muestra ya existe`

Notas:
- `pacienteCodigo` es opcional según el modelo (puede ser `null`). Si se envía, debe existir el paciente.
- El campo de “ubicación física” se refleja en `observacion`.

## Validaciones de negocio
- `codigo`: requerido, no vacío, único.
- `tipoMuestraId`: requerido.
- `pacienteCodigo`: si se especifica, debe existir en `pacientes`.
- `cantidadDonada`: opcional; si se usa, no negativa (validación futura, ver siguientes pasos).

## Integración con la BD
- Tabla: `muestras(codigo PK, tipo_muestra_id, paciente_codigo, observacion, created_at DEFAULT now(), cantidad_donada)`.
- FK: `paciente_codigo -> pacientes(codigo)` validada en servicio; `tipo_muestra_id -> tipos_muestra(id)` validada por la base (FK) y/o por validación futura.

## Archivos clave tocados
- `back/src/main/java/gatoscopio/back/model/Muestra.java` (añadidos campos `tipoMuestraId`, `pacienteCodigo` y `setCodigo`).
- `back/src/main/java/gatoscopio/back/repository/MuestraRepository.java` (nuevo `JpaRepository`).
- `back/src/main/java/gatoscopio/back/service/ServiceEncuestador.java` (método `createMuestra`).
- `back/src/main/java/gatoscopio/back/service/impl/ServiceEncuestadorImpl.java` (validaciones y persistencia de muestra).
- `back/src/main/java/gatoscopio/back/controller/EncuestadorController.java` (endpoint `POST /api/muestras`).

## Cómo levantar y probar
1) DB
```
docker compose up -d db
PGPASSWORD=demopassword psql -h localhost -p 5432 -U demo -d demo_db -c "SELECT 1"
```
2) Backend
```
cd back
./mvnw spring-boot:run
```
3) Crear muestra
```
curl -X POST http://localhost:8080/api/muestras \
  -H "Content-Type: application/json" \
  -d '{
        "codigo":"MX-ABC",
        "tipoMuestraId":1,
        "pacienteCodigo":"PAC001",
        "cantidadDonada":5.5,
        "observacion":"Ubicación: Freezer A - Caja 2"
      }'
```
4) Verificar en SQL
```
PGPASSWORD=demopassword psql -h localhost -p 5432 -U demo -d demo_db \
  -c "SELECT codigo, tipo_muestra_id, paciente_codigo, cantidad_donada, observacion FROM muestras WHERE codigo='MX-ABC';"
```

## Siguientes pasos (opcional)
- Validar existencia de `tipo_muestra_id` antes de insertar (crear entidad/repo `TipoMuestra`).
- Normalizar campo de ubicación (columna específica en `muestras` si se requiere a futuro).
- Endpoint para listar muestras y ver stock por tipo.
- Registrar operaciones (`operaciones_muestra`) al momento de ingreso.

