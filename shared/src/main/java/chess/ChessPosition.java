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
        return new ChessPosition(row+1,col+1);
    }
    public ChessPosition negativeDiagonal(){
        return new ChessPosition(row-1,col-1);
    }
    public ChessPosition leftUpDiagonal(){
        return new ChessPosition(row+1,col-1);
    }
    public ChessPosition rightDownDiagonal(){
        return new ChessPosition(row-1,col+1);
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
