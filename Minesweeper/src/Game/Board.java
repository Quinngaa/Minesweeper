package Game;

import java.util.*;

// Board class will manage the grid of Cell objects
public class Board {
    private ArrayList<ArrayList<Cell>> cells;
    private int rows;
    private int cols;
    private int mineCount;

    public Board(int rows, int cols, int mineCount) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        cells = new ArrayList<ArrayList<Cell>>();
        for (int i = 0; i < rows; i++) {
            ArrayList<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < cols; j++) {
                row.add(new Cell());
            }
            cells.add(row);
        }
        // placeMines();
        // calculateNeighbouringMines();
    }

    // Method to place mines randomly on the board
    public void placeMines(int r, int c) {
        Random random = new Random();
        int minesPlaced = 0;
        while (minesPlaced < mineCount) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            if (!cells.get(row).get(col).isMine()) {
                if (row == r && col == c) {
                    continue;
                }
                cells.get(row).get(col).setMine();
                minesPlaced++;
            }
        }
        calculateNeighbouringMines();
    }

    // Method to calculate the number of neighbouring mines for each cell
    private void calculateNeighbouringMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells.get(i).get(j).isMine()) {
                    incrementNeighbours(i, j); // Increase the mine count around the cells adjacent to the current cell
                                               // (i, j).
                }
            }
        }
    }

    private void incrementNeighbours(int row, int col) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue; // skip the mine itself

                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    cells.get(newRow).get(newCol).incrementNeighbourCount();
                }

            }
        }
    }

    // Getter for the cells
    public Cell getCell(int row, int col) {
        return cells.get(row).get(col);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMineCount() {
        return mineCount;
    }
}
