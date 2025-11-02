package gatoscopio.back.dto;

import jakarta.validation.constraints.NotNull;

public class RespuestaUpsertRequest {
    @NotNull
    private Integer preguntaId;

    @NotNull
    private String valor;

    public Integer getPreguntaId() { return preguntaId; }
    public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
}

