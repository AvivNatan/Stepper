package stepper.roles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Role
{
    private String nameRole;
    private String description;
    private Set<String> flowsNames;
    private Set<String> usersNames;

    public Role(String nameRole, String description,Set<String> flowsNames) {
        this.nameRole = nameRole;
        this.description = description;
        this.flowsNames = flowsNames;
        this.usersNames = new HashSet<>();
    }
    public boolean isUserExistInRole(String userName)
    {
        return usersNames.contains(userName);
    }

    public Set<String> getUsersNames() {
        return usersNames;
    }

    public void setUsersNames(Set<String> usersNames) {
        this.usersNames = usersNames;
    }
    public void addUserToRole(String name)
    {
        this.usersNames.add(name);
    }
    public void RemoveUserFromRole(String name)
    {
        this.usersNames.remove(name);
    }

    public String getNameRole() {
        return nameRole;
    }
    public void addFlow(String nameFlow)
    {
        this.flowsNames.add(nameFlow);
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getFlowsNames() {
        return flowsNames;
    }

    public void setFlowsNames(Set<String> flowsNames) {
        this.flowsNames = flowsNames;
    }
}
