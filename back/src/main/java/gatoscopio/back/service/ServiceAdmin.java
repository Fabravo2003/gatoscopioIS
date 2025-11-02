package gatoscopio.back.service;


import java.util.List;

import gatoscopio.back.model.Usuario;

public interface ServiceAdmin {
   
    Usuario createUser(Usuario usuario);
    void modifyUser(Usuario usuario);
    void deleteUser(Usuario usuario);
    List<Usuario> getUsers();

    // Roles
    java.util.List<String> listRoles();
    java.util.List<String> getUserRoles(Integer userId);
    Usuario setUserRoles(Integer userId, java.util.Set<String> roles);

    // Usuarios (paginado con roles)
    org.springframework.data.domain.Page<gatoscopio.back.dto.UserSummary> pageUsers(org.springframework.data.domain.Pageable pageable);

    // Crear usuario (DTO)
    Usuario createUser(gatoscopio.back.dto.CreateUserRequest req);

    // Actualizar datos básicos (sin contraseña) y eliminar
    Usuario updateUser(Integer id, gatoscopio.back.dto.UpdateUserRequest req);
    void deleteUser(Integer id);

    // Cambiar contraseña
    void changePassword(Integer id, String nuevaContrasena);
    void changePassword(Integer id, gatoscopio.back.dto.UpdatePasswordRequest req);

}
