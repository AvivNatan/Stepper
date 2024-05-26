package stepper.users;

import dto.DtoUserList;

import java.util.*;

public class UserManager
{
    private final Set<User> usersSet;

    public UserManager()
    {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(User user) {
        usersSet.add(user);
    }

    public synchronized void removeUser(User user) {
        usersSet.remove(user);
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public synchronized DtoUserList getUsersNames() {
        List<String> res=new ArrayList<>();
        for(User user:this.usersSet)
            res.add(user.getUserName());
        DtoUserList list=new DtoUserList();
        list.getUsers().addAll(res);
        return list;
    }

    public boolean isUserExists(String username) {
        for(User user:this.usersSet)
        {
            if(Objects.equals(user.getUserName(), username))
                return true;
        }
        return false;
    }
    public User getUserByName(String userName)
    {
        for(User user:this.usersSet)
        {
            if(Objects.equals(user.getUserName(), userName))
                return user;
        }
        return null;
    }
}
