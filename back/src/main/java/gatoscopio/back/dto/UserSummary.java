package gatoscopio.back.dto;

import java.util.List;

public class UserSummary {
    private Integer id;
    private String nombre;
    private String correo;
    private List<String> roles;

    public UserSummary() {}

    public UserSummary(Integer id, String nombre, String correo, List<String> roles) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.roles = roles;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}

