package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.screens.FirstScreen;
import be.ucl.lfsab1509.gravityrun.screens.HomeScreen;
import be.ucl.lfsab1509.gravityrun.screens.ScreenManager;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothManager;
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

import java.util.Map;

public class GravityRun extends Game {

    public static float DENSITY;
    public static int HEIGHT;
    public static final int MULTI_HEIGHT = 800;
    public static final int MULTI_WIDTH = 480;
    public static int WIDTH;

    public I18NBundle i18n;
    public Preferences preferences;
    public ScreenManager screenManager;
    public Skin aaronScoreSkin, labelScoreBoardSkin, tableScoreBoardSkin, tableSkin, titleSkin;
    public SoundManager soundManager;
    public SpriteBatch spriteBatch;
    private TextureAtlas skinTextureAtlas;
    public User user;

    public GravityRun(BluetoothManager bluetoothManager) {
        super();

        BluetoothManager.bluetoothManager = bluetoothManager;
    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);

        DENSITY = Gdx.graphics.getDensity();
        HEIGHT = Gdx.graphics.getHeight();
        WIDTH = Gdx.graphics.getWidth();

        initializeSkins();

        screenManager = new ScreenManager(this);
        spriteBatch = new SpriteBatch();

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        i18n = I18NBundle.createBundle(baseFileHandle);
        // I18NBundle.setExceptionOnMissingKey(false);
        User.i18n = i18n;

        preferences = Gdx.app.getPreferences("Player");
        preferences.flush();

        Map<String, ?> map = preferences.get();

        if (!preferences.getBoolean(User.KEY_FIRSTTIME)) {
            user = null;
            soundManager = new SoundManager();
            screenManager.push(new FirstScreen(this));
        } else {
            user = new User(this, map);
            soundManager = new SoundManager(user.getSoundLevel(), user.getMusicLevel());
            screenManager.push(new HomeScreen(this));
        }
    }

    @Override
    public void dispose() {
        screenManager.disposeAll();
        soundManager.dispose();
        spriteBatch.dispose();    // FIXME : already disposed ?
        disposeSkins();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void resume() {
        super.resume();
    }

    private void disposeSkins() {
        aaronScoreSkin.dispose();
        labelScoreBoardSkin.dispose();
        tableScoreBoardSkin.dispose();
        tableSkin.dispose();
        titleSkin.dispose();

        skinTextureAtlas.dispose();
    }

    public void exit() {
        Gdx.app.exit();
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

}
