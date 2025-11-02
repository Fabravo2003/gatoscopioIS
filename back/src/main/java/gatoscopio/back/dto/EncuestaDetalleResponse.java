package gatoscopio.back.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EncuestaDetalleResponse {
    private Integer id;
    private String estado;
    private String pacienteCodigo;
    private LocalDateTime createdAt;
    private List<ItemRespuesta> respuestas;
    private UltimoEditor ultimoEditor;

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
    public UltimoEditor getUltimoEditor() { return ultimoEditor; }
    public void setUltimoEditor(UltimoEditor ultimoEditor) { this.ultimoEditor = ultimoEditor; }

    public static class ItemRespuesta {
        private Integer preguntaId;
        private String valor;
        private String nombreVariable;
        private String enunciado;
        private String tipoDato;
        private java.util.List<String> valoresValidos;

        public ItemRespuesta() {}
        public ItemRespuesta(Integer preguntaId, String valor) { this.preguntaId = preguntaId; this.valor = valor; }
        public Integer getPreguntaId() { return preguntaId; }
        public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }
        public String getValor() { return valor; }
        public void setValor(String valor) { this.valor = valor; }
        public String getNombreVariable() { return nombreVariable; }
        public void setNombreVariable(String nombreVariable) { this.nombreVariable = nombreVariable; }
        public String getEnunciado() { return enunciado; }
        public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
        public String getTipoDato() { return tipoDato; }
        public void setTipoDato(String tipoDato) { this.tipoDato = tipoDato; }
        public java.util.List<String> getValoresValidos() { return valoresValidos; }
        public void setValoresValidos(java.util.List<String> valoresValidos) { this.valoresValidos = valoresValidos; }
    }

    public static class UltimoEditor {
        private Integer id;
        private String nombre;
        private String correo;
        private LocalDateTime fechaHoraModificacion;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
        public LocalDateTime getFechaHoraModificacion() { return fechaHoraModificacion; }
        public void setFechaHoraModificacion(LocalDateTime fechaHoraModificacion) { this.fechaHoraModificacion = fechaHoraModificacion; }
    }
}
