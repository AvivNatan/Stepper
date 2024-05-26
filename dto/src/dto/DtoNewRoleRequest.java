package dto;

import java.util.List;

public class DtoNewRoleRequest
{
    private String roleNAme;
    private String Description;
    private List<String> flows;

    public DtoNewRoleRequest(String roleNAme, String description, List<String> flows) {
        this.roleNAme = roleNAme;
        Description = description;
        this.flows = flows;
    }

    public String getRoleNAme() {
        return roleNAme;
    }

    public void setRoleNAme(String roleNAme) {
        this.roleNAme = roleNAme;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<String> getFlows() {
        return flows;
    }

    public void setFlows(List<String> flows) {
        this.flows = flows;
    }
}
