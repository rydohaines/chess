package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position );
    default Collection<ChessMove> getDirectionalMoves(
            ChessBoard board,
            ChessPosition position,
            ChessGame.TeamColor teamColor,
            List<Function<ChessPosition, ChessPosition>> directions){
        Collection<ChessMove> validMoves = new ArrayList<>();
        for(Function<ChessPosition,ChessPosition> direction : directions){
            ChessPosition endPos = direction.apply(position);
            while(endPos != null){
                if(board.getPiece(endPos) == null){
                    validMoves.add(new ChessMove(position,endPos,null));
                }else if(board.getPiece(endPos).getTeamColor() != teamColor){
                    validMoves.add(new ChessMove(position,endPos,null));
                    break;
                }
                else{
                    break;
                }
                endPos = direction.apply(endPos);
            }
        }
        return validMoves;
    }
}


