package chess;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        List<Function<ChessPosition,ChessPosition>> moves = List.of(
                ChessPosition::upOne,ChessPosition::downOne,ChessPosition::rightOne,ChessPosition::leftOne
        );
        ChessGame.TeamColor teamColor = board.getPiece(position).getTeamColor();
        return getDirectionalMoves(board,position,teamColor,moves);
    }
}

