package Game;

// Represents induvidual cell on the board
public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int neighbouringMines;

    public Cell() {
        isMine = false;
        isRevealed = false;
        isFlagged = false;
        neighbouringMines = 0;
    }

    // Getter and Setter methods
    public boolean isMine() {
        return isMine;
    }
    
    public void setMine() {
        isMine = true;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed() {
        isRevealed = true;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void toggleFlag() {
        isFlagged = !isFlagged;
    }

    public int getNeighbouringMines() {
        return neighbouringMines;
    }

    public void incrementNeighbourCount() {
        neighbouringMines++;
    }
}
