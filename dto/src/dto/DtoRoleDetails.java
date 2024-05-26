package dto;

import java.util.List;
import java.util.Set;

public class DtoRoleDetails {
    private final String roleName;
    private final String roleDescription;
    private final Set<String> users;
    private final Set<String> flows;
    private final Set<String> flowsNotAssigned;


    public DtoRoleDetails(String roleName, String roleDescription, Set<String> users,Set<String> flows,
                          Set<String> flowsNotAssigned ) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
        this.users = users;
        this.flows=flows;
        this.flowsNotAssigned=flowsNotAssigned;
    }

    public Set<String> getFlows() {
        return flows;
    }

    public Set<String> getFlowsNotAssigned() {
        return flowsNotAssigned;
    }

    public Set<String> getUsers() {
        return users;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

}
