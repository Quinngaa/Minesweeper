package Game;

import javax.swing.JButton;

public class RevealAction extends Action {
    private Cell cell;
    private JButton button;
    private boolean isFlagged;
    private UI ui;
    
    public RevealAction(Cell cell, JButton button, UI ui) {
        this.cell = cell;
        isFlagged = cell.isFlagged();
        this.button = button;
        this.ui = ui;
    }

    public void doAction() {
        cell.setRevealed();
        button.setEnabled(false); 
        if (cell.getNeighbouringMines() > 0) { 
            button.setText(String.valueOf(cell.getNeighbouringMines())); 
        }
        if (isFlagged) {
            cell.toggleFlag();
        }
    }

    public void undo() {
        cell.setRevealed(false);
        button.setEnabled(true); 
        button.setIcon(ui.getEmptyCellIcon());
        if (isFlagged) {
            button.setIcon(ui.getFlagIcon());
            if (!cell.isFlagged()) {
                cell.toggleFlag();
            }
        }
    }
}
