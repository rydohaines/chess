package chess;

import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator{
    private Collection<ChessMove> validMoves;
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
    ChessPosition forwardOne = new ChessPosition(position.getRow()+1, position.getColumn());
    ChessPosition backwardsOne = new ChessPosition(position.getRow()-1, position.getColumn());
    ChessPosition rightOne = new ChessPosition(position.getRow(), position.getColumn()+1);
    ChessPosition leftOne = new ChessPosition(position.getRow(), position.getColumn()-1);
    if(board.getPiece(position.positiveDiagonal()) == null || board.getPiece(position.positiveDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()){
            validMoves.add(new ChessMove(position, position.positiveDiagonal(),null))
    }
    if(board.getPiece(position.negativeDiagonal())){

    }
    if(board.getPiece(position.leftUpDiagonal())){

    }
    if(board.getPiece(position.rightDownDiagonal())){

    }
    if(board.getPiece(forwardOne) == null){
        validMoves.add(new ChessMove(position, forwardOne,null));
    }
    }
}
