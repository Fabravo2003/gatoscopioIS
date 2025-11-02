package gatoscopio.back.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tipos_muestra")
public class TipoMuestra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "minimo_alerta")
    private BigDecimal minimoAlerta;

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public BigDecimal getMinimoAlerta() { return minimoAlerta; }
    public void setMinimoAlerta(BigDecimal minimoAlerta) { this.minimoAlerta = minimoAlerta; }
}

