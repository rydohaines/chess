package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryUserDAO implements UserDAO{
    private final Collection<UserData> userDatabase = new ArrayList<>();
    @Override
    public UserData getUser(String username){
        for(UserData user : userDatabase){
            if(Objects.equals(username, user.username())){
                return user;
            }
        }
        return null;
    }
    @Override
    public void addUser(UserData userData){
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        userDatabase.add(new UserData(userData.username(),hashedPassword, userData.email()));
    }
    @Override
    public void clearAll(){
        userDatabase.clear();
    }
}
