package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator{
    private final Collection<ChessMove> validMoves = new ArrayList<>();
    public Collection<ChessMove> promotionMoves(ChessPosition start, ChessPosition end){
        Collection<ChessMove> promoMoves = new ArrayList<>();
        promoMoves.add(new ChessMove(start,end, ChessPiece.PieceType.QUEEN));
        promoMoves.add(new ChessMove(start,end, ChessPiece.PieceType.KNIGHT));
        promoMoves.add(new ChessMove(start,end, ChessPiece.PieceType.BISHOP));
        promoMoves.add(new ChessMove(start,end, ChessPiece.PieceType.ROOK));
        return promoMoves;
    }
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
                                validMoves.addAll(promotionMoves(position,endingPos));
                            }
                            else {
                                validMoves.add(new ChessMove(position,endingPos,null));
                            }
                }
            }
            if(position.upOne()!= null){
                if(board.getPiece(position.upOne()) == null){
                    if(position.upOne().getRow() == 8){
                        validMoves.addAll(promotionMoves(position,position.upOne()));
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
                                validMoves.addAll(promotionMoves(position,endingPos));
                            }
                            else{
                                validMoves.add(new ChessMove(position,endingPos,null));
                            }
                }
            }
            if(position.downOne()!= null){
                if(board.getPiece(position.downOne()) == null){
                    if(position.downOne().getRow() == 1){
                        validMoves.addAll(promotionMoves(position,position.downOne()));
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
