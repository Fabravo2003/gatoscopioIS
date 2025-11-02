package gatoscopio.back.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gatoscopio.back.model.ValorValidoPregunta;

@Repository
public interface ValorValidoPreguntaRepository extends JpaRepository<ValorValidoPregunta, Integer> {
    List<ValorValidoPregunta> findByPreguntaId(Integer preguntaId);
}

