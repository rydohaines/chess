package service;

import dataaccess.*;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import responses.*;

import java.sql.SQLException;
import java.util.Objects;

public class UserService {
    private final AuthDAO authDataAccess;
    private final UserDAO dataAccess;
    public UserService(UserDAO dataAccess, AuthDAO authDataAccess){
        this.dataAccess = dataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResponse register(RegisterRequest req ) throws Exception{
        UserData user = new UserData(req.username(), req.password(), req.email());
        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new DataAccessException("Bad Request");
        }
        if (dataAccess.getUser(req.username()) == null) {
            dataAccess.addUser(user);
            String authData = authDataAccess.addAuth(user.username());
            return new RegisterResponse(req.username(), authData);
        } else{
            throw new DataAccessException("Already Taken");
        }
    }
    public LoginResponse login(LoginRequest req) throws DataAccessException, ResponseException, SQLException {
        UserData user = dataAccess.getUser(req.username());
        if(req.username() == null || req.password() == null) {
            throw new DataAccessException("Bad Request");
        }
        if(user == null){
            throw new DataAccessException("unauthorized");
        }
        if(!BCrypt.checkpw(req.password(),user.password())){
            throw new DataAccessException("unauthorized");
        }
        authDataAccess.addAuth(req.username());
        return new LoginResponse(user.username(), authDataAccess.addAuth(req.username()));


    }
    public void logout(LogoutRequest req) throws DataAccessException, SQLException {
        if(authDataAccess.getAuth(req.authToken()) == null){
            throw new DataAccessException("unauthorized");
        }
        else {
            authDataAccess.deleteAuth(req.authToken());
        }
    }
    public AuthDAO getAuthDataAccess(){
        return authDataAccess;
    }
}
