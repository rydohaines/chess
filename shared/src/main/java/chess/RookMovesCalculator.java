package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class RookMovesCalculator implements PieceMovesCalculator{
    private final Collection<ChessMove> validMoves = new ArrayList<>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        List<Function<ChessPosition,ChessPosition>> directions = List.of(
                ChessPosition::upOne,ChessPosition::downOne,ChessPosition::rightOne,ChessPosition::leftOne
        );
        for(Function<ChessPosition,ChessPosition> direction : directions){
            ChessPosition endPos = direction.apply(position);
            while(endPos != null){
                if(board.getPiece(endPos) == null){
                    validMoves.add(new ChessMove(position,endPos,null));
                }else if(board.getPiece(endPos).getTeamColor() != pieceColor){
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

