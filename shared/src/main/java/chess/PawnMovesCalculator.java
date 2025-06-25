package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator{
    private Collection<ChessMove> validMoves = new ArrayList<>(0);

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        if(board.getPiece(position.positiveDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()){
            if(position.positiveDiagonal().getRow() == 8)
            else{ validMoves.add(new ChessMove(position,position.positiveDiagonal(),null)
        }
        }
        return validMoves;
    }
}
