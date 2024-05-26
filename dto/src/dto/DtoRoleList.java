package dto;

import java.util.Set;

public class DtoRoleList {
    Set<String> roles;

    public DtoRoleList(Set<String> roles) {
        this.roles = roles;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
