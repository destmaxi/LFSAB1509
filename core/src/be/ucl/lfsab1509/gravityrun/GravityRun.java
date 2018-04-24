package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.screens.*;
import be.ucl.lfsab1509.gravityrun.tools.IGpgs;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.User;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Map;

public class GravityRun extends Game {

    public static float DENSITY;
    public static int HEIGHT;
    public static final String TITLE = "Gravity Run";
    public static int WIDTH;

    public ArrayList<Integer> scoreList;
    public I18NBundle i18n;
    public IGpgs gpgs;
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

        Screen.initializeSkins();

        i18n = I18NBundle.createBundle(Gdx.files.internal("strings/string"));
        screenManager = new ScreenManager(this);
        soundManager = new SoundManager();
        spriteBatch = new SpriteBatch();

        preferences = Gdx.app.getPreferences("Player");
        preferences.flush();

        Map<String, ?> map = preferences.get();

        if (!preferences.getBoolean(User.FIRSTTIME)) {
            user = new User(this);
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
//        spriteBatch.dispose();    // FIXME : already disposed ?
    }

    @Override
    public void pause() {
        super.pause();
        gpgs.onPause();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.2f, .2f, .2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        super.render();
    }

    @Override
    public void resume() {
        super.resume();
        gpgs.onResume();
    }

    public void connect() {
        if (gpgs.isSignedIn())
            gpgs.signOut();
        else
            gpgs.startSignInIntent();
    }

    public void exit() {
        dispose();
        Gdx.app.exit();
    }

    void errorMessage(String message) {
        ((AbstractMenuScreen) getScreen()).spawnErrorDialog("Erreur", message);
    }

}
