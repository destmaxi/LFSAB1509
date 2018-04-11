package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public abstract class State {

    static float height, width;
    static Skin aaronScoreSkin, labelScoreBoardSkin, tableSkin, tableScoreBoardSkin, titleSkin;
    static TextureAtlas skinTextureAtlas;
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

    public static void disposeSkins() {
        if (skinInitialized) {
            aaronScoreSkin.dispose();
            labelScoreBoardSkin.dispose();
            tableSkin.dispose();
            tableScoreBoardSkin.dispose();
            titleSkin.dispose();
            skinTextureAtlas.dispose();
        }
    }

    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(SpriteBatch spriteBatch);

    public abstract void dispose();

}