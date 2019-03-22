import java.util.ArrayList;

/**
 * The backend class for controlling the game area's state.
 *
 * @author Heikki Kangas
 * @version 2018.1213
 */
public class GameLogic {
    /** Constant for player's character in the game area. */
    private static final char PLAYERCHAR = 'X';

    /** Constant for computer's character in the game area. */
    private static final char COMPUTERCHAR = 'O';

    /** Constant for empty character in the game area. */
    private static final char EMPTYCHAR = ' ';

    /** 2D array representing the game grid. */
    private static char[][] gameArea;

    /** How many rounds have been played. */
    private static int roundsPlayed = 0;

    /** How many symbols in row is required to win the game */
    private static int winningRowLength;

    /**
     * Getter for {@link #PLAYERCHAR}.
     * @return {@link #PLAYERCHAR}.
     */
    public static char getPLAYERCHAR() {
        return PLAYERCHAR;
    }

    /**
     * Getter for {@link #COMPUTERCHAR}.
     * @return {@link #COMPUTERCHAR}.
     */
    public static char getCOMPUTERCHAR() {
        return COMPUTERCHAR;
    }

    /**
     * Getter for {@link #EMPTYCHAR}.
     * @return {@link #EMPTYCHAR}.
     */
    public static char getEMPTYCHAR() {
        return EMPTYCHAR;
    }

    /**
     * Fills all cells in {@link #gameArea} with whitespace chars.
     */
	public static void formatGameArea() {
	    int rows = GameAreaSize.getRows();
	    int columns = GameAreaSize.getColumns();
	    gameArea = new char[rows][columns];
		for (int row = 0; row < gameArea.length; row++) {
			for (int col = 0; col < gameArea[row].length; col++) {
				gameArea[row][col] = EMPTYCHAR;
			}
		}
	}

    /**
     * Set custom length for winning row.
     * @param winRowLen symbols in row required to win.
     */
    public static void setWinningRowLength(int winRowLen) {
        winningRowLength = winRowLen;
    }

