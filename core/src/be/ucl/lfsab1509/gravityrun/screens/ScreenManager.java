package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import java.util.Stack;

public class ScreenManager {

    private GravityRun game;
    private Stack<Screen> screens;

    public ScreenManager(GravityRun game) {
        this.game = game;
        screens = new Stack<>();
    }

    public void disposeAll() {
        while (!screens.empty())
            screens.pop().dispose();
    }

    Screen peek() {
        return screens.peek();
    }

    void pop() {
        System.out.println("ScreenManager.pop");
        Screen oldScreen = screens.pop();
        if (screens.empty()) {
            oldScreen.dispose();
            game.exit();
        } else {
            game.setScreen(screens.peek());
            oldScreen.dispose();
        }
    }

    public void push(Screen screen) {
        System.out.println("ScreenManager.push");
        screens.push(screen);
        game.setScreen(screens.peek());
    }

    void set(Screen screen) {
        System.out.println("ScreenManager.set");
        Screen oldScreen = screens.pop();
        screens.push(screen);
        game.setScreen(screens.peek());
        oldScreen.dispose();
    }

}
