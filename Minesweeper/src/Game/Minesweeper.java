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
    private UI ui;

    public Minesweeper(int rows, int cols, int mineCount) {
        board = new Board(rows, cols, mineCount);
        buttons = new ArrayList<>();
        firstClick = false;

        ui = new UI(this, rows, cols, mineCount); // Initialize UI
        startTimer();
    }

    // Method to initialize the buttons for each cell on the board
    public void initButtons(JPanel gridPanel) {
        for (int row = 0; row < board.getRows(); row++) {
            ArrayList<JButton> buttonRow = new ArrayList<>();
            for (int col = 0; col < board.getCols(); col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(50, 50));

                final int currentRow = row;
                final int currentCol = col;

                // Allow players to flag suspected mines
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            StateManager.getInstance().undoState();
                            toggleFlag(currentRow, currentCol);
                            ui.updateMineCounter(); // Update the mine counter whenever a flag is toggled
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            StateManager.getInstance().undoState();
                            if (!firstClick) {
                                board.placeMines(currentRow, currentCol);
                                firstClick = true;
                            }
                            handleCellClick(currentRow, currentCol);
                            ui.updateMineCounter(); // Update the mine counter whenever a flag is toggled
                        }
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

        cell.toggleFlag();
        if (cell.isFlagged()) {
            buttons.get(row).get(col).setText("F");
        } else {
            buttons.get(row).get(col).setText("");
        }
        // ui.updateMineCounter(); // Update the mine counter whenever a flag is toggled
    }

    public void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                secondsPassed++;
                ui.updateTimer(secondsPassed);
            }
        });
        timer.start();
    }

    private void handleCellClick(int row, int col) {
        Cell cell = board.getCell(row, col);
        if (cell.isMine()) {
            timer.stop();
            JOptionPane.showMessageDialog(ui.getFrame(), "Game Over!");
            revealAllMines();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getCols(); j++) {
                    if (!board.getCell(i, j).isMine()) {
                        revealCell(i, j);
                    }
                }
            }
        } else if (!cell.isRevealed()) {
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

        StateManager.getInstance().addAction(new RevealAction(cell, buttons.get(row).get(col)));

        if (cell.isFlagged()) {
            cell.toggleFlag();
            buttons.get(row).get(col).setText("");
            ui.updateMineCounter(); // Update the mine counter whenever a flag is removed
        }

        cell.setRevealed(); // set current cell as revealed
        buttons.get(row).get(col).setEnabled(false); // disable the button (ô đó mở rồi nên vô hiệu hóa cái button đó)
        if (cell.getNeighbouringMines() > 0) { // If there are neighboring mines
            // display the number of neighboring mines on the button
            buttons.get(row).get(col).setText(String.valueOf(cell.getNeighbouringMines()));
        } else {
            // If there are no neighboring mines, recursively reveal surrounding cells
            DFS(row, col);
        }
    }

    private void DFS(int row, int col) {
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
                    buttons.get(row).get(col).setIcon(ui.getMineIcon());
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }
}