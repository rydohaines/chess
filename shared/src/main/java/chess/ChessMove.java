package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private final ChessPosition startingPos;
    private final ChessPosition endingPos;
    private final ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType tempPromotionPiece) {
    startingPos = startPosition;
    endingPos = endPosition;
    promotionPiece = tempPromotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startingPos;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endingPos;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startingPos, chessMove.startingPos) &&
                Objects.equals(endingPos, chessMove.endingPos) && promotionPiece == chessMove.promotionPiece;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(startingPos);
        result = 31 * result + Objects.hashCode(endingPos);
        result = 31 * result + Objects.hashCode(promotionPiece);
        return result;
    }
}
