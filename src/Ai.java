import java.util.Random;
import java.util.ArrayList;

/**
 * Ai class either computes or raffles the next move for computer.
 *
 * @author Heikki Kangas
 * @version 2018.1204
 */
public class Ai {
    /** {@link Cell} array for storing empty cells of the {@link GameLogic#gameArea}. */
    private static Cell[] emptyCells;

    /** Has two possible moves for each empty cell in the {@link GameLogic#gameArea},
     * one with player's char and one with computer's char.
     */
    // A = EmptyCell index
    // B = Player/Computer
    // C = GameArea Row
    // D = GameArea Column
    // possibleMoves[A][B[C][D]
    private static char[][][][] possibleMoves;

    /** {@link Random} for raffling random cells. */
    private static Random rng = new Random();

    /**
     * Updates {@link #possibleMoves} with all the possible moves.
     * Checks for empty cells in game area, then creates two game areas per empty cell,
     * one for player move and another for computer move.
     */
    private static void getBothPlayersPossibleMoves() {
        int rows = GameLogic.GameAreaSize.getRows();
        int columns = GameLogic.GameAreaSize.getColumns();
        emptyCells = GameLogic.getEmptyCells();
        possibleMoves = new char[emptyCells.length][2][rows][columns];
        char playerChar = GameLogic.getPLAYERCHAR();
        char computerChar = GameLogic.getCOMPUTERCHAR();

        for (int i = 0; i < emptyCells.length; i++) {
            char[][] clonedGameArea = GameLogic.cloneGameArea();
            possibleMoves[i][0] = GameLogic.insertSymbol(emptyCells[i], clonedGameArea, playerChar);
            clonedGameArea = GameLogic.cloneGameArea();
            possibleMoves[i][1] = GameLogic.insertSymbol(emptyCells[i], clonedGameArea, computerChar);
        }
    }

    /**
     * Computes the next move for computer.
     * If there's no computerchars in the game area one will be inserted at random location.
     * If there's at least one computerchar in the game area, searches for winning move.
     * If no winning move found, checks if there's player winning move available to block.
     * If no winning moves are found it will insert computerchar to random location.
     */
    public static void nextMove() {
        int computerSymbolsInserted = GameLogic.getComputerSymbolsInserted();
        ArrayList<Cell> playerWinMoves = new ArrayList<>();
        if (computerSymbolsInserted == 0) {
            Cell cell = getRandomEmptyCell();
            GameLogic.insertComputerSymbol(cell);
        } else {
            getBothPlayersPossibleMoves();

            boolean computerWinningMoveFound = false;
            boolean playerWinningMoveFound = false;
            boolean winningMoveFound = false;

            for (int i = 0; i < possibleMoves.length; i++) {
                for (int a = 0; a < possibleMoves[i].length; a++) {
                    Cell cell = GameLogic.getWinningMove(possibleMoves[i][a], emptyCells[i]);
                    if (cell.getRow() != -1 && cell.getCol() != -1) {
                        if (cell.getSymbol() == GameLogic.getCOMPUTERCHAR()) {
                            GameLogic.insertComputerSymbol(cell);
                            winningMoveFound = true;
                            computerWinningMoveFound = true;
                            break;
                        } else {
                            playerWinMoves.add(cell);
                            winningMoveFound = true;
                            playerWinningMoveFound = true;
                        }

                    }
                }
                if (computerWinningMoveFound) {
                    break;
                }

            }
            if (playerWinningMoveFound && !computerWinningMoveFound) {
                GameLogic.insertComputerSymbol(playerWinMoves.get(rng.nextInt(playerWinMoves.size())));
            }
            if (!winningMoveFound) {
                Cell cell = getRandomEmptyCell();
                GameLogic.insertComputerSymbol(cell);
            }
        }
    }

    /**
     * Returns {@link Cell} with random coordinates.
     * @return {@link Cell} with random coordinates.
     */
    private static Cell getRandomEmptyCell() {
        boolean emptyCellFound = false;
        Cell cell = new Cell();
		int row = 0;
		int col = 0;
		while (!emptyCellFound) {
		    row = rng.nextInt(GameLogic.GameAreaSize.getRows());
            col = rng.nextInt(GameLogic.GameAreaSize.getColumns());
            cell = new Cell(row, col);
            emptyCellFound = GameLogic.isTheCellEmpty(cell);
        }
        return cell;
    }
}