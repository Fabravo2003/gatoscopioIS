package gatoscopio.back.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import gatoscopio.back.model.Usuario;
import gatoscopio.back.repository.UsuarioRepository;
import gatoscopio.back.repository.RoleRepository;
import gatoscopio.back.model.Role;
import gatoscopio.back.dto.UserSummary;
import org.springframework.transaction.annotation.Transactional;
import gatoscopio.back.service.ServiceAdmin;

@Service
public class ServiceAdminImpl implements ServiceAdmin {
     private final UsuarioRepository repository;
     private final RoleRepository roleRepository;

    @Autowired
    public ServiceAdminImpl(UsuarioRepository repository, RoleRepository roleRepository){
        this.repository = repository;
        this.roleRepository = roleRepository;
    }

    public void createUser(Usuario usuario) {
        //TODO
    }

    public void modifyUser(Usuario usuario) {
        //TODO
    }

    public void deleteUser(Usuario usuario) {
        //TODO
    }

    public List<Usuario> getUsers() {
        //TODO
        return List.of();
    }

    // ===== Roles =====
    @Override
    public java.util.List<String> listRoles() {
        return roleRepository.findAll().stream().map(Role::getNombre).toList();
    }

    @Override
    public java.util.List<String> getUserRoles(Integer userId) {
        Usuario u = repository.findById(userId).orElseThrow(() -> new java.util.NoSuchElementException("usuario no existe"));
        return u.getRoles().stream().map(Role::getNombre).toList();
    }

    @Override
    public Usuario setUserRoles(Integer userId, java.util.Set<String> roles) {
        if (roles == null) throw new IllegalArgumentException("roles requeridos");
        Usuario u = repository.findById(userId).orElseThrow(() -> new java.util.NoSuchElementException("usuario no existe"));
        java.util.Set<Role> nuevo = new java.util.LinkedHashSet<>();
        for (String r : roles) {
            if (r == null || r.trim().isEmpty()) {
                throw new IllegalArgumentException("rol vacío");
            }
            String key = r.trim();
            Role role = roleRepository.findById(key)
                .orElseThrow(() -> new java.util.NoSuchElementException("rol no existe: " + key));
            nuevo.add(role);
        }
        u.setRoles(nuevo);
        return repository.save(u);
    }

    @Override
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<UserSummary> pageUsers(org.springframework.data.domain.Pageable pageable) {
        var page = repository.findAll(pageable);
        return page.map(u -> new UserSummary(
                u.getId(),
                u.getNombre(),
                u.getCorreo(),
                u.getRoles().stream().map(Role::getNombre).toList()
        ));
    }
}
