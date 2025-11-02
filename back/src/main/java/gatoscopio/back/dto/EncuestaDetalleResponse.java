package gatoscopio.back.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EncuestaDetalleResponse {
    private Integer id;
    private String estado;
    private String pacienteCodigo;
    private LocalDateTime createdAt;
    private List<ItemRespuesta> respuestas;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getPacienteCodigo() { return pacienteCodigo; }
    public void setPacienteCodigo(String pacienteCodigo) { this.pacienteCodigo = pacienteCodigo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<ItemRespuesta> getRespuestas() { return respuestas; }
    public void setRespuestas(List<ItemRespuesta> respuestas) { this.respuestas = respuestas; }

    public static class ItemRespuesta {
        private Integer preguntaId;
        private String valor;
        public ItemRespuesta() {}
        public ItemRespuesta(Integer preguntaId, String valor) { this.preguntaId = preguntaId; this.valor = valor; }
        public Integer getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
    }
}

