package gatoscopio.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gatoscopio.back.model.Paciente;
import gatoscopio.back.repository.PacienteRepository;
import gatoscopio.back.service.ServiceEncuestador;

@Service
public class ServiceEncuestadorImpl implements ServiceEncuestador{

    private final PacienteRepository pacienteRepository;

    @Autowired
    public ServiceEncuestadorImpl(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public void createPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("paciente requerido");
        }
        if (isBlank(paciente.getCodigo())) {
            throw new IllegalArgumentException("codigo es requerido");
        }
        String cc = paciente.getCasoControl();
        if (cc == null || !("caso".equalsIgnoreCase(cc) || "control".equalsIgnoreCase(cc))) {
            throw new IllegalArgumentException("casoControl debe ser 'Caso' o 'Control'");
        }

        if (pacienteRepository.existsById(paciente.getCodigo())) {
            throw new IllegalStateException("paciente ya existe");
        }
        pacienteRepository.save(paciente);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    @Override
    public void createEncuesta() {
        // TODO: implementar en su HU
    }

    @Override
    public Paciente getPaciente(String codigo) {
        if (isBlank(codigo)) {
            throw new IllegalArgumentException("codigo es requerido");
        }
        return pacienteRepository.findById(codigo)
                .orElseThrow(() -> new java.util.NoSuchElementException("paciente no encontrado"));
    }

    @Override
    public org.springframework.data.domain.Page<Paciente> listPacientes(org.springframework.data.domain.Pageable pageable) {
        return pacienteRepository.findAll(pageable);
    }
    
}
