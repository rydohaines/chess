package chess;

import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
public ChessPiece[][] board;
    public ChessBoard() {
        ChessPiece[][] board = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     * MINOR CHANGE
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if(board[position.getRow()][position.getColumn()].getPieceType() == KING){
            return KING;
        }
        if(board[position.getRow()][position.getColumn()] == QUEEN){
            return QUEEN;
        }
        if(board[position.getRow()][position.getColumn()] == BISHOP){
            return BISHOP;
        }
        if(board[position.getRow()][position.getColumn()] == KNIGHT){
            return KNIGHT;
        }
        if(board[position.getRow()][position.getColumn()] == ROOK){
            return ROOK;
        }
        if(board[position.getRow()][position.getColumn()] == PAWN){
            return PAWN;
        }
        else{
            return NULL;
            }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
