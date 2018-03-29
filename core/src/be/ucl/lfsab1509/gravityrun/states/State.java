package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class State {

    static float height, width;
    static Skin aaronScoreSkin, labelScoreBoardSkin, tableSkin, tableScoreBoardSkin, titleSkin;
    private static boolean skinInitialized = false;

    GameStateManager gameStateManager;
    SoundManager soundManager;

    State(GameStateManager gameStateManager, SoundManager soundManager) {
        this.gameStateManager = gameStateManager;
        this.soundManager = soundManager;
    }

    public static void initializeSkins() {
        float d = GravityRun.DENSITY;
        height = GravityRun.HEIGHT;
        width = GravityRun.WIDTH;

        if (skinInitialized)
            disposeSkins();

        skinInitialized = true;

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
        // En espérant qu'il ne soit pas interrompu entre-temps.
    }

    public static void disposeSkins() {
        if (skinInitialized) {
            aaronScoreSkin.dispose();
            labelScoreBoardSkin.dispose();
            tableSkin.dispose();
            tableScoreBoardSkin.dispose();
            titleSkin.dispose();
        }
    }

    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();

}