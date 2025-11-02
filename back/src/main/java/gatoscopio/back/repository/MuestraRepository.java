package gatoscopio.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gatoscopio.back.model.Muestra;

@Repository
public interface MuestraRepository extends JpaRepository<Muestra, String> {
}

