/**
 * Cell class is used to store X and Y cordinates
 * and the symbol of given cell in game area.
 *
 * @author Heikki Kangas
 * @version 2018.1204
 */
public class Cell {
    /** Y coordinate of the cell. */
    private int row;

    /** X coordinate of the cell. */
    private int col;

    /** Symbol of the cell. */
    private char symbol;

    /** Creates empty cell. */
    public Cell() {
        this.row = -1;
        this.col = -1;
    }

    /**
     * Creates cell with given coordinates.
     * @param row Y coordinate.
     * @param col X coordinate.
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Creates cell with given coordinates and symbol.
     * @param row Y coordinate.
     * @param col X coordinate.
     * @param symbol Symbol of the cell.
     */
    public Cell(int row, int col, char symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }

    /**
     * Returns the symbol of the cell.
     * @return char symbol of the cell.
     */
    public char getSymbol() {
        return symbol;
    }

    /**
     * Sets the given symbol to the cell.
     * @param symbol char to set to the cell.
     */
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the Y coordinate of the cell.
     * @return the Y coordinate of the cell.
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the X coordinate of the cell.
     * @return the X coordinate of the cell.
     */
    public int getCol() {
        return col;
    }

    /**
     * Sets the cell's Y coordinate to given int.
     * @param row new Y coordinate for the cell.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the cell's X coordinate to given int.
     * @param col new X coordinate for the cell.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Checks if the cell's coordinates are within the {@link GameLogic#gameArea}.
     * @return true if cell's coordinates are within the game area.
     */
    public boolean validateCell() {
        int rows = GameLogic.GameAreaSize.getRows();
        int columns = GameLogic.GameAreaSize.getColumns();
        if (row < 0 || row >= rows || col < 0 || col >= columns) {
            return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Cell) {
            Cell cell = (Cell) object;
            if (this.row == cell.getRow() && this.col == cell.getCol()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Row: " + row + "\tCol: " + col;
    }
}