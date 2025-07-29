package repls;

import server.ServerFacade;
import service.responses.*;

import java.util.Arrays;

import static repls.ClientStatus.POSTLOGIN;
import static repls.ClientStatus.PRELOGIN;

public class LoginClient implements Client {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final Repl repl;
    private ClientStatus status = PRELOGIN;
    private String authToken = null;

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
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
    public String logout() throws Exception {
        LogoutRequest request = new LogoutRequest(authToken);
        serverFacade.logout(request);
        status = PRELOGIN;
        return "You have logged out. Type 'help' for more options";
    }

    public String login(String... params) throws Exception {
        LoginRequest request = new LoginRequest(params[0], params[1]);
        LoginResponse response = serverFacade.login(request);
        authToken = response.authToken();
        status = POSTLOGIN;
        System.out.println("Setting auth token to: " + authToken);
        return "You logged in as " + response.username();
    }

    public String register(String... params) throws Exception {
        RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResponse response = serverFacade.register(request);
        status = POSTLOGIN;
        authToken = response.authToken();
        System.out.println("Setting auth token to: " + response.authToken());
        return "You logged in as " + response.username();
    }

    public String create(String... params) throws Exception {
        System.out.println("auth token is: " + authToken);
        CreateGameResponse response = serverFacade.create(new CreateGameRequest(authToken, params[0]));
        return "You created game: " + params[0] + "with gameID " + response.gameID();
    }
}
