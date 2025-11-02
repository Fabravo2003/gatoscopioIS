package gatoscopio.back.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import gatoscopio.back.model.Usuario;
import gatoscopio.back.repository.UsuarioRepository;
import gatoscopio.back.repository.RoleRepository;
import gatoscopio.back.model.Role;
import gatoscopio.back.dto.UserSummary;
import gatoscopio.back.dto.CreateUserRequest;
import gatoscopio.back.dto.UpdateUserRequest;
import gatoscopio.back.dto.UpdatePasswordRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Override
    public Usuario createUser(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("usuario requerido");
        if (usuario.getCorreo() == null || usuario.getCorreo().isBlank()) throw new IllegalArgumentException("correo es requerido");
        if (repository.existsByCorreoIgnoreCase(usuario.getCorreo())) throw new IllegalStateException("correo ya existe");
        if (usuario.getContrasena() == null || usuario.getContrasena().length() < 8) throw new IllegalArgumentException("contrasena debe tener al menos 8 caracteres");
        var encoder = new BCryptPasswordEncoder();
        usuario.setContrasena(encoder.encode(usuario.getContrasena()));
        return repository.save(usuario);
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
    public Usuario createUser(CreateUserRequest req) {
        if (req == null) throw new IllegalArgumentException("body requerido");
        if (req.getNombre() == null || req.getNombre().isBlank()) throw new IllegalArgumentException("nombre es requerido");
        if (req.getCorreo() == null || req.getCorreo().isBlank()) throw new IllegalArgumentException("correo es requerido");
        if (!req.getCorreo().contains("@")) throw new IllegalArgumentException("correo inválido");
        if (repository.existsByCorreoIgnoreCase(req.getCorreo())) throw new IllegalStateException("correo ya existe");
        if (req.getContrasena() == null || req.getContrasena().length() < 8) throw new IllegalArgumentException("contrasena debe tener al menos 8 caracteres");

        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setCorreo(req.getCorreo());
        var encoder = new BCryptPasswordEncoder();
        u.setContrasena(encoder.encode(req.getContrasena()));

        // Roles opcionales
        java.util.Set<Role> nuevo = new java.util.LinkedHashSet<>();
        if (req.getRoles() != null) {
            for (String r : req.getRoles()) {
                if (r == null || r.trim().isEmpty()) throw new IllegalArgumentException("rol vacío");
                String key = r.trim();
                Role role = roleRepository.findById(key)
                        .orElseThrow(() -> new java.util.NoSuchElementException("rol no existe: " + key));
                nuevo.add(role);
            }
        }
        u.setRoles(nuevo);
        return repository.save(u);
    }

    @Override
    public Usuario updateUser(Integer id, UpdateUserRequest req) {
        if (req == null) throw new IllegalArgumentException("body requerido");
        Usuario u = repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("usuario no existe"));
        if (req.getNombre() != null && !req.getNombre().isBlank()) {
            u.setNombre(req.getNombre());
        }
        if (req.getCorreo() != null && !req.getCorreo().isBlank()) {
            if (!req.getCorreo().contains("@")) throw new IllegalArgumentException("correo inválido");
            if (repository.existsByCorreoIgnoreCaseAndIdNot(req.getCorreo(), id)) throw new IllegalStateException("correo ya existe");
            u.setCorreo(req.getCorreo());
        }
        return repository.save(u);
    }

    @Override
    public void deleteUser(Integer id) {
        if (id == null) throw new IllegalArgumentException("id requerido");
        if (!repository.existsById(id)) throw new java.util.NoSuchElementException("usuario no existe");
        repository.deleteById(id);
    }

    @Override
    public void changePassword(Integer id, String nuevaContrasena) {
        if (nuevaContrasena == null || nuevaContrasena.length() < 8) throw new IllegalArgumentException("contrasena debe tener al menos 8 caracteres");
        Usuario u = repository.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("usuario no existe"));
        var encoder = new BCryptPasswordEncoder();
        u.setContrasena(encoder.encode(nuevaContrasena));
        repository.save(u);
    }

    @Override
    public void changePassword(Integer id, UpdatePasswordRequest req) {
        if (req == null) throw new IllegalArgumentException("body requerido");
        changePassword(id, req.getContrasena());
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
