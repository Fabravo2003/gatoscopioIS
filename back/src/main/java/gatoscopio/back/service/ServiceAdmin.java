package gatoscopio.back.service;


import java.util.List;

import gatoscopio.back.model.Usuario;

public interface ServiceAdmin {
   
    void createUser(Usuario usuario);
    void modifyUser(Usuario usuario);
    void deleteUser(Usuario usuario);
    List<Usuario> getUsers();

    // Roles
    java.util.List<String> listRoles();
    java.util.List<String> getUserRoles(Integer userId);
    Usuario setUserRoles(Integer userId, java.util.Set<String> roles);

    // Usuarios (paginado con roles)
    org.springframework.data.domain.Page<gatoscopio.back.dto.UserSummary> pageUsers(org.springframework.data.domain.Pageable pageable);

}
