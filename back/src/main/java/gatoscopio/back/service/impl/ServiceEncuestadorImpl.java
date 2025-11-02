package gatoscopio.back.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gatoscopio.back.model.Paciente;
import gatoscopio.back.model.Muestra;
import gatoscopio.back.repository.PacienteRepository;
import gatoscopio.back.repository.MuestraRepository;
import gatoscopio.back.service.ServiceEncuestador;

@Service
public class ServiceEncuestadorImpl implements ServiceEncuestador{

    private final PacienteRepository pacienteRepository;
    private final MuestraRepository muestraRepository;

    @Autowired
    public ServiceEncuestadorImpl(PacienteRepository pacienteRepository, MuestraRepository muestraRepository) {
        this.pacienteRepository = pacienteRepository;
        this.muestraRepository = muestraRepository;
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

    @Override
    public void createMuestra(Muestra muestra) {
        if (muestra == null) {
            throw new IllegalArgumentException("muestra requerida");
        }
        if (isBlank(muestra.getCodigo())) {
            throw new IllegalArgumentException("codigo es requerido");
        }
        if (muestra.getTipoMuestraId() == null) {
            throw new IllegalArgumentException("tipoMuestraId es requerido");
        }
        if (muestraRepository.existsById(muestra.getCodigo())) {
            throw new IllegalStateException("muestra ya existe");
        }
        // Validar paciente si viene
        if (muestra.getPacienteCodigo() != null && !muestra.getPacienteCodigo().trim().isEmpty()) {
            if (!pacienteRepository.existsById(muestra.getPacienteCodigo())) {
                throw new java.util.NoSuchElementException("paciente no existe");
            }
        }
        muestraRepository.save(muestra);
    }

    @Override
    public Muestra getMuestra(String codigo) {
        if (isBlank(codigo)) {
            throw new IllegalArgumentException("codigo es requerido");
        }
        return muestraRepository.findById(codigo)
                .orElseThrow(() -> new java.util.NoSuchElementException("muestra no encontrada"));
    }

    @Override
    public org.springframework.data.domain.Page<Muestra> listMuestras(org.springframework.data.domain.Pageable pageable) {
        return muestraRepository.findAll(pageable);
    }
    
}
