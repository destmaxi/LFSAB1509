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

    boolean clickedBack() {
        return Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

}