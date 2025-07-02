package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator{
    private final Collection<ChessMove> validMoves = new ArrayList<>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        ChessPosition upTwoRight = new ChessPosition((position.getRow()+2),(position.getColumn()+1));
        ChessPosition upTwoLeft = new ChessPosition((position.getRow()+2),(position.getColumn()-1));
        ChessPosition rightTwoUp = new ChessPosition((position.getRow()+1),(position.getColumn()+2));
        ChessPosition rightTwoDown = new ChessPosition((position.getRow()-1),(position.getColumn()+2));
        ChessPosition downTwoRight = new ChessPosition((position.getRow()-2),(position.getColumn()+1));
        ChessPosition downTwoLeft = new ChessPosition((position.getRow()-2),(position.getColumn()-1));
        ChessPosition leftTwoUp = new ChessPosition((position.getRow()+1),(position.getColumn()-2));
        ChessPosition leftTwoDown = new ChessPosition((position.getRow()-1),(position.getColumn()-2));
        List<ChessPosition> possibleMoves = List.of(
                upTwoLeft,upTwoRight,rightTwoDown,rightTwoUp,downTwoLeft,downTwoRight,leftTwoDown,leftTwoUp
        );
        for(ChessPosition endPos : possibleMoves){
            if(endPos.getRow() <=8 && endPos.getRow() >=1 && endPos.getColumn()<= 8 && endPos.getColumn() >=1){
                if (board.getPiece(endPos) == null || board.getPiece(endPos).getTeamColor() != pieceColor) {
                    validMoves.add(new ChessMove(position, endPos, null));
                }
            }
        }


        return validMoves;
    }
}