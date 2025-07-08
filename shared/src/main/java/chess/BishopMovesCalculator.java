package chess;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class BishopMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        List<Function<ChessPosition,ChessPosition>> directions = List.of(
                ChessPosition::leftUpDiagonal,ChessPosition::positiveDiagonal,ChessPosition::negativeDiagonal,ChessPosition::rightDownDiagonal
        );
        return getDirectionalMoves(board,position,pieceColor,directions);
    }
}
