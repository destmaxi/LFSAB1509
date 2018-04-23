package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.screens.FirstScreen;
import be.ucl.lfsab1509.gravityrun.screens.HomeScreen;
import be.ucl.lfsab1509.gravityrun.screens.Screen;
import be.ucl.lfsab1509.gravityrun.screens.ScreenManager;
import be.ucl.lfsab1509.gravityrun.tools.IMyGpgsClient;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.User;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;
import de.golfgl.gdxgamesvcs.IGameServiceListener;

import java.util.ArrayList;
import java.util.Map;

public class GravityRun extends Game implements IGameServiceListener {

    public static float DENSITY;
    public static int HEIGHT;
    public static final String TITLE = "Gravity Run";
    public static int WIDTH;

    public ArrayList<Integer> scoreList;
    public I18NBundle i18n;
    public IMyGpgsClient gsClient;
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

        /*if (gsClient == null)
            gsClient = new NoGameServiceClient();
        gsClient.setListener(this);*/
        gsClient.onResume();

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

        gsClient.onPause();
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

        gsClient.onResume();
    }

    @Override
    public void gsOnSessionActive() {
    }

    @Override
    public void gsOnSessionInactive() {
    }

    @Override
    public void gsShowErrorToUser(GsErrorType et, String msg, Throwable t) {
    }

    public void connect() {
        if (gsClient.isSessionActive())
            gsClient.signOut();
        else {
            gsClient.startSignInIntent();
            /*if (!gsClient.logIn())
                Gdx.app.error("GPGS_ERROR", "Cannot sign in");*/
        }
    }

    public void exit() {
        dispose();
        Gdx.app.exit();
    }

}
