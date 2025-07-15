package service;

import dataaccess.*;
import model.UserData;

import java.util.Objects;

public class UserService {
    private final AuthDAO authDataAccess;
    private final UserDAO dataAccess;
    public UserService(UserDAO dataAccess, AuthDAO authDataAccess){
        this.dataAccess = dataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResponse register(RegisterRequest req ) throws DataAccessException {
        UserData user = new UserData(req.username(), req.password(), req.email());
        if(req.username() == null || req.password() == null || req.email() == null){
            throw new DataAccessException("Bad Request");
        }
        if(dataAccess.getUser(req.username()) == null){
            dataAccess.addUser(user);
            return new RegisterResponse(req.username(),authDataAccess.addAuth(req.username()));
        }
        else throw new DataAccessException("Already Taken");
    }
    public LoginResponse login(LoginRequest req) throws DataAccessException{
        UserData user = dataAccess.getUser(req.username());
        if(req.username() == null || req.password() == null || user == null) {
            throw new DataAccessException("Bad Request");
        }
        if(!Objects.equals(user.password(), req.password())){
            throw new DataAccessException("unauthorized");
        }
        return new LoginResponse(user.username(), authDataAccess.addAuth(req.username()));


    }
}
