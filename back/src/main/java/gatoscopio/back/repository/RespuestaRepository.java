package gatoscopio.back.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import gatoscopio.back.model.Respuesta;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    List<Respuesta> findByEncuestaId(Integer encuestaId);
    Optional<Respuesta> findByEncuestaIdAndPreguntaId(Integer encuestaId, Integer preguntaId);
}

