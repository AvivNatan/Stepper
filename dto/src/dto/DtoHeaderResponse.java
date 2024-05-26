package dto;

import java.util.Set;

public class DtoHeaderResponse {
    private Boolean isManager;
    private Set<String> roles;

    public DtoHeaderResponse(Boolean isManager, Set<String> roles) {
        this.isManager = isManager;
        this.roles = roles;
    }

    public Boolean getManager() {
        return isManager;
    }

    public void setManager(Boolean manager) {
        isManager = manager;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
