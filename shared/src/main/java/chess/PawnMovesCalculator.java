package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    private final Collection<ChessMove> validMoves = new ArrayList<>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor pieceColor = board.getPiece(position).getTeamColor();
        if(pieceColor == ChessGame.TeamColor.WHITE){
            ChessPosition[] endPos = {
                    position.positiveDiagonal(),position.leftUpDiagonal()
            };
            for(ChessPosition endingPos : endPos){
                if(endingPos != null && board.getPiece(endingPos) != null && board.getPiece(endingPos).getTeamColor() != pieceColor){
                            if(endingPos.getRow() == 8){
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.QUEEN));
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.KNIGHT));
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.BISHOP));
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.ROOK));
                            }
                            else {
                                validMoves.add(new ChessMove(position,endingPos,null));
                            }
                }
            }
            if(position.upOne()!= null){
                if(board.getPiece(position.upOne()) == null){
                    if(position.upOne().getRow() == 8){
                        validMoves.add(new ChessMove(position,position.upOne(), ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position,position.upOne(), ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(position,position.upOne(), ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position,position.upOne(), ChessPiece.PieceType.ROOK));
                    }else {
                        validMoves.add(new ChessMove(position,position.upOne(),null));
                    }
                    if(position.getRow() == 2 && board.getPiece(position.upOne().upOne()) == null){
                        validMoves.add(new ChessMove(position,position.upOne().upOne(),null));
                    }
                }
            }
        }
        else {
            ChessPosition[] endPos = {
                    position.negativeDiagonal(),position.rightDownDiagonal()
            };
            for(ChessPosition endingPos : endPos){
                if(endingPos != null && board.getPiece(endingPos) != null && board.getPiece(endingPos).getTeamColor() != pieceColor){
                            if(endingPos.getRow() == 1){
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.QUEEN));
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.KNIGHT));
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.BISHOP));
                                validMoves.add(new ChessMove(position,endingPos, ChessPiece.PieceType.ROOK));
                            }
                            else{
                                validMoves.add(new ChessMove(position,endingPos,null));
                            }
                }
            }
            if(position.downOne()!= null){
                if(board.getPiece(position.downOne()) == null){
                    if(position.downOne().getRow() == 1){
                        validMoves.add(new ChessMove(position,position.downOne(), ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position,position.downOne(), ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(position,position.downOne(), ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position,position.downOne(), ChessPiece.PieceType.ROOK));
                    }else{
                        validMoves.add(new ChessMove(position,position.downOne(),null));
                    }
                    if(position.getRow() == 7 && board.getPiece(position.downOne().downOne()) == null){
                        validMoves.add(new ChessMove(position,position.downOne().downOne(),null));
                    }
                }
            }
        }
        return validMoves;
    }
}
