package gatoscopio.back.repository;

import gatoscopio.back.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByCorreoIgnoreCaseAndIdNot(String correo, Integer id);
    long countByRoles_NombreIgnoreCase(String nombre);
    org.springframework.data.domain.Page<gatoscopio.back.model.Usuario> findByRoles_NombreIgnoreCase(String nombre, org.springframework.data.domain.Pageable pageable);
}
