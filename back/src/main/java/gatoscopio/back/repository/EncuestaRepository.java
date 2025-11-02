package gatoscopio.back.repository;

import gatoscopio.back.model.Encuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Integer> {

    @Query("select e from Encuesta e where (:estado is null or lower(e.estado) = lower(:estado)) and (:paciente is null or e.pacienteCodigo = :paciente)")
    Page<Encuesta> findAllFiltered(String estado, String paciente, Pageable pageable);
}
