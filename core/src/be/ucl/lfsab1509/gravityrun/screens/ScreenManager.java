package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import java.util.Stack;

public class ScreenManager {

    private GravityRun game;
    private Stack<Screen> screens;

    public ScreenManager(GravityRun game) {
        this.game = game;
        screens = new Stack<Screen>();
    }

    public void disposeAll() {
        while (!screens.empty())
            screens.pop().dispose();
    }

    void pop() {
        screens.pop().dispose();
        game.setScreen(screens.peek());
    }

    public void push(Screen screen) {
        screens.push(screen);
        game.setScreen(screens.peek());
    }

    void set(Screen screen) {
        screens.pop().dispose();
        screens.push(screen);
        game.setScreen(screens.peek());
    }

    void errorMessage(String message) {

    }

}
