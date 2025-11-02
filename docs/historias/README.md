# Historias de Usuario (Backend)

Este directorio reúne la documentación técnica de las historias de usuario implementadas en el backend de Spring Boot. Cada HU incluye:

- Contexto y alcance
- Endpoints expuestos y contratos
- Validaciones y manejo de errores
- Pasos para levantar y probar
- Archivos tocados y decisiones
- Notas y siguientes pasos

Listado:
- HU_Registrar_Paciente.md
- HU_Registrar_Muestra.md
- HU_Gestionar_Roles.md
- HU_Crear_Usuario.md
- HU_Administrar_Usuarios.md
- HU_Editar_Encuestas.md

## Comunes
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Formato de error estándar (ControllerAdvice):
  - `{ "timestamp": "...", "code": "bad_request|conflict|not_found", "message": "..." }`
- CORS: configurable con `app.cors.allowed-origins` (por defecto `*`).
