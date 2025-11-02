package gatoscopio.back.dto;

import java.util.Set;
import jakarta.validation.constraints.NotEmpty;

public class RolesUpdateRequest {
    @NotEmpty
    private Set<String> roles;

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}
