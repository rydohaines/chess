package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator{
    private Collection<ChessMove> validMoves = new ArrayList<>(0);
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
    ChessPosition forwardOne = position.upOne();
    ChessPosition backwardsOne = position.downOne();
    ChessPosition leftOne = position.leftOne();
    ChessPosition rightOne = position.rightOne();
    ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
    if(position.positiveDiagonal() != null) {
        if (board.getPiece(position.positiveDiagonal()) == null || board.getPiece(position.positiveDiagonal()).getTeamColor() != pieceColor) {
            validMoves.add(new ChessMove(position, position.positiveDiagonal(), null));
        }
    }
    if(position.negativeDiagonal() != null) {
        if (board.getPiece(position.negativeDiagonal()) == null || board.getPiece(position.negativeDiagonal()).getTeamColor() != pieceColor) {
            validMoves.add(new ChessMove(position, position.negativeDiagonal(), null));
        }
    }
    if(position.leftUpDiagonal()!= null) {
        if (board.getPiece(position.leftUpDiagonal()) == null || board.getPiece(position.leftUpDiagonal()).getTeamColor() != pieceColor) {
            validMoves.add(new ChessMove(position, position.leftUpDiagonal(), null));
        }
    }
    if(position.rightDownDiagonal()!= null) {
        if (board.getPiece(position.rightDownDiagonal()) == null || (board.getPiece(position.rightDownDiagonal()).getTeamColor() != pieceColor)) {
            validMoves.add(new ChessMove(position, position.rightDownDiagonal(), null));
        }
    }
    if(forwardOne != null) {
        if (board.getPiece(forwardOne) == null || board.getPiece(forwardOne).getTeamColor() != pieceColor) {
            validMoves.add(new ChessMove(position, forwardOne, null));
        }
    }
    if(backwardsOne != null) {
        if (board.getPiece(backwardsOne) == null || board.getPiece(backwardsOne).getTeamColor() != pieceColor) {
            validMoves.add(new ChessMove(position, backwardsOne, null));
        }
    }
    if(rightOne != null) {
        if (board.getPiece(rightOne) == null || board.getPiece(rightOne).getTeamColor() != pieceColor) {
            validMoves.add(new ChessMove(position, rightOne, null));
        }
    }
    if(leftOne != null) {
        if (board.getPiece(leftOne) == null || board.getPiece(leftOne).getTeamColor() != board.getPiece(position).getTeamColor()) {
            validMoves.add(new ChessMove(position, leftOne, null));
        }
    }
    return validMoves;
    }
}
