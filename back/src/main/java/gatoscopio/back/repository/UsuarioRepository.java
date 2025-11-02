package gatoscopio.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Útil para verificar si un usuario ya existe
    Optional<Usuario> findByCorreo(String correo);
}
