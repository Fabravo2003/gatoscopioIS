package gatoscopio.back.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "muestras")
public class Muestra {

    @Id
    private String codigo;

    private String observacion;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "cantidad_donada")
    private BigDecimal cantidadDonada;

    // Getters y Setters


    public String getCodigo() {
        return codigo;
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
