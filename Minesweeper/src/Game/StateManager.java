package Game;

import java.util.*;

// Singleton class to manage the state of the game
public class StateManager {
    private static StateManager instance;
    private Stack<ArrayList<Action>> states;
    private boolean gameOver;
    
    private StateManager() {
        states = new Stack<ArrayList<Action>>();
        gameOver = false;
    }

    public static StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager();
        }
        return instance;
    }

    public void undoState() { 
        states.push(new ArrayList<Action>());
    }

    public void addAction(Action action) {
        states.peek().add(action);
    }

    public void undo() {
        if (!states.isEmpty()) {
            ArrayList<Action> actions = states.pop();
            for (int i = actions.size() - 1; i >= 0; i--) {
                actions.get(i).undo();
            }
        }
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void reset() {
        states.clear();
        gameOver = false;
    }
}
