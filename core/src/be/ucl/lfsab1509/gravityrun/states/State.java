package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import be.ucl.lfsab1509.gravityrun.tools.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

public abstract class State implements Screen {

    static float height, width;
    static Skin aaronScoreSkin, labelScoreBoardSkin, tableSkin, tableScoreBoardSkin, titleSkin;

    ArrayList<Integer> scoreList;
    GravityRun game;
    I18NBundle i18n;
    OrthographicCamera camera;
    Preferences pref;
    ScreenManager screenManager;
    SoundManager soundManager;
    User user;

    State(GravityRun gravityRun) {
        camera = new OrthographicCamera();
        game = gravityRun;
        i18n = I18NBundle.createBundle(Gdx.files.internal("strings/string"));
        pref = game.pref;
        scoreList = game.scoreList;
        screenManager = game.screenManager;
        soundManager = game.soundManager;
        user = game.user;
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
        update(dt);
        render(game.batch);
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

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void update(float dt);

    boolean clickedBack() {
        return Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    public static void initializeSkins() {
        float d = GravityRun.DENSITY;
        height = GravityRun.HEIGHT;
        width = GravityRun.WIDTH;

        tableScoreBoardSkin = new Skin();
        tableScoreBoardSkin.createSkin((int) (0.5f * width / d / 10));

        aaronScoreSkin = new Skin();
        aaronScoreSkin.createSkin((int) (0.75f * width / d / 10));

        labelScoreBoardSkin = new Skin();
        labelScoreBoardSkin.createSkin((int) (0.9f * width / d / 10));

        tableSkin = new Skin();
        tableSkin.createSkin((int) (width / d / 10));

        titleSkin = new Skin();
        titleSkin.createSkin((int) (1.5f * width / d / 10));
    }

}