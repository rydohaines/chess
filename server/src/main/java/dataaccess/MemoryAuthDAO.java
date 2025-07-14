package dataaccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private final Collection<AuthData> AuthDatabase = new ArrayList<>();
    @Override
    public String addAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username,authToken);
        AuthDatabase.add(authData);
        return authToken;
    }
}
