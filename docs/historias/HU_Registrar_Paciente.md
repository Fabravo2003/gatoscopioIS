# HU: Registrar un nuevo paciente

Estado: Completada (backend-only)

## Objetivo
Como encuestador, registrar un nuevo paciente con un identificador único para evitar duplicados y asegurar trazabilidad.

## Alcance y decisiones
- Alcance: API REST backend (sin UI). Persistencia en Postgres.
- Identificador de paciente: `codigo` (PK en tabla `pacientes`).
- Validación de dominio: `casoControl` debe ser "Caso" o "Control".
- Manejo de errores: respuestas JSON con `{ code, message }` para 4xx.
- CORS de desarrollo habilitado para `/api/**` (orígenes configurables por propiedad `app.cors.allowed-origins`).

## Endpoints

1) Crear paciente
- `POST /api/pacientes`
- Request JSON:
  ```json
  {
    "codigo": "PAC200",
    "nombre": "Ana",
    "correo": "ana@example.com",
    "telefono": "+56911111111",
    "casoControl": "Caso"
  }
  ```
- Respuestas:
  - 201 Created: cuerpo = paciente creado
  - 400 Bad Request: `{ "code": "bad_request", "message": "..." }`
  - 409 Conflict (duplicado): `{ "code": "conflict", "message": "paciente ya existe" }`

2) Obtener paciente por código
- `GET /api/pacientes/{codigo}`
- Respuestas:
  - 200 OK: cuerpo = paciente
  - 400 Bad Request (código vacío)
  - 404 Not Found (no existe)

3) Listar pacientes (paginado)
- `GET /api/pacientes?page=0&size=20&sort=codigo,asc`
- Respuesta 200 con objeto `Page` (Spring Data) serializado en JSON.

## Validaciones
- `codigo`: requerido, no vacío.
- `casoControl`: requerido, solo "Caso" o "Control" (case-insensitive).
- Duplicados: conflicto si existe un paciente con el mismo `codigo`.

## Manejo de CORS (desarrollo)
- Configuración en `gatoscopio.back.config.WebCorsConfig`.
- Propiedad: `app.cors.allowed-origins` (por defecto `*`).
- Ejemplo de ejecución con origen específico:
  `-Dapp.cors.allowed-origins=http://localhost:5173`

## Cómo levantar y probar

1) Base de datos (Docker Compose)
```
docker compose up -d db
docker compose logs -f db
PGPASSWORD=demopassword psql -h localhost -p 5432 -U demo -d demo_db -c "SELECT 1"
```

2) Backend (local, puerto 8080)
```
cd back
./mvnw spring-boot:run
```

3) Pruebas rápidas
```
# Crear
curl -X POST http://localhost:8080/api/pacientes \
  -H "Content-Type: application/json" \
  -d '{"codigo":"PAC300","nombre":"Ana","casoControl":"Caso"}'

# Obtener por código
curl http://localhost:8080/api/pacientes/PAC300

# Listar (paginado)
curl "http://localhost:8080/api/pacientes?page=0&size=10&sort=codigo,asc"

# Verificar en SQL
PGPASSWORD=demopassword psql -h localhost -p 5432 -U demo -d demo_db \
  -c "SELECT codigo,nombre,caso_control FROM pacientes WHERE codigo='PAC300';"
```

## Archivos clave tocados
- `back/src/main/java/gatoscopio/back/model/Paciente.java`
  - Agregados setters `setCodigo(String)` y `setCasoControl(String)`.
- `back/src/main/java/gatoscopio/back/repository/PacienteRepository.java`
  - Nuevo repositorio `JpaRepository<Paciente, String>`.
- `back/src/main/java/gatoscopio/back/service/ServiceEncuestador.java`
  - Métodos: `createPaciente`, `getPaciente`, `listPacientes(Pageable)`.
- `back/src/main/java/gatoscopio/back/service/impl/ServiceEncuestadorImpl.java`
  - Validaciones, verificación de duplicados y persistencia.
  - Implementación de `getPaciente` y `listPacientes`.
- `back/src/main/java/gatoscopio/back/controller/EncuestadorController.java`
  - Endpoints `POST /api/pacientes`, `GET /api/pacientes/{codigo}`, `GET /api/pacientes`.
- `back/src/main/java/gatoscopio/back/config/WebCorsConfig.java`
  - CORS para `/api/**` con orígenes configurables.

## Notas y decisiones
- `application.properties` usa `jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:demo_db}`. En local, funciona sin variables; en Docker, se puede exportar `DB_HOST=db` si se ejecuta el backend dentro de Compose.
- En Docker Compose, el backend expone `8080` (Dockerfile) pero el compose mapea `8000:8000`. Si ejecutan el backend en contenedor, cambiar mapping a `8000:8080` o `8080:8080`.
- Modelo de errores sencillo `{ code, message }` para 4xx.

## Siguientes pasos (opcional)
- Documentación OpenAPI (springdoc) para facilitar al front.
- Validaciones adicionales (formato correo, teléfono, longitud de campos).
- Tests unitarios de servicio y de controlador.
- Auditoría (timestamps/usuario creador) si se requiere.

