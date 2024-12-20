package Game;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

public class Minesweeper {
    private Board board;
    private ArrayList<ArrayList<JButton>> buttons;
    private Timer timer;
    private int secondsPassed; // time passed since the game started
    private boolean firstClick;
    private boolean gameEnded;

    private int tileSize;
    private int boardWidth;
    private int boardHeight;

    public Minesweeper(int rows, int cols, int mineCount) {
        tileSize = 50;
        this.boardWidth = cols * tileSize;
        this.boardHeight = rows * tileSize;

        board = new Board(rows, cols, mineCount);
        buttons = new ArrayList<>();
        firstClick = false;
        gameEnded = false;

        UI.getInstance().setVariables(this, rows, cols, mineCount, tileSize);
        startTimer();
    }

    // Method to initialize the buttons for each cell on the board
    public void initButtons(JPanel gridPanel) {
        // buttons.clear();
        for (int row = 0; row < board.getRows(); row++) {
            ArrayList<JButton> buttonRow = new ArrayList<>();
            for (int col = 0; col < board.getCols(); col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(tileSize, tileSize));
                button.setBackground(Color.LIGHT_GRAY);
                button.setIcon(UI.getInstance().getEmptyCellIcon());
                // button.setBorder(BorderFactory.createLineBorder(Color.RED));

                final int currentRow = row;
                final int currentCol = col;

                // Allow players to flag suspected mines
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            StateManager.getInstance().newState();
                            toggleFlag(currentRow, currentCol);
                            StateManager.getInstance().doAction();
                            UI.getInstance().updateMineCounter(); // Update the mine counter whenever a flag is toggled
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            StateManager.getInstance().newState();
                            if (!firstClick) {
                                System.out.println("Placing mines...");
                                board.placeMines(currentRow, currentCol);
                                firstClick = true;
                            }
                            handleCellClick(currentRow, currentCol);
                            StateManager.getInstance().doAction();
                            UI.getInstance().updateMineCounter(); // Update the mine counter whenever a flag is toggled
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        button.setBorder(BorderFactory.createLineBorder(Color.RED));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        button.setBorder(null);
                    }
                });
                buttonRow.add(button);
                gridPanel.add(button); // Add each button to the grid panel
            }
            buttons.add(buttonRow);
        }
    }

    // Method to toggle flag on a cell
    private void toggleFlag(int row, int col) {
        Cell cell = board.getCell(row, col);
        if (cell.isRevealed()) {
            return; // cannot flag a revealed cell
        }
        StateManager.getInstance().addAction(new ToggleFlagAction(buttons.get(row).get(col), cell));
    }

    public void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                secondsPassed++;
                UI.getInstance().updateTimer(secondsPassed);
            }
        });
        timer.start();
    }

    private void handleCellClick(int row, int col) {
        Cell cell = board.getCell(row, col);
        if (!cell.isRevealed()) {
            revealCell(row, col);
        } else {
            int countFlags = 0;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;
                    if (newRow >= 0 && newRow < board.getRows() && newCol >= 0 && newCol < board.getCols()) {
                        if (board.getCell(newRow, newCol).isFlagged()) {
                            countFlags++;
                        }
                    }
                }
            }
            if (countFlags == cell.getNeighbouringMines()) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int newRow = row + i;
                        int newCol = col + j;
                        if (newRow >= 0 && newRow < board.getRows() && newCol >= 0 && newCol < board.getCols()) {
                            if (!board.getCell(newRow, newCol).isFlagged()
                                    && !board.getCell(newRow, newCol).isRevealed()) {
                                revealCell(newRow, newCol);
                            }
                        }
                    }
                }
            }
        }
    }

    private void revealCell(int row, int col) {
        Cell cell = board.getCell(row, col);
        if (cell.isRevealed()) { // Ignore if cell is already revealed
            return;
        }

        if (cell.isMine()) {
            gameEnded = true;
            timer.stop();
            UI.getInstance().disableUndoButton(); // no UNDO if game over
            buttons.get(row).get(col).setBackground(Color.RED);

            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    if (!board.getCell(i, j).isMine()) {
                        revealCell(i, j);
                    }
                }
            }
            revealAllMines();
            UI.getInstance().showGameOverDialog();
            return;
        }

        StateManager.getInstance().addAction(new RevealAction(cell, buttons.get(row).get(col)));

        if (cell.isFlagged()) {
            cell.toggleFlag();
            buttons.get(row).get(col).setIcon(UI.getInstance().getEmptyCellIcon());
            UI.getInstance().updateMineCounter(); // Update the mine counter whenever a flag is removed
        }

        cell.setRevealed(); // set current cell as revealed

        if (cell.getNeighbouringMines() == 0) {
            // If there are no neighboring mines, recursively reveal surrounding cells
            miniDFS(row, col);
        }

        if (!gameEnded && checkWin()) {
            timer.stop();
            StateManager.getInstance().doAction();
            UI.getInstance().disableUndoButton();
            UI.getInstance().showGameWinDialog();
        }
    }

    private void miniDFS(int row, int col) {
        int[] directions = { -1, 0, 1 };
        for (int i : directions) {
            for (int j : directions) {
                if (i == 0 && j == 0)
                    continue;
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < board.getRows() && newCol >= 0 && newCol < board.getCols()) {
                    Cell neighbor = board.getCell(newRow, newCol);
                    if (!neighbor.isRevealed() && !neighbor.isMine()) {
                        revealCell(newRow, newCol);
                    }
                }
            }
        }
    }

    private void revealAllMines() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Cell cell = board.getCell(row, col);
                if (cell.isMine()) {
                    cell.setRevealed();
                    buttons.get(row).get(col).setIcon(UI.getInstance().getMineIcon());
                }
            }
        }
    }

    public void restartGame() {
        secondsPassed = 0;
        firstClick = false;
        gameEnded = false;

        buttons.clear();
        board.resetBoard();
        UI.getInstance().resetUI();
        StateManager.getInstance().clearStates();

        UI.getInstance().enableUndoButton();
        timer.start();
    }

    public boolean checkWin() {
        for (int row = 0; row < board.getRows(); row++) {
            for (int col = 0; col < board.getCols(); col++) {
                Cell cell = board.getCell(row, col);
                if (!cell.isRevealed() && !cell.isMine()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board getBoard() {
        return board;
    }

    public int getCols() {
        return board.getCols();
    }

    public int getRows() {
        return board.getRows();
    }
}