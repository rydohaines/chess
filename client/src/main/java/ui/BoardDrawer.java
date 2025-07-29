package ui;


import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    public void drawStandardBoardWhite(){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        String[] headers = { "a", "b", "c","d","e","f","g","h"};
        drawHeaders(out,headers);
        String[] numbers = {"8","7","6","5","4","3","2","1"};
        drawChessBoard(out, numbers,0);
        drawHeaders(out,headers);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
    public void drawBoardBlack(){
        var out = new PrintStream(System.out,true,StandardCharsets.UTF_8);
        String[] headers = { "h","g","f","e","d","c","b","a"};
        drawHeaders(out,headers);
        String[] numbers = {"8","7","6","5","4","3","2","1"};
        drawChessBoard(out,numbers,1);
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

    private static void drawChessBoard(PrintStream out, String[] numbers, int mod) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_DARK_GREY);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            out.print(numbers[boardRow]);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            drawRowOfSquares(out,boardRow,mod);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(SET_TEXT_COLOR_DARK_GREY);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            out.print(numbers[boardRow]);
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS/2));
            setBlack(out);
            out.println();
        }
    }

    private static void drawRowOfSquares(PrintStream out,int indexRow, int mod) {
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if((boardCol+indexRow) %2 ==mod){
                    out.print(SET_BG_COLOR_WHITE);
                    printPiece(out,indexRow,boardCol,mod);
                }
                else {
                    out.print(SET_BG_COLOR_BLACK);
                    printPiece(out,indexRow,boardCol,mod);
                }
            }
            setWhite(out);
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
            {WHITE_ROOK,WHITE_KNIGHT,WHITE_BISHOP,WHITE_QUEEN,WHITE_KING,WHITE_BISHOP,WHITE_KNIGHT,WHITE_ROOK}, // Black major pieces
            {WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN,WHITE_PAWN}, // Black pawns
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN,BLACK_PAWN}, // White pawns
            {BLACK_ROOK,BLACK_KNIGHT,BLACK_BISHOP,BLACK_QUEEN,BLACK_KING,BLACK_BISHOP,BLACK_KNIGHT,BLACK_ROOK}, // White major pieces
    };
    private static void printPiece(PrintStream out, int row, int col,int mod){
        String piece;

        if (mod == 0) {
            piece = INITIAL_PIECES_WHITE[row][col];
        } else {
            piece = INITIAL_PIECES_BLACK[row][col];
        }
        if(piece == null){
            out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        }
        else if( mod ==0 && row < 2){
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(piece);
        }
        else if(mod == 1 && row < 2){
            out.print(SET_TEXT_COLOR_RED);
            out.print(piece);
        }
        else if(mod == 0){
            out.print(SET_TEXT_COLOR_RED);
            out.print(piece);
        }
        else{
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(piece);
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_RED);
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
