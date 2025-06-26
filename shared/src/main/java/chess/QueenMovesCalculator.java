package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{
    private Collection<ChessMove> validMoves = new ArrayList<>(0);
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ChessPosition incrementer = position.positiveDiagonal();
        while(incrementer != null){
            if(board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if(board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()){
                validMoves.add(new ChessMove(position,incrementer,null));
                break;
            }
            else break;
            incrementer = incrementer.positiveDiagonal();
        }
        incrementer = position.negativeDiagonal();
        while(incrementer != null){
            if(board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if(board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()){
                validMoves.add(new ChessMove(position,incrementer,null));
                break;
            }
            else break;
            incrementer = incrementer.negativeDiagonal();
        }
        incrementer = position.leftUpDiagonal();
        while(incrementer != null){
            if(board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if(board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()){
                validMoves.add(new ChessMove(position,incrementer,null));
                break;
            }
            else break;
            incrementer = incrementer.leftUpDiagonal();
        }
        incrementer = position.rightDownDiagonal();
        while(incrementer != null){
            if(board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if(board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()){
                validMoves.add(new ChessMove(position,incrementer,null));
                break;
            }
            else break;
            incrementer = incrementer.rightDownDiagonal();
        }
        incrementer = position.upOne();
        while(incrementer != null){
            if(board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if(board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()){
                validMoves.add(new ChessMove(position,incrementer,null));
                break;
            }
            else break;
            incrementer = incrementer.upOne();
        }
        incrementer = position.downOne();
        while(incrementer != null) {
            if (board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if (board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()) {
                validMoves.add(new ChessMove(position, incrementer, null));
                break;
            }
            else break;
            incrementer = incrementer.downOne();
        }
        incrementer = position.rightOne();
        while(incrementer != null) {
            if (board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if (board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()) {
                validMoves.add(new ChessMove(position, incrementer, null));
                break;
            }
            else break;
            incrementer = incrementer.rightOne();
        }
        incrementer = position.leftOne();
        while(incrementer != null) {
            if (board.getPiece(incrementer) == null) {
                validMoves.add(new ChessMove(position, incrementer, null));
            }
            else if (board.getPiece(incrementer).getTeamColor() != board.getPiece(position).getTeamColor()) {
                validMoves.add(new ChessMove(position, incrementer, null));
                break;
            }
            else break;
            incrementer = incrementer.leftOne();
        }
        return validMoves;
    }
}
