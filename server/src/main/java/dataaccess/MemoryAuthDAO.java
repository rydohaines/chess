package dataaccess;

import model.AuthData;

import java.util.*;

public class MemoryAuthDAO implements AuthDAO{
    private final Collection<AuthData> authDatabase = new ArrayList<>();
    @Override
    public String addAuth(String username){
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username,authToken);
        authDatabase.add(authData);
        return authToken;
    }
    @Override
    public void clearAll(){
        authDatabase.clear();
    }
    @Override
    public String getAuth(String authToken){
        for(AuthData authData : authDatabase){
           if(Objects.equals(authToken, authData.authToken())){
               return authToken;
            }
        }
        return null;
    }
    @Override
    public String getUser(String authToken){
        for(AuthData authData : authDatabase){
            if(Objects.equals(authToken, authData.authToken())){
                return authData.username();
            }
        }
        return null;
    }
    @Override
    public void deleteAuth(String authToken) {
        Iterator<AuthData> iterator = authDatabase.iterator();
        while (iterator.hasNext()) {
            AuthData authData = iterator.next();
            if (Objects.equals(authToken, authData.authToken())) {
                iterator.remove();
                return;
            }
        }
    }
}
