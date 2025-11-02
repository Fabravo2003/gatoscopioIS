package gatoscopio.back.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateRoleRequest {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,32}$", message = "nombre debe ser alfanumérico (3-32) con _ o - opcionales")
    private String nombre;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

