package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
private  ChessBoard gameBoard = new ChessBoard();
private TeamColor turnColor;
private boolean gameComplete = false;
    public ChessGame() {
    gameBoard.resetBoard();
    setTeamTurn(TeamColor.WHITE);
    }
    public void completeGame(){
        gameComplete = true;
    }
    public boolean isGameComplete(){
        return gameComplete;
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn(){
        return turnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece currPiece = gameBoard.getPiece(startPosition);
        TeamColor pieceColor = currPiece.getTeamColor();
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> pieceMoves = currPiece.pieceMoves(gameBoard, startPosition);
        for (ChessMove move : pieceMoves) {
            ChessBoard tempBoard = gameBoard.deepCopy();
            if(move.getPromotionPiece() != null){
                gameBoard.addPiece(move.getEndPosition(),new ChessPiece(pieceColor,move.getPromotionPiece()));
            }else {
                gameBoard.addPiece(move.getEndPosition(), currPiece);
            }
            gameBoard.setNull(move.getStartPosition());
            if (!isInCheck(pieceColor)) {
                validMoves.add(move);
            }
            setBoard(tempBoard);
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(gameBoard.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException();
        }
        ChessPiece currPiece = gameBoard.getPiece(move.getStartPosition());
        TeamColor pieceColor = currPiece.getTeamColor();
        if(pieceColor != turnColor){
            throw new InvalidMoveException();
        }
        if(!validMoves(move.getStartPosition()).contains(move)){
            throw new InvalidMoveException();
        }
        if(move.getPromotionPiece() != null){
            gameBoard.addPiece(move.getEndPosition(),new ChessPiece(pieceColor,move.getPromotionPiece()));
        }else {
            gameBoard.addPiece(move.getEndPosition(), currPiece);
        }
        gameBoard.setNull(move.getStartPosition());
        if(turnColor == TeamColor.WHITE){
            setTeamTurn(TeamColor.BLACK);
        }else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor,gameBoard);
        Collection<ChessMove> opponentMoves = new ArrayList<>();
        for(int i = 1; i <=8; ++i){
            for(int j = 1; j<=8; ++j){
                ChessPosition currPos = new ChessPosition(i,j);
                ChessPiece piece = gameBoard.getPiece(currPos);
                if(piece != null && piece.getTeamColor() != teamColor){
                   Collection<ChessMove> moves = piece.pieceMoves(gameBoard,currPos);
                   if(moves != null){
                       opponentMoves.addAll(moves);
                   }
                }
            }
        }
        for(ChessMove move : opponentMoves){
            if(move.getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;
    }

    public ChessPosition findKingPosition(TeamColor teamColor, ChessBoard board){
        for(int i = 1; i <= 8; ++i){
            for(int j = 1; j <=8; ++j){
                if(board.getPiece(new ChessPosition(i,j)) != null){
                    ChessPosition currPos = new ChessPosition(i,j);
                    ChessPiece piece = board.getPiece(currPos);
                    if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor()==teamColor ) {
                            return new ChessPosition(i, j);
                    }
                }
            }
        }
        return null;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> validMoves = allValidMoves(teamColor);
        return validMoves.isEmpty() && isInCheck(teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> validMoves = allValidMoves(teamColor);
        return validMoves.isEmpty() && !isInCheck(teamColor);
    }
    Collection<ChessMove> allValidMoves(TeamColor teamColor){
        Collection<ChessMove> validMoves = new ArrayList<>();
        for(int i = 1; i<=8; ++i){
            for(int j = 1; j<= 8; ++j){
                ChessPosition currPos = new ChessPosition(i,j);
                ChessPiece currPiece = gameBoard.getPiece(currPos);
                if(currPiece != null && currPiece.getTeamColor()== teamColor){
                    validMoves.addAll(this.validMoves(currPos));
                }
            }
        }
        return validMoves;
    }
    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(gameBoard, chessGame.gameBoard) && turnColor == chessGame.turnColor;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(gameBoard);
        result = 31 * result + Objects.hashCode(turnColor);
        return result;
    }
}
