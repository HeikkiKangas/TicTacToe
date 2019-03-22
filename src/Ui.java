import java.util.Scanner;

/**
 * Ui is the command line interface for ticatactoe game.
 *
 * @author Heikki Kangas
 * @version 2018.1213
 */
public class Ui {
	/** {@link Scanner} for reading player's input. */
	private static Scanner input;

	/**
	 * The main method that starts the game.
	 * <p>The game loops while there's no winning row
	 * on the game area and the game area is not full.</p>
	 * @param args not used for anything.
	 */
	public static void main(String [] args) {
		input = new Scanner(System.in);
		setGameAreaSize();
		setWinRowLength();
        GameLogic.formatGameArea();
        printGameArea();
		while (GameLogic.getWinner() == ' ' && !GameLogic.gameAreaFull()) {
		    askCell();
		    printGameArea();

		    if (GameLogic.getWinner() != ' ' || GameLogic.gameAreaFull()) {
		        break;
            }

            Ai.nextMove();
            printGameArea();
        }

        switch (GameLogic.getWinner()) {
            case 'X':
                System.out.println("You won! after " + GameLogic.getRoundsPlayed() + " rounds.");
                break;
            case 'O':
                System.out.println("You lost! after " + GameLogic.getRoundsPlayed() + " rounds.");
                break;
            default:
                System.out.println("Tie! after " + GameLogic.getRoundsPlayed() + " rounds.");
        }
	}

    /**
     * Keeps asking for game area size while given number is under 3 or input is invalid.
     */
    public static void setGameAreaSize() {
        int rows = 0;
        int columns = 0;
        System.out.println("Minimum game area size is 3 x 3.");
        while (rows < 3 || columns < 3) {
            try {
                System.out.print("How many rows: ");
                rows = Integer.parseInt(input.nextLine().trim());
                System.out.print("How many columns: ");
                columns = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please give a number.");
                continue;
            }

            if (rows < 3 || columns < 3) {
                System.out.println("Please give bigger numbers, 3 rows and columns is the minimum.");
            }
        }
        System.out.println();
        GameLogic.GameAreaSize.setSize(rows, columns);
        GameLogic.formatGameArea();
    }

    /**
     * Set custom length for winning row.
     */
    public static void setWinRowLength() {
        int winRowLength = GameLogic.GameAreaSize.getMinDimension();
        System.out.println("Default amount of symbols in row required for win is " + winRowLength +
                " on " + GameLogic.GameAreaSize.getRows() + "x" + GameLogic.GameAreaSize.getColumns() + " game area.");
        do {
            System.out.print("Please enter number to use custom win row length (min 2) or press enter to continue with the default: ");
            String winRowLengthStr = input.nextLine().trim();
            if (!winRowLengthStr.equals("")) {
                try {
                    winRowLength = Integer.parseInt(winRowLengthStr);

                    if (winRowLength > GameLogic.GameAreaSize.getMaxDimension()) {
                        System.out.println("Win row length can't be greater than game area size.");
                        winRowLength = 0;
                    }

                    if (winRowLength > 1) {
                        GameLogic.setWinningRowLength(winRowLength);
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Please give a number.");
                }
            }
        } while (winRowLength < 2);
        GameLogic.setWinningRowLength(winRowLength);
    }

    /**
     * Asks the user in which cell should the next symbol be inserted into.
	 * If given cell is not empty or input is invalid, it will ask for new cell.
     */
	public static void askCell() {
	    boolean symbolInserted = false;
	    while (!symbolInserted) {
	    	Cell selectedCell;
	        int row;
	        int col;

	        try {
                System.out.print("Select row: ");
                row = Integer.parseInt(input.nextLine().trim()) - 1;
                System.out.print("Select column: ");
                col = Integer.parseInt(input.nextLine().trim()) - 1;
                selectedCell = new Cell(row, col);
            } catch (NumberFormatException e) {
	            System.out.println("Please give a number.");
                continue;
            }

            if (!selectedCell.validateCell()) {
                System.out.println("Selected cell is outside the game area.");
                continue;
            }

            symbolInserted = GameLogic.insertUserSymbol(selectedCell);

            if (!symbolInserted) {
                System.out.println("Please select empty cell.");
            }
        }
	}

    /**
     * Prints out the current game area.
     */
	public static void printGameArea() {
		/*
		Output for 3x3 game area:

			     01  02  03
               +---+---+---+
			01 | X | O | X |
			   +---+---+---+
			02 | O |   | X |
			   +---+---+---+
			03 | O | X |   |
			   +---+---+---+
		*/

		char[][] gameArea = GameLogic.getGameArea();

		// Column numbering
		System.out.print("     ");
		for (int i = 1; i <= gameArea[0].length; i++) {
			if (i < 10) {
				System.out.print("0" + i + "  ");
			} else {
				System.out.print(i + "  ");
			}
		}
		System.out.println();

		// Top of the grid
		printVerticalSeparator();

		for (int row = 0; row < gameArea.length; row++) {
			
			// Row numbering 
			if (row < 9) {
				System.out.print("0" + (row + 1) + " |");
			} else {
				System.out.print((row + 1) + " |");
			}

			// Row of symbols
			for (int col = 0; col < gameArea[row].length; col++) {
					System.out.print(" " + gameArea[row][col] + " |");
			}
			System.out.println();

			// Row separator
			printVerticalSeparator();
		}
		System.out.println();
	}

	/**
	 * Helper method for {@link #printGameArea()} to print vertical separators for the grid.
	 */
	private static void printVerticalSeparator() {
		System.out.print("   ");
		for (int i = 0; i < GameLogic.GameAreaSize.getColumns(); i++) {
			System.out.print("+---");
		}
		System.out.println("+");
	}
}