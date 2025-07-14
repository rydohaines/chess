package service;

import dataaccess.*;

public class UserService {
    private final UserDAO dataAccess = new MemoryUserDAO();
    private final AuthDAO authDataAccess = new MemoryAuthDAO();
    public RegisterResponse register(RegisterRequest req ){
        if(dataAccess.getUser(req.username()) == null){
            dataAccess.addUser(req.username());
            return authDataAccess.addAuth(req.username());
        }
        else throw DataAccessException;
    }
}
