package gatoscopio.back.model;
import jakarta.persistence.*;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    private String codigo;

    private String nombre;
    private String correo;
    private String telefono;

    @Column(name = "caso_control")
    private String casoControl;

    // Getters y Setters


    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCasoControl() {
        return casoControl;
    }

}
