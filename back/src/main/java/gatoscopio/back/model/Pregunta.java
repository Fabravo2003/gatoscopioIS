package gatoscopio.back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas")
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_variable")
    private String nombreVariable;

    private String codigo;

    @Column(name = "tipo_dato")
    private String tipoDato;

    private String enunciado;

    private String audiencia;

    public Integer getId() { return id; }
    public String getNombreVariable() { return nombreVariable; }
    public String getCodigo() { return codigo; }
    public String getTipoDato() { return tipoDato; }
    public String getEnunciado() { return enunciado; }
    public String getAudiencia() { return audiencia; }
}

