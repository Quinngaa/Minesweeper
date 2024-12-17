package Game;

import java.util.*;

// Singleton class to manage the state of the game
public class StateManager {
    private static StateManager instance;
    private Stack<ArrayList<Action>> states;
    
    public StateManager() {
        states = new Stack<ArrayList<Action>>();
    }

    public static StateManager getInstance() {
        if (instance == null) {
            instance = new StateManager();
        }
        return instance;
    }

    public void newState() { 
        states.push(new ArrayList<Action>());
    }

    public void addAction(Action action) {
        states.peek().add(action);
    }

    public void undo() {
        while (!states.isEmpty() && states.peek().isEmpty()) {
            states.pop();
        }
        if (!states.isEmpty()) {
            ArrayList<Action> actions = states.pop();
            for (int i = actions.size() - 1; i >= 0; i--) {
                actions.get(i).undo();
            }
        }
    }

    public void doAction() {
        if (states.isEmpty()) {
            return;
        }
        ArrayList<Action> actions = states.peek();
        for (Action action : actions) {
            action.doAction();
        }
    }

    public void clearStates() {
        states.clear();
    }
}
