package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import be.ucl.lfsab1509.gravityrun.tools.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.Input;

public abstract class Screen implements com.badlogic.gdx.Screen {

    private static boolean skinInitialized = false;
    static float height, width;
    static Skin aaronScoreSkin, labelScoreBoardSkin, tableScoreBoardSkin, tableSkin, titleSkin;
    static TextureAtlas skinTextureAtlas;

    GravityRun game;
    ScreenManager screenManager;
    SoundManager soundManager;
    User user;

    Screen(GravityRun gravityRun) {
        game = gravityRun;
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

    static void disposeSkins() {
        if (skinInitialized) {
            aaronScoreSkin.dispose();
            labelScoreBoardSkin.dispose();
            tableScoreBoardSkin.dispose();
            tableSkin.dispose();
            titleSkin.dispose();

            skinTextureAtlas.dispose();

            skinInitialized = false;
        }
    }

    public static void initializeSkins() {
        float d = GravityRun.DENSITY;
        height = GravityRun.HEIGHT;
        width = GravityRun.WIDTH;

        if (skinInitialized)
            disposeSkins();

        skinInitialized = true;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("arial.ttf"));
        skinTextureAtlas = new TextureAtlas("skin/uiskin.atlas");

        tableScoreBoardSkin = new Skin();
        tableScoreBoardSkin.createSkin((int) (0.5f * width / d / 10), generator, skinTextureAtlas);

        aaronScoreSkin = new Skin();
        aaronScoreSkin.createSkin((int) (0.75f * width / d / 10), generator, skinTextureAtlas);

        labelScoreBoardSkin = new Skin();
        labelScoreBoardSkin.createSkin((int) (0.9f * width / d / 10), generator, skinTextureAtlas);

        tableSkin = new Skin();
        tableSkin.createSkin((int) (width / d / 10), generator, skinTextureAtlas);

        titleSkin = new Skin();
        titleSkin.createSkin((int) (1.5f * width / d / 10), generator, skinTextureAtlas);
        // En espérant qu'il ne soit pas interrompu entre-temps.

        generator.dispose();
    }

}