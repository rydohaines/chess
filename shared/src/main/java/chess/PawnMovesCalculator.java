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
            if (position.positiveDiagonal() != null) {
                if (board.getPiece(position.positiveDiagonal()) != null) {
                    if (board.getPiece(position.positiveDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.positiveDiagonal().getRow() == 8) {
                            validMoves.add(new ChessMove(position, position.positiveDiagonal(), ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, position.positiveDiagonal(), ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, position.positiveDiagonal(), ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, position.positiveDiagonal(), ChessPiece.PieceType.BISHOP));
                        } else {
                            validMoves.add(new ChessMove(position, position.positiveDiagonal(), null));
                        }
                    }
                }
            }
            if(position.leftUpDiagonal() != null) {
                if (board.getPiece(position.leftUpDiagonal()) != null) {
                    if (board.getPiece(position.leftUpDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.leftUpDiagonal().getRow() == 8) {
                            validMoves.add(new ChessMove(position, position.leftUpDiagonal(), ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, position.leftUpDiagonal(), ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, position.leftUpDiagonal(), ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, position.leftUpDiagonal(), ChessPiece.PieceType.BISHOP));
                        } else {
                            validMoves.add(new ChessMove(position, position.leftUpDiagonal(), null));
                        }
                    }
                }
            }
            if(position.upOne() != null) {
                if (board.getPiece(position.upOne()) == null) {
                    if(position.upOne().getRow() == 8 ){
                        validMoves.add(new ChessMove(position,position.upOne(), ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, position.upOne(), ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, position.upOne(), ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(position, position.upOne(), ChessPiece.PieceType.BISHOP));
                    }
                    else {
                        validMoves.add(new ChessMove(position, position.upOne(), null));
                    }
                    if (position.getRow() == 2 && board.getPiece(position.upOne().upOne()) == null && position.upOne().getRow() != position.upOne().upOne().getRow()) {
                        validMoves.add(new ChessMove(position, position.upOne().upOne(), null));
                    }
                }
            }
        }

        else{
            if(position.negativeDiagonal() != null) {
                if (board.getPiece(position.negativeDiagonal()) != null) {
                    if (board.getPiece(position.negativeDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.negativeDiagonal().getRow() == 1) {
                            validMoves.add(new ChessMove(position, position.negativeDiagonal(), ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, position.negativeDiagonal(), ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, position.negativeDiagonal(), ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, position.negativeDiagonal(), ChessPiece.PieceType.BISHOP));
                        } else {
                            validMoves.add(new ChessMove(position, position.negativeDiagonal(), null));
                        }
                    }
                }
            }
            if(position.rightDownDiagonal()!= null) {
                if (board.getPiece(position.rightDownDiagonal()) != null) {
                    if (board.getPiece(position.rightDownDiagonal()).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (position.rightDownDiagonal().getRow() == 1) {
                            validMoves.add(new ChessMove(position, position.rightDownDiagonal(), ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, position.rightDownDiagonal(), ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, position.rightDownDiagonal(), ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, position.rightDownDiagonal(), ChessPiece.PieceType.BISHOP));
                        } else {
                            validMoves.add(new ChessMove(position, position.rightDownDiagonal(), null));
                        }
                    }
                }
            }
            if(position.downOne() != null) {
                if (board.getPiece(position.downOne()) == null) {
                    if(position.downOne().getRow() == 1 ){
                        validMoves.add(new ChessMove(position,position.downOne(), ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, position.downOne(), ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, position.downOne(), ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(position, position.downOne(), ChessPiece.PieceType.BISHOP));
                    }else {
                        validMoves.add(new ChessMove(position, position.downOne(), null));
                    }
                    if (position.getRow() == 7 && board.getPiece(position.downOne().downOne()) == null && position.downOne().getRow() != position.downOne().downOne().getRow()) {
                        validMoves.add(new ChessMove(position, position.downOne().downOne(), null));
                    }
                }
            }

        }
        return validMoves;
    }
}
