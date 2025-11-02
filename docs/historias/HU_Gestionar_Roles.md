# HU: Gestionar roles

Estado: Completada (backend-only)

## Objetivo
Como administrador, gestionar roles de usuario para asignar permisos.

## Modelo
- Roles ManyToMany: `usuarios ⇄ usuarios_roles ⇄ roles`.
- Entidades:
  - `Usuario.roles: Set<Role>` mapeado a `usuarios_roles(usuario_id, rol_nombre)`.
  - `Role(nombre PK)` mapeado a `roles`.

## Endpoints
- `GET /api/roles` → 200 `["admin","investigador","encuestador"]`
- `GET /api/usuarios/{id}/roles` → 200 `["admin", ...]` (404 si usuario no existe)
- `PUT /api/usuarios/{id}/roles` → 200 usuario con roles
  - Request: `{ "roles": ["admin","encuestador"] }`
  - Errores: 400 body inválido; 404 usuario/rol no existe

## Validaciones
- Usuario debe existir.
- Cada rol debe existir en `roles`.
- Conjunto sin duplicados; operación idempotente (reemplaza el set).

## Archivos clave
- `back/src/main/java/gatoscopio/back/model/Role.java`
- `back/src/main/java/gatoscopio/back/model/Usuario.java` (ManyToMany)
- `back/src/main/java/gatoscopio/back/repository/RoleRepository.java`
- `back/src/main/java/gatoscopio/back/service/ServiceAdmin.java`
- `back/src/main/java/gatoscopio/back/service/impl/ServiceAdminImpl.java`
- `back/src/main/java/gatoscopio/back/controller/AdminController.java`
- `back/src/main/java/gatoscopio/back/dto/RolesUpdateRequest.java`

## Cómo probar
1) Listar roles disponibles
```
curl http://localhost:8080/api/roles
```
2) Ver roles de un usuario (ej: id=1)
```
curl http://localhost:8080/api/usuarios/1/roles
```
3) Asignar roles
```
curl -X PUT http://localhost:8080/api/usuarios/1/roles \
  -H "Content-Type: application/json" \
  -d '{"roles":["admin","encuestador"]}'
```

## Notas
- Semillas: `db/init/002_seed.sql` ya incluye `roles` y usuarios de prueba.
- CORS y Swagger habilitados para facilitar pruebas desde front.

