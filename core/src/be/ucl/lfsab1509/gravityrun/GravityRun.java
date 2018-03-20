package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.states.*;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.SensorHelper;
import be.ucl.lfsab1509.gravityrun.tools.User;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Map;

public class GravityRun extends ApplicationAdapter {

    public static float DENSITY;
    public static int HEIGHT;
    public static final String TITLE = "Gravity Run";
    public static int WIDTH;

    public static ArrayList<Integer> scoreList;
    public static Preferences pref;
    public static User user;
    public static SensorHelper sensorHelper = null;

    private GameStateManager gsm;
    private SpriteBatch batch;
    private SoundManager sound;

    public GravityRun(SensorHelper sensorHelper) {
        GravityRun.sensorHelper = sensorHelper;
        SensorHelper.MAIN = sensorHelper;
    }

    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);

        DENSITY = Gdx.graphics.getDensity();
        HEIGHT = Gdx.graphics.getHeight();
        WIDTH = Gdx.graphics.getWidth();

        State.initializeSkins();

        batch = new SpriteBatch();
        gsm = new GameStateManager();
        sound = new SoundManager();

        pref = Gdx.app.getPreferences("Player");
        pref.flush();

        Map<String, ?> map = pref.get();

        if (!pref.getBoolean(User.FIRSTTIME)) {
            user = new User();
            gsm.push(new FirstState(gsm, sound));
        } else {
            user = new User(map);
            gsm.push(new MenuState(gsm, sound));
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }

	@Override
	public void dispose() {
		batch.dispose();
		sound.dispose();
	}

	@Override
	public void pause() {
		super.pause(); // TODO
		sensorHelper.pauseSensors();
	}

	@Override
	public void resume() {
		super.resume(); // TODO
		sensorHelper.resumeSensors();
	}

	// Eventuellement, il ne reste plus que resize à overrider.
}
