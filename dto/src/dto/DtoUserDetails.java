package dto;

import java.util.List;
import java.util.Set;

public class DtoUserDetails
{
    private String userName;
    private Set<String> roles;
    private final Set<String> rolesNotAssigned;
    private int numUserFlows;
    private int numUserExecutions;

    private final boolean isManager;

    public DtoUserDetails(String userName, Set<String> roles,Set<String> rolesNotAssigned, int numUserFlows, int numUserExecutions,boolean isManager) {
        this.userName = userName;
        this.roles = roles;
        this.rolesNotAssigned=rolesNotAssigned;
        this.numUserFlows = numUserFlows;
        this.numUserExecutions = numUserExecutions;
        this.isManager=isManager;
    }

    public boolean isManager() {
        return isManager;
    }

    public String getUserName() {
        return userName;
    }

    public Set<String> getRolesNotAssigned() {
        return rolesNotAssigned;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public int getNumUserFlows() {
        return numUserFlows;
    }

    public void setNumUserFlows(int numUserFlows) {
        this.numUserFlows = numUserFlows;
    }

    public int getNumUserExecutions() {
        return numUserExecutions;
    }

    public void setNumUserExecutions(int numUserExecutions) {
        this.numUserExecutions = numUserExecutions;
    }
}
