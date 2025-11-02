package gatoscopio.back.dto;

import jakarta.validation.constraints.Email;

public class UpdateUserRequest {
    private String nombre;   // opcional

    @Email
    private String correo;   // opcional

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
