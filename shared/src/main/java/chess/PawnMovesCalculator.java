package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.ChessGame.TeamColor.WHITE;

public class PawnMovesCalculator implements PieceMovesCalculator{
    private Collection<ChessMove> validMoves = new ArrayList<>(0);

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        if(board.getPiece(position).getTeamColor() == WHITE) {
            if (board.getPiece(position.positiveDiagonal()) != null) {
                if (board.getPiece(position.positiveDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    if (position.positiveDiagonal().getRow() == 8) {
                        validMoves.add(new ChessMove(position, position.positiveDiagonal(), ChessPiece.PieceType.QUEEN));
                    } else {
                        validMoves.add(new ChessMove(position, position.positiveDiagonal(), null));
                    }
                }
            }
            if(board.getPiece(position.leftUpDiagonal()) != null) {
                if (board.getPiece(position.leftUpDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    if (position.leftUpDiagonal().getRow() == 8) {
                        validMoves.add(new ChessMove(position, position.leftUpDiagonal(), ChessPiece.PieceType.QUEEN));
                    } else {
                        validMoves.add(new ChessMove(position, position.leftUpDiagonal(), null));
                    }
                }
            }
            if(board.getPiece(position.upOne()) == null) {
                validMoves.add(new ChessMove(position, position.upOne(), null));

                if (position.getRow() == 2 && board.getPiece(position.upOne().upOne()) == null && position.upOne().getRow() != position.upOne().upOne().getRow()) {
                    validMoves.add(new ChessMove(position, position.upOne().upOne(), null));
                }
            }
        }

        else{
            if(board.getPiece(position.negativeDiagonal()) != null) {
                if (board.getPiece(position.negativeDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    if (position.negativeDiagonal().getRow() == 8) {
                        validMoves.add(new ChessMove(position, position.negativeDiagonal(), ChessPiece.PieceType.QUEEN));
                    } else {
                        validMoves.add(new ChessMove(position, position.negativeDiagonal(), null));
                    }
                }
            }
            if(board.getPiece(position.rightDownDiagonal()) != null) {
                if (board.getPiece(position.rightDownDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    if (position.rightDownDiagonal().getRow() == 8) {
                        validMoves.add(new ChessMove(position, position.rightDownDiagonal(), ChessPiece.PieceType.QUEEN));
                    } else {
                        validMoves.add(new ChessMove(position, position.rightDownDiagonal(), null));
                    }
                }
            }
            if(board.getPiece(position.downOne()) == null) {
                validMoves.add(new ChessMove(position, position.downOne(), null));
                if (position.getRow() == 7 && board.getPiece(position.downOne().downOne()) == null && position.downOne().getRow() != position.downOne().downOne().getRow()) {
                    validMoves.add(new ChessMove(position, position.downOne().downOne(), null));
                }
            }

        }
        return validMoves;
    }
}
