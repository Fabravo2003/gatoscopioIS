package gatoscopio.back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "respuestas")
public class Respuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "encuesta_id")
    private Integer encuestaId;

    @Column(name = "pregunta_id")
    private Integer preguntaId;

    @Column(columnDefinition = "text")
    private String valor;

    public Integer getId() { return id; }
    public Integer getEncuestaId() { return encuestaId; }
    public void setEncuestaId(Integer encuestaId) { this.encuestaId = encuestaId; }
    public Integer getPreguntaId() { return preguntaId; }
    public void setPreguntaId(Integer preguntaId) { this.preguntaId = preguntaId; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
}

