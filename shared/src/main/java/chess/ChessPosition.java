package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
private int row;
private int col;
    public ChessPosition(int newRow, int newCol) {
    row = newRow;
    col = newCol;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }
    public ChessPosition positiveDiagonal() {
        if(row+1 > 8 || col+1 > 8 ){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row+1,col+1);
    }
    public ChessPosition negativeDiagonal(){
        if(row-1 < 1 || col-1 < 1){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row-1,col-1);
    }
    public ChessPosition leftUpDiagonal(){
        if(row+1 > 8 || col-1 <1){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row+1,col-1);
    }
    public ChessPosition rightDownDiagonal(){
        if(row - 1 < 1 || col+1 > 8){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row-1,col+1);
    }
    public ChessPosition upOne(){
        if(row + 1 > 8){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row+1,col);
    }
    public ChessPosition downOne(){
        if(row-1 < 1){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row-1,col);
    }
    public ChessPosition rightOne(){
        if(col+1 > 8){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row,col+1);

    }
    public ChessPosition leftOne(){
        if(col-1 < 1){
            return new ChessPosition(row,col);
        }
        return new ChessPosition(row,col-1);
    }
    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }
}
