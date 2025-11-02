package gatoscopio.back.model;

import jakarta.persistence.*;

@Entity
@Table(name = "valor_valido_pregunta")
public class ValorValidoPregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String valor;

    @Column(name = "pregunta_id")
    private Integer preguntaId;

    public Integer getId() { return id; }
    public String getValor() { return valor; }
    public Integer getPreguntaId() { return preguntaId; }
}

