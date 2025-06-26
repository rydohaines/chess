package chess;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
private PieceType type;
private ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor newPieceColor, ChessPiece.PieceType newType) {
    type = newType;
    pieceColor = newPieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN,
        NULL
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if(type == PieceType.KING){
            PieceMovesCalculator calc = new KingMovesCalculator();
            return calc.pieceMoves(board,myPosition);
        }
        if(type == PieceType.PAWN){
            PieceMovesCalculator pawn = new PawnMovesCalculator();
            return pawn.pieceMoves(board,myPosition);
        }
        if(type == PieceType.KNIGHT){
            PieceMovesCalculator knight = new KnightMovesCalculator();
            return knight.pieceMoves(board,myPosition);
        }
        if(type == PieceType.ROOK){
            PieceMovesCalculator rook = new RookMovesCalculator();
            return rook.pieceMoves(board,myPosition);
        }
        else return Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(pieceColor);
        return result;
    }
}
