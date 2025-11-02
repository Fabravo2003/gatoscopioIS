package gatoscopio.back.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateEstadoEncuestaRequest {
    @NotBlank
    private String estado;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}

