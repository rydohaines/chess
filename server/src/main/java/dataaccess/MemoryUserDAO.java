package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    private final Collection<UserData> UserDatabase = new ArrayList<>();
    public UserData getUser(String username){
        for(UserData user : UserDatabase){
            if(Objects.equals(username, user.username())){
                return user;
            }
        }
        return null;
    }
}
