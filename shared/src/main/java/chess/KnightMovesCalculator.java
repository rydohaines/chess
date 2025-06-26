package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator{
    private Collection<ChessMove> validMoves = new ArrayList<>(0);
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        if(position.getRow()+2 <= 8){
            if(position.getColumn()+1 <= 8){
                ChessPosition movePos = new ChessPosition(position.getRow()+2, position.getColumn()+1 );
                if(board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()) {

                    validMoves.add(new ChessMove(position, movePos, null));
                }
            }
            if(position.getColumn() - 1 >= 1) {
                ChessPosition movePos = new ChessPosition(position.getRow() + 2, position.getColumn() - 1);
                if (board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()){
                validMoves.add(new ChessMove(position, movePos, null));
            }
            }

        }
        if(position.getRow()-2 >= 1){
            if(position.getColumn()+1 <= 8){
                ChessPosition movePos = new ChessPosition(position.getRow()-2,position.getColumn()+1);
                if (board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    validMoves.add(new ChessMove(position, movePos, null));
                }
            }
            if(position.getColumn() - 1 >= 1){
                ChessPosition movePos = new ChessPosition(position.getRow()-2,position.getColumn()-1);
                if (board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    validMoves.add(new ChessMove(position, movePos, null));
                }
            }
        }
        if(position.getColumn()+2 <=8){
            if(position.getRow()+1 <= 8){
                ChessPosition movePos = new ChessPosition(position.getRow()+1,position.getColumn()+2);
                if (board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    validMoves.add(new ChessMove(position, movePos, null));
                }
            }
            if(position.getRow()-1 >= 1){
                ChessPosition movePos = new ChessPosition(position.getRow()-1,position.getColumn()+2);
                if (board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    validMoves.add(new ChessMove(position, movePos, null));
                }
            }
        }
        if(position.getColumn()-2 >= 1){
            if(position.getRow()+1 <= 8){
                ChessPosition movePos = new ChessPosition(position.getRow()+1,position.getColumn()-2);
                if (board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    validMoves.add(new ChessMove(position, movePos, null));
                }
            }
            if(position.getRow()-1 >= 1){
                ChessPosition movePos = new ChessPosition(position.getRow()-1,position.getColumn()-2);
                if (board.getPiece(movePos) == null || board.getPiece(movePos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    validMoves.add(new ChessMove(position, movePos, null));
                }
            }
        }
        return validMoves;
    }
}
