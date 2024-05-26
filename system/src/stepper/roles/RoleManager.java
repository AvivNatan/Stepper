package stepper.roles;

import stepper.users.User;

import java.util.*;

public class RoleManager
{
    private final Map<String,Role> roles;

    public RoleManager()
    {
        roles =new HashMap<>();
        initRoles();
    }

    public synchronized void addRole(String name, String description, Set<String> flowsNames) {
        roles.put(name,new Role(name,description,flowsNames));
    }
    public synchronized void addRole(Role role) {
        roles.put(role.getNameRole(),role);
    }
//    public synchronized void removeRole(String role) {
//        rolesSet.remove(role);
//    }
    public Set<String> getRolesNotAssignedOfUser(User user)
    {
        Set<String> rolesNot=new HashSet<>();
        for(Role role:this.roles.values())
        {
            if(!role.isUserExistInRole(user.getUserName()))
                rolesNot.add(role.getNameRole());
        }
        return rolesNot;
    }
    public synchronized Role getRole(String name) {
        return this.roles.get(name);
    }
    public boolean isRoleExists(String name) {
        return this.roles.get(name) != null;
    }
    private void initRoles()
    {
        Role readOnly=new Role("Read Only Flows","This role is for read only flows",new HashSet<>());
                Role allFlows=new Role("All Flows","This role is for all flows",new HashSet<>());
                addRole(readOnly);
                addRole(allFlows);
    }
    public Set<String> getRolesNames()
    {
        return this.roles.keySet();
    }
}
