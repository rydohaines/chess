package dataaccess;

import model.AuthData;

import java.util.*;

public class MemoryAuthDAO implements AuthDAO{
    private final Collection<AuthData> AuthDatabase = new ArrayList<>();
    @Override
    public String addAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username,authToken);
        AuthDatabase.add(authData);
        return authToken;
    }
    @Override
    public void clearAll(){
        AuthDatabase.clear();
    }
    @Override
    public String getAuth(String authToken){
        for(AuthData authData : AuthDatabase){
           if(Objects.equals(authToken, authData.authToken())){
               return authToken;
            }
        }
        return null;
    }
    @Override
    public String getUser(String authToken){
        for(AuthData authData : AuthDatabase){
            if(Objects.equals(authToken, authData.authToken())){
                return authData.username();
            }
        }
        return null;
    }
    @Override
    public void deleteAuth(String authToken) {
        Iterator<AuthData> iterator = AuthDatabase.iterator();
        while (iterator.hasNext()) {
            AuthData authData = iterator.next();
            if (Objects.equals(authToken, authData.authToken())) {
                iterator.remove();
                return;
            }
        }
    }
}
