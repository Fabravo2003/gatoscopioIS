# HU: Crear usuario

Estado: Completada (backend-only)

## Objetivo
Como administrador, crear un usuario con nombre, correo y contraseña segura, y asignar roles iniciales.

## Endpoint
- `POST /api/usuarios`
- Request JSON:
  ```json
  {
    "nombre": "Admin 2",
    "correo": "admin2@example.com",
    "contrasena": "Secreta123",
    "roles": ["admin", "encuestador"]
  }
  ```
- Respuestas:
  - 201 Created: `{ id, nombre, correo, roles: [..] }`
  - 400 Bad Request: validaciones (campos, contraseña >= 8, correo válido)
  - 404 Not Found: algún rol no existe
  - 409 Conflict: correo ya existe

## Seguridad
- La contraseña se almacena con BCrypt; nunca retorna en respuestas.

## Validaciones
- `nombre`: requerido.
- `correo`: requerido, con `@` básico y único (case-insensitive).
- `contrasena`: requerida; longitud mínima 8.
- `roles`: opcional; si se envía, cada rol debe existir en `roles`.

## Archivos clave
- `back/src/main/java/gatoscopio/back/dto/CreateUserRequest.java`
- `back/src/main/java/gatoscopio/back/repository/UsuarioRepository.java` (existsByCorreoIgnoreCase)
- `back/src/main/java/gatoscopio/back/service/ServiceAdmin.java` (métodos createUser)
- `back/src/main/java/gatoscopio/back/service/impl/ServiceAdminImpl.java` (hash + validaciones + roles)
- `back/src/main/java/gatoscopio/back/controller/AdminController.java` (POST /api/usuarios)

## Cómo probar
```
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Admin 2","correo":"admin2@example.com","contrasena":"Secreta123","roles":["admin"]}'
```

