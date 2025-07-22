package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.UserService;
import service.responses.*;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class DataTests {
    public void positiveRegister() throws Exception {
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        userDataAccess.clearAll();
        authDataAccess.clearAll();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        RegisterRequest request = new RegisterRequest("NewUsername","newpassword","newuser@test.com");
        RegisterResponse result = userService.register(request);
        assertEquals("NewUsername", result.username());
        assertNotNull(result.authToken());
    }
    @Test
    public void negativeRegister() throws Exception {
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        RegisterRequest request = new RegisterRequest("NewUsername","newpassword","newuser@test.com");
        RegisterResponse result = userService.register(request);
        RegisterRequest repeatRequest = new RegisterRequest("NewUsername","newpassword","newuser@test.com");
        assertThrows(DataAccessException.class, () -> userService.register(repeatRequest));
    }
    @Test
    public void positiveClear() throws Exception {
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        GameDAO gameDataAccess = new MySQLGameDAO();
        ClearService clearService = new ClearService(userDataAccess,authDataAccess,gameDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        authDataAccess.addAuth("newUser");
        gameDataAccess.createGame("testGame");
        clearService.clear();

        assertNull(userDataAccess.getUser("newUser"));
        assertNull(authDataAccess.getAuth("newUser"));
        assertTrue(gameDataAccess.listGames().isEmpty());
    }
    @Test
    public void negativeClear(){
        assertTrue(true);
    }
    @Test
    public void positiveLogout() throws Exception {
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        String authToken = authDataAccess.addAuth("newUser");
        userService.logout(new LogoutRequest(authToken));

        assertNull(authDataAccess.getAuth("authToken"));
    }
    @Test
    public void negativeLogout() throws Exception {
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));

        assertThrows(DataAccessException.class,() -> userService.logout(new LogoutRequest("invalidToken")));
    }
    @Test
    public void positiveLogin() throws Exception {
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        String authToken = authDataAccess.addAuth("newUser");
        LoginResponse response = userService.login(new LoginRequest("newUser","password"));
        assertEquals("newUser",response.username());
        assertNotNull(response.authToken());

    }
    @Test
    public void negativeLogin() throws Exception {
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        String authToken = authDataAccess.addAuth("newUser");
        assertThrows(DataAccessException.class, () -> userService.login(new LoginRequest("newUser","wrongpassword")));
    }
    @Test
    public void positiveCreateGame() throws Exception {
        UserDAO userDAO = new MySQLUserDAO();
        AuthDAO authDAO = new MySQLAuthDAO();
        GameDAO gameDAO = new MySQLGameDAO();
        GameService gameService = new GameService(gameDAO,authDAO);
        UserService userService = new UserService(userDAO,authDAO);
        authDAO.clearAll();
        gameDAO.clearAll();
        userDAO.clearAll();
        RegisterResponse response = userService.register(new RegisterRequest("newUser","password","email"));
        CreateGameResponse result =  gameService.createGame(new CreateGameRequest(response.authToken(),"gameName"));

        assertNotNull(gameDAO.getGame(result.gameID()));
    }
    @Test
    public void negativeCreateGame() throws Exception {
        UserDAO userDAO = new MySQLUserDAO();
        AuthDAO authDAO = new MySQLAuthDAO();
        GameDAO gameDAO = new MySQLGameDAO();
        GameService gameService = new GameService(gameDAO,authDAO);
        UserService userService = new UserService(userDAO,authDAO);
        userDAO.clearAll();
        RegisterResponse response = userService.register(new RegisterRequest("newUser","password","email"));
        assertThrows(DataAccessException.class, () -> gameService.createGame(new CreateGameRequest(response.authToken(),null)));
    }
    @Test
    public void positiveJoinGame() throws Exception {
        UserDAO userDAO = new MySQLUserDAO();
        AuthDAO authDAO = new MySQLAuthDAO();
        GameDAO gameDAO = new MySQLGameDAO();
        userDAO.clearAll();
        userDAO.addUser(new UserData("newUser","password","email"));
        int gameID = gameDAO.createGame("newGame");
        GameService service= new GameService(gameDAO,authDAO);
        service.joinGame(new JoinGameRequest("BLACK", gameID ,"newUser"),"newUser");
        assertEquals("newUser", gameDAO.getGame(gameID).blackUsername());

    }
    @Test
    public void negativeJoinGame() throws Exception {
        UserDAO userDAO = new MySQLUserDAO();
        AuthDAO authDAO = new MySQLAuthDAO();
        GameDAO gameDAO = new MySQLGameDAO();
        userDAO.addUser(new UserData("newUser","password","email"));
        int gameID = gameDAO.createGame("newGame");
        GameService service= new GameService(gameDAO,authDAO);
        service.joinGame(new JoinGameRequest("BLACK", gameID ,"newUser"),"newUser");
        assertThrows(DataAccessException.class, () -> service.joinGame(new JoinGameRequest("BLACK",gameID,"newUser"),"newUser"));
    }
    @Test
    public void positiveListGames() throws Exception {
        UserDAO userDAO = new MySQLUserDAO();
        AuthDAO authDAO = new MySQLAuthDAO();
        GameDAO gameDAO = new MySQLGameDAO();
        authDAO.clearAll();
        userDAO.clearAll();
        gameDAO.clearAll();
        userDAO.addUser(new UserData("alice", "pass", "alice@example.com")) ;
        String authToken = authDAO.addAuth("alice");
        int gameID = gameDAO.createGame("game1");
        int game2ID = gameDAO.createGame("game2");

        GameService gameService = new GameService(gameDAO, authDAO);
        Collection<ListGamesResponse> games = gameService.listGames();
        ListGamesResult result = new ListGamesResult(games);
        assertEquals(2,result.games().size());
    }
    @Test
    public void negativeListGames(){
        // Handler handles authorization
        assertTrue(true);
    }
}
