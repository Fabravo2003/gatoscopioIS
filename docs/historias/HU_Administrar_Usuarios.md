# HU: Administrar usuarios (listar, actualizar, cambiar contraseña, eliminar)

Estado: Completada (backend-only)

## Endpoints
- `GET /api/usuarios?page=0&size=20&sort=id,asc` → página de `UserSummary { id, nombre, correo, roles }`
- `PUT /api/usuarios/{id}` body `{ nombre?, correo? }` → 200 usuario actualizado
- `PUT /api/usuarios/{id}/password` body `{ contrasena }` → 204 sin contenido
- `DELETE /api/api/usuarios/{id}` → 204 sin contenido

## Validaciones
- Update: correo válido si se envía; único (case-insensitive) y no perteneciente a otro usuario.
- Password: longitud mínima 8; se guarda con BCrypt; no se expone nunca.
- Delete: 404 si no existe; 400 si id inválido.

## Archivos clave
- Controlador: `back/src/main/java/gatoscopio/back/controller/AdminController.java`
- Servicio: `back/src/main/java/gatoscopio/back/service/impl/ServiceAdminImpl.java`
- DTOs: `UpdateUserRequest`, `UpdatePasswordRequest`, `UserSummary`
- Repositorio: `UsuarioRepository` con `existsByCorreoIgnoreCaseAndIdNot`

## Pruebas rápidas
```
# Listar usuarios
curl "http://localhost:8080/api/usuarios?page=0&size=5&sort=id,asc"

# Actualizar datos
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Nuevo Nombre","correo":"nuevo@example.com"}'

# Cambiar contraseña
curl -X PUT http://localhost:8080/api/usuarios/1/password \
  -H "Content-Type: application/json" \
  -d '{"contrasena":"Contrasena123"}' -i

# Eliminar
curl -X DELETE http://localhost:8080/api/usuarios/1 -i
```

