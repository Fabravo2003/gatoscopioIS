package gatoscopio.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gatoscopio.back.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}

