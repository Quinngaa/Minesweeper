package Game;

import javax.swing.JButton;

public class ToggleFlagAction extends Action {
    private JButton button;
    private Cell cell;

    public ToggleFlagAction(JButton button, Cell cell) {
        this.button = button;
        this.cell = cell;
    }

    public void doAction() {
        cell.toggleFlag();
        if (cell.isFlagged()) {
            button.setIcon(UI.getInstance().getFlagIcon());
        } else {
            button.setIcon(UI.getInstance().getEmptyCellIcon());
        }
    }

    public void undo() {
        doAction();
    }
}
