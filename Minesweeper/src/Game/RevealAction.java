package Game;

import javax.swing.JButton;

public class RevealAction extends Action {
    private Cell cell;
    private JButton button;
    private boolean isFlagged;
    
    public RevealAction(Cell cell, JButton button) {
        this.cell = cell;
        isFlagged = cell.isFlagged();
        this.button = button;
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
        // button.setText("");
        button.setIcon(UI.getInstance().getEmptyCellIcon());
        
        if (isFlagged) {
            button.setIcon(UI.getInstance().getFlagIcon());
            if (!cell.isFlagged()) {
                cell.toggleFlag();
            }
        }
    }
}
