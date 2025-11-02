package gatoscopio.back.service;

import gatoscopio.back.model.Paciente;

public interface ServiceEncuestador {

    void createPaciente(Paciente paciente);
    void createEncuesta();
    Paciente getPaciente(String codigo);
    org.springframework.data.domain.Page<Paciente> listPacientes(org.springframework.data.domain.Pageable pageable);

    void createMuestra(gatoscopio.back.model.Muestra muestra);

}
