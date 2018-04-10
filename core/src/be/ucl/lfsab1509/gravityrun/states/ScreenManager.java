package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import java.util.Stack;

public class ScreenManager {

    private GravityRun game;
    private Stack<State> states;

    public ScreenManager(GravityRun game) {
        this.game = game;
        states = new Stack<State>();
    }

    public void disposeAll() {
        while (!states.empty())
            states.pop().dispose();
    }

    public void pop() {
        states.pop().dispose();
        game.setScreen(states.peek());
    }

    public void push(State state) {
        states.push(state);
        game.setScreen(states.peek());
    }

    public void set(State state) {
        states.pop().dispose();
        states.push(state);
        game.setScreen(states.peek());
    }

}
