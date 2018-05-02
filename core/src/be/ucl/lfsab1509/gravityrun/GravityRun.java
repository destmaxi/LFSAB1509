package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.screens.*;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.User;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Map;

public class GravityRun extends Game {

    public static float DENSITY;
    public static int HEIGHT;
    public static final String TITLE = "Gravity Run";
    public static int WIDTH;
    public Skin aaronScoreSkin, labelScoreBoardSkin, tableScoreBoardSkin, tableSkin, titleSkin;
    private TextureAtlas skinTextureAtlas;

    public ArrayList<Integer> scoreList;
    public I18NBundle i18n;
    public Preferences preferences;
    public ScreenManager screenManager;
	public SoundManager soundManager;
    public SpriteBatch spriteBatch;
    public User user;

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);

        DENSITY = Gdx.graphics.getDensity();
        HEIGHT = Gdx.graphics.getHeight();
        WIDTH = Gdx.graphics.getWidth();

        initializeSkins();

        screenManager = new ScreenManager(this);
        soundManager = new SoundManager();
        spriteBatch = new SpriteBatch();

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        i18n = I18NBundle.createBundle(baseFileHandle);
        User.i18n = i18n;

        preferences = Gdx.app.getPreferences("Player");
        preferences.flush();

        Map<String, ?> map = preferences.get();

        if (!preferences.getBoolean(User.FIRSTTIME)) {
            user = null;
            screenManager.push(new FirstScreen(this));
        } else {
            user = new User(this, map);
            screenManager.push(new HomeScreen(this));
        }
    }

    @Override
    public void dispose() {
        screenManager.disposeAll();
        soundManager.dispose();
        spriteBatch.dispose();    // FIXME : already disposed ?
        disposeSkins();
        User.i18n = null;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void resize(int width, int height) {
        System.out.println("GravityRun.resize");
        super.resize(width, height);
    }

    @Override
    public void resume() {
        System.out.println("GravityRun.resume");
        super.resume();
    }

    @Override
    public void pause() {
        System.out.println("GravityRun.pause");
        super.pause();
    }

    private void disposeSkins() {
        aaronScoreSkin.dispose();
        labelScoreBoardSkin.dispose();
        tableScoreBoardSkin.dispose();
        tableSkin.dispose();
        titleSkin.dispose();

        skinTextureAtlas.dispose();
    }

    private void initializeSkins() {
        float d = DENSITY;
        float width = WIDTH;

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

    public void exit() {
        Gdx.app.exit();
    }

}
