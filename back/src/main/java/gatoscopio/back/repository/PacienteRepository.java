package gatoscopio.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gatoscopio.back.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {
}