    /**
     * Checks if theres {@link #EMPTYCHAR} in the game area.
     * @return true if full, false if not.
     */
	public static boolean gameAreaFull() {
	    for (char[] row : gameArea) {
	        for (char col : row) {
	            if (col == EMPTYCHAR) {
	                return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the given cell in {@link #gameArea} is empty or not.
     * @param cell the coordinates to check.
     * @return true if the cell is empty, false if not.
     */
    public static boolean isTheCellEmpty(Cell cell) {
        return gameArea[cell.getRow()][cell.getCol()] == EMPTYCHAR;
    }

    /**
     * Checks if there's enough symbols in a row to win.
     * @return the char of the winner, PLAYERCHAR or COMPUTERCHAR. If there's no winner, reuturns EMPTYCHAR.
     */
	public static char getWinner() {
	    char winnerChar;
        winnerChar = getHorizontalWinner(gameArea);
        if (winnerChar != EMPTYCHAR) {
            return winnerChar;
        }
        winnerChar = getVerticalWinner(gameArea);
        if (winnerChar != EMPTYCHAR) {
            return winnerChar;
        }
        winnerChar = getDiagonalWinner(gameArea);
        return winnerChar;
    }

    /**
     * Checks for winners on horizontal lines.
     * @param gameArea char[][] where to check for the win.
     * @return the char of the winner, PLAYERCHAR or COMPUTERCHAR. If there's no winner, returns EMPTYCHAR.
     */
    private static char getHorizontalWinner(char[][] gameArea) {
        int symbolsInRow = 0;
        char latestCharr = EMPTYCHAR;
        int rows = GameAreaSize.getRows();
        int cols = GameAreaSize.getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                char cellChar = gameArea[row][col];
                if (cellChar != EMPTYCHAR) {
                    if (cellChar == latestCharr) {
                        symbolsInRow++;
                    } else {
                        latestCharr = cellChar;
                        symbolsInRow = 1;
                    }
                } else {
                    symbolsInRow = 0;
                    latestCharr = cellChar;
                }
                if (symbolsInRow == winningRowLength) {
                    return latestCharr;
                }
            }
            latestCharr = EMPTYCHAR;
            symbolsInRow = 0;
        }

        return EMPTYCHAR;
    }

    /**
     * Checks for winners on vertical lines.
     * @param gameArea char[][] where to check for the win.
     * @return the char of the winner, PLAYERCHAR or COMPUTERCHAR. If there's no winner, reuturns EMPTYCHAR.
     */
    private static char getVerticalWinner(char[][] gameArea) {
        int symbolsInRow = 0;
        char latestCharr = EMPTYCHAR;
        int rows = GameAreaSize.getRows();
        int cols = GameAreaSize.getColumns();
        for (int col = 0; col < cols; col++) {
            for (int row = 0; row < rows; row++) {
                char cellChar = gameArea[row][col];
                if (cellChar != EMPTYCHAR) {
                    if (cellChar == latestCharr) {
                        symbolsInRow++;
                    } else {
                        latestCharr = cellChar;
                        symbolsInRow = 1;
                    }
                } else {
                    symbolsInRow = 0;
                    latestCharr = cellChar;
                }
                if (symbolsInRow == winningRowLength) {
                    return latestCharr;
                }
            }
            latestCharr = EMPTYCHAR;
            symbolsInRow = 0;
        }

        return EMPTYCHAR;
    }

    /**
     * Checks if there's winning row in diagonal direction.
     * @param gameArea char[][] where to check for the win.
     * @return the char of the winner, PLAYERCHAR or COMPUTERCHAR. If there's no winner, reuturns EMPTYCHAR.
     */
    private static char getDiagonalWinner(char[][] gameArea) {
        int rows = GameAreaSize.getRows();
        int cols = GameAreaSize.getColumns();
        //System.out.println("rows: " + rows + "\ncols: " + cols);
        for (int row = 0; row < rows; row++) {
            //System.out.println("checking row: " + row);
            for (int col = 0; col < cols; col++) {
                //System.out.println("checking col: " + col);
                if (gameArea[row][col] != EMPTYCHAR) {
                    int symbolsInRow = 1;
                    char latestCharr = gameArea[row][col];
                    for (int i = 1; i < winningRowLength && row + i < rows && col + i < cols; i++) {
                        if (gameArea[row + i][col + i] == latestCharr) {
                            symbolsInRow++;
                            if (symbolsInRow == winningRowLength) {
                                return latestCharr;
                            }
                        } else {
                            break;
                        }
                    }
                    symbolsInRow = 1;
                    latestCharr = gameArea[row][col];
                    for (int i = 1; i < winningRowLength && row + i < rows && col - i >= 0; i++) {
                        if (gameArea[row + i][col - i] == latestCharr) {
                            symbolsInRow++;
                            if (symbolsInRow == winningRowLength) {
                                return latestCharr;
                            }
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        return EMPTYCHAR;
    }

    /**
     * Returns the symbol at given cell's coordinates.
     * @param cell the coordinates to check.
     * @return the char at given location.
     */
    public static char getSymbol(Cell cell) {
	    return gameArea[cell.getRow()][cell.getCol()];
    }

    /**
     * Ai uses this to checks if there's win available by filling some empty cell.
     * @param gameArea char[][] where to check for the win.
     * @param cell coordinates of last added symbol.
     * @return if there's winning move, returns the original cell updated with winner char, if not returns cell with -1, -1 coordinates
     */
    public static Cell getWinningMove(char[][] gameArea, Cell cell) {
        char winnerChar;
        winnerChar = getHorizontalWinner(gameArea);
        if (winnerChar != EMPTYCHAR) {
            cell.setSymbol(getHorizontalWinner(gameArea));
            return cell;
        }
        winnerChar = getVerticalWinner(gameArea);
        if (winnerChar != EMPTYCHAR) {
            cell.setSymbol(getVerticalWinner(gameArea));
            return cell;
        }
        winnerChar = getDiagonalWinner(gameArea);
        if (winnerChar != EMPTYCHAR) {
            cell.setSymbol(getDiagonalWinner(gameArea));
            return cell;
        }
        return new Cell(-1, -1);
    }

    /**
     * Getter for {@link #gameArea}.
     * @return 2D Array of the game area.
     */
	public static char[][] getGameArea() {
		return gameArea;
	}

    /**
     * Inserts the given symbol to given cell's coordinates in the {@link #gameArea}.
     * @param cell coordinates where to insert the symbol.
     * @param symbol to insert at given coordinates.
     */
	private static void insertSymbol(Cell cell, char symbol) {
		gameArea[cell.getRow()][cell.getCol()] = symbol;
	}

    /**
     * Inserts {@link #PLAYERCHAR} if the selected cell is empty.
     * @param cell coordinates where to insert the {@link #PLAYERCHAR}.
     * @return true if the cell was empty, false if not.
     */
	public static boolean insertUserSymbol(Cell cell) {
		if (isTheCellEmpty(cell)) {
			insertSymbol(cell, PLAYERCHAR);
			roundsPlayed++;
			return true;
		}
		return false;
	}

    /**
     * Insert COMPUTERCHAR to the given cell.
     * @param cell coordinates where to insert the {@link #COMPUTERCHAR}.
     */
	public static void insertComputerSymbol(Cell cell) {
	    int row = cell.getRow();
	    int col = cell.getCol();
	    gameArea[row][col] = COMPUTERCHAR;
	    roundsPlayed++;
    }

    /**
     * Inserts {@link #COMPUTERCHAR} to the given cell on the given char[][] game area.
     * @param cell coordinates where to insert the symbol.
     * @param gameArea where we insert the symbol into.
     * @param charToInsert what char should be inserted to the given game area.
     * @return the game area with the symboll added at the given coordinates.
     */
    public static char[][] insertSymbol(Cell cell, char[][] gameArea, char charToInsert) {
        int row = cell.getRow();
        int col = cell.getCol();
        gameArea[row][col] = charToInsert;
        return gameArea;
    }

    /**
     * Get the amount of rounds played.
     * @return number of rounds played this far.
     */
	public static int getRoundsPlayed() {
	    return roundsPlayed;
    }

    /**
     * Iterates trough all the cells in game area checking if they are empty.
     * @return an array of empty cells.
     */
    public static Cell[] getEmptyCells() {
	    ArrayList<Cell> emptyCells = new ArrayList<>();
	    for (int row = 0; row < GameAreaSize.getRows(); row++) {
	        for (int col = 0; col < GameAreaSize.getColumns(); col++) {
	            if (gameArea[row][col] == EMPTYCHAR) {
                    emptyCells.add(new Cell(row, col));
                }
            }
        }
        return emptyCells.toArray(new Cell[0]);
    }

    /**
     * How many symbols in row is required to win.
     * @return number of symbols in row required to win.
     */
    public static int getWinningRowLength() {
        return winningRowLength;
    }

    /**
     * Clones the current game area.
     * @return new char[][] identical to current {@link #gameArea}
     */
    public static char[][] cloneGameArea() {
        int rows = GameAreaSize.getRows();
        int cols = GameAreaSize.getColumns();
	    char[][] gameAreaClone = new char[rows][cols];
	    for (int row = 0; row < rows; row++) {
	        for (int col = 0; col < cols; col++) {
	            gameAreaClone[row][col] = gameArea[row][col];
            }
        }
        return gameAreaClone;
    }

    /**
     * Checks how many computer symbols are on the game area.
     * @return the number of computer symbols in the {@link #gameArea}.
     */
    public static int getComputerSymbolsInserted() {
	    int computerSymbols = 0;
	    for (char[] row : gameArea) {
	        for (char col : row) {
	            if (col == COMPUTERCHAR) {
	                computerSymbols++;
                }
            }
        }
        return computerSymbols;
    }

    /**
     * Holds the game area's height and width.
     */
    public static class GameAreaSize {
        private static int rows = 0;
        private static int columns = 0;

        /**
         * Private constructor for overriding the default public one.
         */
        private GameAreaSize() {
        }

        /**
         * Sets amount of both rows and columns.
         * @param rowsIn How many rows on the game area.
         * @param columnsIn How many columns on the game area.
         */
        public static void setSize(int rowsIn, int columnsIn) {
            rows = rowsIn;
            columns = columnsIn;
        }

        /**
         * Returns the bigger dimension of the game area.
         * @return int the bigger dimension.
         */
        public static int getMaxDimension() {
            if (rows > columns) {
                return rows;
            } else {
                return columns;
            }
        }

        /**
         * Returns the smaller dimension of the game area.
         * @return int the smaller dimension.
         */
        public static int getMinDimension() {
            if (rows < columns) {
                return rows;
            } else {
                return columns;
            }
        }

        /**
         * Returns the amount of rows on the game area.
         * @return the amount of rows.
         */
        public static int getRows() {
            return rows;
        }

        /**
         * Sets the amount of rows to given number.
         * @param rowsIn the amount of rows.
         */
        public static void setRows(int rowsIn) {
            rows = rowsIn;
        }

        /**
         * Returns the amount of columns on the game area.
         * @return the amount of columns.
         */
        public static int getColumns() {
            return columns;
        }

        /**
         * Sets the amount of columns to given number.
         * @param columnsIn the amount of columns.
         */
        public static void setColumns(int columnsIn) {
            columns = columnsIn;
        }
    }
}