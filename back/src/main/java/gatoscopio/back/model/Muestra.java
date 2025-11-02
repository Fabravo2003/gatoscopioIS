package gatoscopio.back.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "muestras")
public class Muestra {

    @Id
    private String codigo;

    @Column(name = "tipo_muestra_id")
    private Integer tipoMuestraId;

    @Column(name = "paciente_codigo")
    private String pacienteCodigo;

    private String observacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "cantidad_donada")
    private BigDecimal cantidadDonada;

    // Getters y Setters


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getTipoMuestraId() {
        return tipoMuestraId;
    }

    public void setTipoMuestraId(Integer tipoMuestraId) {
        this.tipoMuestraId = tipoMuestraId;
    }

    public String getPacienteCodigo() {
        return pacienteCodigo;
    }

    public void setPacienteCodigo(String pacienteCodigo) {
        this.pacienteCodigo = pacienteCodigo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getCantidadDonada() {
        return cantidadDonada;
    }

    public void setCantidadDonada(BigDecimal cantidadDonada) {
        this.cantidadDonada = cantidadDonada;
    }
}
