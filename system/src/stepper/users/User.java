package stepper.users;

import stepper.roles.Role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class User
{
    private String userName;
    private final Set<String> rolesNames;
    private boolean isManager;

    public User(String userName,boolean isManager) {
        this.userName = userName;
        this.rolesNames = new HashSet<>();
        this.isManager = isManager;
    }
    public User(String userName) {
        this(userName,false);
    }
    public void addRole(String name)
    {
        this.rolesNames.add(name);
    }
    public void RemoveRole(String name)
    {
        this.rolesNames.remove(name);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getRolesNames() {
        return rolesNames;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }
}
