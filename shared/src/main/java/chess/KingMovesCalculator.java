package chess;

import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
    Collection<ChessMove> validMoves = List.of();

    if(board.getPiece(newPosition)== null){
        validMoves.add(new ChessMove(position, newPosition));
    }
    }
}
