package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public abstract class Screen implements com.badlogic.gdx.Screen {

    float height, width;
    GravityRun game;
    ScreenManager screenManager;
    SoundManager soundManager;

    Screen(GravityRun gravityRun) {
        game = gravityRun;
        height = GravityRun.HEIGHT;
        width = GravityRun.WIDTH;
        screenManager = game.screenManager;
        soundManager = game.soundManager;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void render(float dt) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    int calculateStandardWidth() {
        int standardWidth;
        if (width <= 480)
            standardWidth = 480;
        else if (width <= 600)
            standardWidth = 600;
        else if (width <= 840)
            standardWidth = 840;
        else if (width <= 960)
            standardWidth = 960;
        else if (width <= 1280)
            standardWidth = 1280;
        else if (width <= 1440)
            standardWidth = 1440;
        else
            standardWidth = 1600;
        return standardWidth;
    }

    boolean clickedBack() {
        return Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

}