import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

/**
 * The main window started by {@link Gui}.
 *
 * @author Heikki Kangas
 * @version 2018.1213
 */
public class MainWindow extends JFrame implements Runnable {

    /** JPanel that has the scores. */
    private static ScorePanel scorePanel;

    /** JPanel that has the game grid. */
    private static GameAreaPanel gameAreaPanel;

    /**
     * The root window of GUI.
     * Asks the user for game area size.
     * Has two panels, one displaying scores and one for displaying game state.
     */
    public MainWindow() {
        super("TicTacToe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setOptions();
        gameAreaPanel = new GameAreaPanel();
        scorePanel = new ScorePanel();
        setLayout(new BorderLayout());
        add(scorePanel, BorderLayout.PAGE_START);
        add(gameAreaPanel, BorderLayout.CENTER);
        setMinimumSize(new Dimension(300,300));
        pack();
        setVisible(true);
        gameAreaPanel.adjustFontSize();
    }

    /**
     * Shows input dialog with 3 textfields, one for rows, columns and length of the winning row.
     */
    private void setOptions() {
        while (true) {
            int rows = 3;
            int columns = 3;
            int winRowLen = 3;

            JTextField rowsField = new JTextField();
            JTextField columnsField = new JTextField();
            JTextField winRowLenField = new JTextField();
            Object[] fields = {"Minimum game area size is 3 x 3.\nMinimum winning row length is 2." +
                    "\nIf field is left empty, default will be used.", "Rows: (default: 3)", rowsField, "Columns: (default: 3)",
                    columnsField, "Winning row length: (default: smallest side length)", winRowLenField};

            JOptionPane.showConfirmDialog(this, fields, "Options", JOptionPane.PLAIN_MESSAGE);

            try {
                if (!rowsField.getText().equals("")) {
                    rows = Integer.parseInt(rowsField.getText().trim());
                }
                if (!columnsField.getText().equals("")) {
                    columns = Integer.parseInt(columnsField.getText().trim());
                }
                if (rows < 3 || columns < 3) {
                    JOptionPane.showMessageDialog(this, "Minimum game area size is 3 x 3, please try again.", "Input error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                GameLogic.GameAreaSize.setSize(rows, columns);
                GameLogic.formatGameArea();

                if (!winRowLenField.getText().equals("")) {
                    winRowLen = Integer.parseInt(winRowLenField.getText().trim());
                    if (winRowLen < 2) {
                        JOptionPane.showMessageDialog(this, "Minimum win row length is 2, please try again.", "Input error", JOptionPane.ERROR_MESSAGE);
                        continue;
                    }
                } else {
                    winRowLen = GameLogic.GameAreaSize.getMinDimension();
                }
                GameLogic.setWinningRowLength(winRowLen);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a number or leave the field empty.", "Input error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            return;
        }

    }

    /**
     * Displays the dialog telling if you have won, lost or if it was a tie.
     * Game area's buttons are disabled while dialog is visible.
     * Starts new game when dialog is closed.
     */
    public void showWinnerDialog() {
        int TIE = 0;
        int WON = 1;
        int LOST = 2;

        String[] messages = {"It's a tie!", "You won!", "You lost!"};

        // JLabels are used for centering the message in the dialog.
        JLabel[] msgLabels = new JLabel[3];
        for (int i = 0; i < 3; i++) {
            msgLabels[i] = new JLabel(messages[i], SwingConstants.CENTER);
        }

        char winner = GameLogic.getWinner();
        if (winner == GameLogic.getEMPTYCHAR() && GameLogic.gameAreaFull()) {
            gameAreaPanel.disableButtons();
            JOptionPane.showMessageDialog(this, msgLabels[TIE], "Game over", JOptionPane.PLAIN_MESSAGE);
        } else if (winner == GameLogic.getPLAYERCHAR()) {
            gameAreaPanel.disableButtons();
            scorePanel.increasePlayerScore();
            JOptionPane.showMessageDialog(this, msgLabels[WON], "Game over", JOptionPane.PLAIN_MESSAGE);
        } else if (winner == GameLogic.getCOMPUTERCHAR()) {
            gameAreaPanel.disableButtons();
            scorePanel.increaseComputerScore();
            JOptionPane.showMessageDialog(this, msgLabels[LOST], "Game over", JOptionPane.PLAIN_MESSAGE);
        }

        GameLogic.formatGameArea();
        gameAreaPanel.enableButtons();
        gameAreaPanel.updateView();
    }

    public void run() {
    }
    
    /**
     * JPanel showing the current game score.
     */
    private class ScorePanel extends JPanel {

        /** Player's score. */
        private int playerScore;

        /** Computer's score. */
        private int computerScore;

        /** Label for player's score. */
        private JLabel playerScoreLabel;

        /** Label for computer's score. */
        private JLabel computerScoreLabel;

        /**
         * Creates new ScorePanel.
         */
        public ScorePanel() {

            // One column, two rows, 5px X & Y padding.
            setLayout(new GridLayout(1, 2, 5, 5));
            playerScore = 0;
            computerScore = 0;
            playerScoreLabel = new JLabel();
            computerScoreLabel = new JLabel();
            playerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            computerScoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
            updateScores();
            add(playerScoreLabel);
            add(computerScoreLabel);
        }

        /**
         * Updates {@link #playerScoreLabel} and {@link #computerScoreLabel} to show current scores.
         */
        private void updateScores() {
            playerScoreLabel.setText("Player score: " + playerScore);
            computerScoreLabel.setText("Computer score: " + computerScore);
        }

        /**
         * Increases {@link #playerScore} by one.
         */
        public void increasePlayerScore() {
            playerScore++;
            updateScores();
        }

        /**
         * Increases {@link #computerScore} by one.
         */
        public void increaseComputerScore() {
            computerScore++;
            updateScores();
        }
    }

    /**
     * JPanel showing the current game area state.
     */
    private class GameAreaPanel extends JPanel {

        /** Buttons resembling the cells in the game area grid. */
        private JButton[][] buttons;

        /** Font used in the buttons. */
        private Font btnFont;

        /**
         * Creates new GameAreaPanel.
         */
        public GameAreaPanel() {
            int rows = GameLogic.GameAreaSize.getRows();
            int cols = GameLogic.GameAreaSize.getColumns();
            setLayout(new GridLayout(rows, cols, 2, 2));
            buttons = new JButton[rows][cols];
            btnFont = new Font("Arial", Font.PLAIN, 15);
            //setBorder(new EmptyBorder(3, 3, 3, 3));
            setBackground(Color.BLACK);

            /**
             * Actionlistener for the buttons on the game area.
             */
            ActionListener btnListener = new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (GameLogic.getWinner() == ' ' && !GameLogic.gameAreaFull()) {
                        int rows = GameLogic.GameAreaSize.getRows();
                        int cols = GameLogic.GameAreaSize.getColumns();
                        // Check which button was clicked.
                        JButton sourceBtn = (JButton) e.getSource();
                        for (int row = 0; row < rows; row++) {
                            for (int col = 0; col < cols; col++) {
                                if (buttons[row][col] == sourceBtn) {

                                    // If the clicked button was empty...
                                    if (GameLogic.insertUserSymbol(new Cell(row, col))) {
                                        updateView();

                                        // Check if game should end.
                                        if (GameLogic.gameAreaFull() || GameLogic.getWinner() != ' ') {
                                            showWinnerDialog();
                                        } else {
                                            Ai.nextMove();
                                            updateView();
                                            if (GameLogic.gameAreaFull() || GameLogic.getWinner() != ' ') {
                                                showWinnerDialog();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            };

            Insets insets = new Insets(0, 0, 0, 0);
            // Add buttons to the grid.
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    JButton btn = new JButton();
                    btn.setFont(btnFont);
                    btn.addActionListener(btnListener);
                    btn.setPreferredSize(new Dimension(33, 33));
                    btn.setMargin(insets);
                    btn.setBackground(Color.WHITE);
                    buttons[row][col] = btn;
                    add(btn);
                }
            }

            /**
             * ComponentListener for adjusting buttons font size whenever the window is resized.
             */
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    adjustFontSize();
                }
            });
        }

        /**
         * Disables all the buttons when game ends.
         */
        public void disableButtons() {
            for (JButton[] btnArray : buttons) {
                for (JButton btn : btnArray) {
                    btn.setEnabled(false);
                }
            }
        }

        /**
         * Enables all the buttons in the beginning of new game.
         */
        public void enableButtons() {
            for (JButton[] btnArray : buttons) {
                for (JButton btn : btnArray) {
                    btn.setEnabled(true);
                }
            }
        }

        /**
         * Adjusts buttons font size, called whenever the window is resized.
         */
        public void adjustFontSize() {
            JButton firstBtn = buttons[0][0];
            int minSize = Math.min(firstBtn.getWidth(), firstBtn.getHeight());
            int fontSize = (int) (minSize / 1.1);
            btnFont = new Font("Arial", Font.PLAIN, fontSize);
            for (JButton[] btnArray : buttons) {
                for (JButton btn : btnArray) {
                    btn.setFont(btnFont);
                }
            }
        }

        /**
         * Updates the buttons texts to chars in the {@link GameLogic#gameArea}.
         */
        private void updateView() {
            char[][] gameArea = GameLogic.getGameArea();
            int rows = GameLogic.GameAreaSize.getRows();
            int cols = GameLogic.GameAreaSize.getColumns();
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    buttons[row][col].setText(gameArea[row][col] + "");
                }
            }
        }
    }
}