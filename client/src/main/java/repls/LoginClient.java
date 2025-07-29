package repls;

import dataaccess.ResponseException;
import server.ServerFacade;
import service.responses.*;

import java.util.Arrays;
import java.util.Objects;

import static repls.ClientStatus.*;

public class LoginClient implements Client {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final Repl repl;
    private ClientStatus status = PRELOGIN;
    private String authToken = null;
    private String currentUser = null;

    public LoginClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        serverFacade = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    public String help() {
        if (status == PRELOGIN) {
            return """
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account"
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        if (status == POSTLOGIN) {
            return """
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return null;
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> create(params);
                case"logout" -> logout();
                case "quit" -> "quit";
                case"join"-> join(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
    public String join(String ... params) throws Exception {
        assertSignedIn();
        int gameID;
        try {
            gameID = Integer.parseInt(params[0]);
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Invalid game ID: please enter a valid number");
        }
                if (Objects.equals(params[1], "black")) {
                JoinGameRequest request = new JoinGameRequest(params[1],gameID, currentUser);
                serverFacade.join(request,authToken);
            } else if (Objects.equals(params[1], "white")) {
                JoinGameRequest request = new JoinGameRequest(params[1],gameID, currentUser);
                serverFacade.join(request,authToken);
            }
            else{
                throw new ResponseException(400, "please enter 'WHITE' or 'BLACK'");
            }
            status = GAMESTATUS;
        return "Joined game as " + params[1];
    }
    public String logout() throws Exception {
        assertSignedIn();
        LogoutRequest request = new LogoutRequest(authToken);
        serverFacade.logout(request);
        status = PRELOGIN;
        authToken = null;
        currentUser = null;
        return "You have logged out. Type 'help' for more options";
    }

    public String login(String... params) throws Exception {
        assertSignedOut();
        LoginRequest request = new LoginRequest(params[0], params[1]);
        LoginResponse response = serverFacade.login(request);
        authToken = response.authToken();
        currentUser = response.username();
        status = POSTLOGIN;
        System.out.println("Setting auth token to: " + authToken);
        return "You logged in as " + response.username();
    }

    public String register(String... params) throws Exception {
        assertSignedOut();
        RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResponse response = serverFacade.register(request);
        status = POSTLOGIN;
        currentUser = response.username();
        authToken = response.authToken();
        return "You logged in as " + response.username();
    }

    public String create(String... params) throws Exception {
        assertSignedIn();
        CreateGameResponse response = serverFacade.create(new CreateGameRequest(authToken, params[0]));
        return "You created game: " + params[0] + "with gameID " + response.gameID();
    }
    private void assertSignedIn() throws ResponseException {
        if (status != POSTLOGIN) {
            throw new ResponseException(400, "You must sign in");
        }
    }
    private void assertSignedOut() throws ResponseException{
        if(status != PRELOGIN){
            throw new ResponseException(400, "Cannot preform action, you are already signed in");
        }
    }
    private void asserInGame() throws ResponseException{
        if(status != GAMESTATUS){
            throw new ResponseException(400, "invalid command you are in a game.");
        }
    }
}
