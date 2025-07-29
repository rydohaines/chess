package repls;

import chess.ChessBoard;
import dataaccess.ResponseException;
import server.ServerFacade;
import service.responses.*;
import ui.BoardDrawer;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static repls.ClientStatus.*;
import static ui.EscapeSequences.*;

public class LoginClient implements Client {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final Repl repl;
    private ClientStatus status = PRELOGIN;
    private String authToken = null;
    private String currentUser = null;
    private Map<Integer,Integer> listGameMap = new HashMap<>();

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
        else{
            return """
                    quit - playing chess
                    help - see possible commands
                    """;
        }
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
                case"list" -> list(params);
                case "observe" -> observe(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String observe(String[] params) {
        drawBoard();
        return "here is the board";
    }

    public String list(String ... params) throws Exception {
        assertSignedIn();
        StringBuilder output = new StringBuilder();
        ListGamesResult result = serverFacade.list(authToken);
        Collection<ListGamesResponse> games = result.games();
        if(games.isEmpty()){
            return "No games created. Type 'help' to see commands";
        }
        int i = 1;
        for (ListGamesResponse game : games) {
            listGameMap.put(i, game.gameID());
            output.append(String.format(
                    "%d. Name: %s, White: %s, Black: %s%n",
                    i++,
                    game.gameName(),
                    game.whiteUsername() != null ? game.whiteUsername() : "none",
                    game.blackUsername() != null ? game.blackUsername() : "none"
            ));

        }
        return output.toString();
    }
    public String join(String ... params) throws Exception {
        assertSignedIn();
        int gameID;
        try {
            if(listGameMap.isEmpty()){
                throw new Exception("Please list games before trying to join");
            }
            gameID = listGameMap.get(Integer.parseInt(params[0]));
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Invalid game ID: please enter a valid number");
        }
                if (Objects.equals(params[1], "black")) {
                JoinGameRequest request = new JoinGameRequest(params[1],gameID, currentUser);
                serverFacade.join(request,authToken);
                this.drawBlackBoard();
                } else if (Objects.equals(params[1], "white")) {
                JoinGameRequest request = new JoinGameRequest(params[1],gameID, currentUser);
                serverFacade.join(request,authToken);
                    this.drawBoard();
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
        return "You created game: " + params[0] +" type 'list' to see all games";
    }
    private void assertSignedIn() throws ResponseException {
        if (status == PRELOGIN) {
            throw new ResponseException(400, "You must sign in");
        }
        if(status == GAMESTATUS){
            throw new ResponseException(400, "You are in a game please exit to preform command");
        }
    }
    private void assertSignedOut() throws ResponseException{
        if(status != PRELOGIN){
            throw new ResponseException(400, "Cannot preform action, you are already signed in");
        }
    }
    private void assertInGame() throws ResponseException{
        if(status != GAMESTATUS){
            throw new ResponseException(400, "invalid command you are in a game.");
        }
    }
    private void drawBoard(){
        BoardDrawer drawer = new BoardDrawer();
        drawer.drawStandardBoardWhite();
    }
    private void drawBlackBoard(){
        BoardDrawer drawer = new BoardDrawer();
        drawer.drawBoardBlack();
    }
}
