package ui;


import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.PAWN;
import static chess.ChessPiece.PieceType.ROOK;
import static ui.EscapeSequences.*;

public class BoardDrawer {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    public void drawStandardBoardWhite(ChessBoard board){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        out.println();
        String[] headers = { "a", "b", "c","d","e","f","g","h"};
        drawHeaders(out,headers);
        String[] numbers = {"8","7","6","5","4","3","2","1"};
        drawChessBoard(out, numbers,0,updateWhiteBoard(board));
        drawHeaders(out,headers);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    public void drawBoardBlack(ChessBoard board){
        var out = new PrintStream(System.out,true,StandardCharsets.UTF_8);
        String[] headers = { "h","g","f","e","d","c","b","a"};
        drawHeaders(out,headers);
        String[] numbers = {"1","2","3","4","5","6","7","8"};
        drawChessBoard(out,numbers,1, updateblackBoard(board));
        drawHeaders(out,headers);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    private static void drawHeaders(PrintStream out,String[] headers) {

        setGrey(out);
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, headers[boardCol]);
        }
        out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        setBlack(out);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(EMPTY);
        printHeaderText(out, headerText);
        out.print(EMPTY);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_DARK_GREY);

        out.print(player);

        setGrey(out);
    }

    private static void drawChessBoard(PrintStream out, String[] numbers, int mod,ChessPiece[][] board) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_DARK_GREY);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            out.print(numbers[boardRow]);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            drawRowOfSquares(out,boardRow,mod,board);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_DARK_GREY);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            out.print(numbers[boardRow]);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            setBlack(out);
            out.println();
        }
    }

    private static void drawRowOfSquares(PrintStream out,int indexRow, int mod,ChessPiece[][] board) {
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if((boardCol+indexRow) %2 ==0){
                    out.print(SET_BG_COLOR_WHITE);
                    printPiece(out,indexRow,boardCol,mod,board);
                }
                else {
                    out.print(SET_BG_COLOR_BLACK);
                    printPiece(out,indexRow,boardCol,mod,board);
                }
            }
            setWhite(out);
    }
    private ChessPiece[][] updateWhiteBoard(ChessBoard board){
        ChessPiece[][] resultBoard = new ChessPiece[8][8];
        for(int i = 8; i >=1; --i){
            for(int j = 1; j <= BOARD_SIZE_IN_SQUARES;++j){
                resultBoard[8-i][j-1] = board.getPiece(new ChessPosition(i,j));
            }
        }
        return resultBoard;
    }
    private ChessPiece[][] updateblackBoard(ChessBoard board){
        ChessPiece[][] resultBoard = new ChessPiece[8][8];
        for(int i =1; i <=8; ++i){
            for(int j = 8; j>=1; --j){
                resultBoard[i-1][8-j]= board.getPiece(new ChessPosition(i,j));
            }
        }
        return resultBoard;
    }

    private static String toString(ChessPiece piece) {
        if (piece == null) return "   ";
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();
        return switch (type) {
            case PAWN -> (color == WHITE) ? WHITE_PAWN : BLACK_PAWN;
            case ROOK -> (color == WHITE) ? WHITE_ROOK : BLACK_ROOK;
            case KNIGHT -> (color == WHITE) ? WHITE_KNIGHT : BLACK_KNIGHT;
            case BISHOP -> (color == WHITE) ? WHITE_BISHOP : BLACK_BISHOP;
            case QUEEN -> (color == WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
            case KING -> (color == WHITE) ? WHITE_KING : BLACK_KING;
            default -> null;
        };
    }

    private static final String[][] INITIAL_PIECES_WHITE = {
            {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_QUEEN,BLACK_KING,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK}, // Black major pieces
            {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN}, // Black pawns
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN}, // White pawns
            {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_QUEEN,WHITE_KING,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK}, // White major pieces
    };
    private static final String[][] INITIAL_PIECES_BLACK = {
            {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_KING,WHITE_QUEEN,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK}, // Black major pieces
            {WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN}, // Black pawns
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN}, // White pawns
            {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_KING,BLACK_QUEEN,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK}, // White major pieces
    };
    private static void printPiece(PrintStream out, int row, int col, int mod, ChessPiece[][] board){
        String piece = toString(board[row][col]);
        if(board[row][col] != null) {
            if (board[row][col].getTeamColor() == BLACK) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(piece);
            } else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(piece);
            }
        }
        else {
            out.print(piece);
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }


    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static void setGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

}
