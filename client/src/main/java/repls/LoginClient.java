package repls;

import responses.*;
import server.ServerFacade;
import ui.BoardDrawer;


import java.util.*;

import static repls.ClientStatus.*;

public class LoginClient implements Client {
    private final ServerFacade serverFacade;
    private final Repl repl;
    private ClientStatus status = PRELOGIN;
    private String authToken = null;
    private String currentUser = null;
    private final Map<Integer,Integer> listGameMap = new HashMap<>();

    public LoginClient(String serverUrl, Repl repl) {
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

    private String observe(String[] params) throws Exception {
        assertSignedIn();
        if(params.length != 1){
            throw new Exception("invalid input, try again.");
        }
        try {
            int gameID = Integer.parseInt(params[0]);
            if(listGameMap.isEmpty()){
                throw new Exception("Please list games before trying to join");
            }
            gameID = listGameMap.get(Integer.parseInt(params[0]));
        } catch (Exception e) {
            throw new Exception("Invalid game ID: please enter a valid number");
        }
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
        if(params.length != 2){
            throw new Exception("invalid input, try again.");
        }
        int gameID;
        try {
            if(listGameMap.isEmpty()){
                throw new Exception("Please list games before trying to join");
            }
            gameID = listGameMap.get(Integer.parseInt(params[0]));
        } catch (Exception e) {
            throw new Exception("Invalid game ID: please enter a valid number");
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
                throw new Exception("please enter 'WHITE' or 'BLACK'");
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
        if(params.length != 2){
            throw new Exception("invalid input, try again.");
        }
        LoginRequest request = new LoginRequest(params[0], params[1]);
        LoginResponse response = serverFacade.login(request);
        authToken = response.authToken();
        currentUser = response.username();
        status = POSTLOGIN;
        return "You logged in as " + response.username();
    }

    public String register(String... params) throws Exception {
        assertSignedOut();
        if(params.length != 3){
            throw new Exception("invalid input, try again.");
        }
        RegisterRequest request = new RegisterRequest(params[0], params[1], params[2]);
        RegisterResponse response = serverFacade.register(request);
        status = POSTLOGIN;
        currentUser = response.username();
        authToken = response.authToken();
        return "You logged in as " + response.username();
    }

    public String create(String... params) throws Exception {
        assertSignedIn();
        if(params.length != 1){
            throw new Exception("invalid input, try again.");
        }
        CreateGameResponse response = serverFacade.create(new CreateGameRequest(authToken, params[0]));
        return "You created game: " + params[0] +" type 'list' to see all games";
    }
    private void assertSignedIn() throws Exception {
        if (status == PRELOGIN) {
            throw new Exception( "You must sign in");
        }
        if(status == GAMESTATUS){
            throw new Exception("You are in a game please exit to preform command");
        }
    }
    private void assertSignedOut() throws Exception{
        if(status != PRELOGIN){
            throw new Exception("Cannot preform action, you are already signed in");
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
