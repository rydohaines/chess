package repls;


import server.NotificationHandler;
import ui.BoardDrawer;
import websocket.messages.ServerMessage;

import java.util.Scanner;


import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final Client client;
    private final BoardDrawer drawer = new BoardDrawer();
    public Repl(String serverUrl) throws Exception {
        client = new LoginClient (serverUrl,this);
    }
    public void run(){
        System.out.println("\uD83D\uDC36 Welcome to the Chess Client. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_BG_COLOR_WHITE);
    }

    @Override
    public void notify(ServerMessage notification) {
        if (notification.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println(SET_TEXT_COLOR_RED + notification.getMessage());
        }
        else if(notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
           client.updateBoard(notification.getGame());
        }
        else if(notification.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
            System.out.println(SET_BG_COLOR_BLACK + SET_TEXT_COLOR_GREEN + notification.getErrorMessage());
        }
    }
}
