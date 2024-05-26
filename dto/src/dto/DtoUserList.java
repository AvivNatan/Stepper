package dto;

import java.util.ArrayList;
import java.util.List;

public class DtoUserList
{
    List<String> users;

    public DtoUserList() {
        this.users = new ArrayList<>();
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
