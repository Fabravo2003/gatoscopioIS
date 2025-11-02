package gatoscopio.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gatoscopio.back.model.TipoMuestra;

@Repository
public interface TipoMuestraRepository extends JpaRepository<TipoMuestra, Integer> {
}

