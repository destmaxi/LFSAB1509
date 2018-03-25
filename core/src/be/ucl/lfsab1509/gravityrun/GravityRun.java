package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.states.*;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
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
    public static int WIDTH;
	public static final String TITLE = "Gravity Run";

    public static ArrayList<Integer> scoreList;
    public static Preferences pref;
    public static User user;

    private GameStateManager gsm;
	private SpriteBatch batch;
	private SoundManager sound;

	@Override
	public void create() {
        Gdx.input.setCatchBackKey(true);
        DENSITY = Gdx.graphics.getDensity();
        HEIGHT = Gdx.graphics.getHeight();
        WIDTH = Gdx.graphics.getWidth();
        System.out.println("density = " + DENSITY);
        System.out.println("height = " + HEIGHT);
        System.out.println("width = " + WIDTH);
        batch = new SpriteBatch();
		gsm = new GameStateManager();
		sound = new SoundManager();
		pref = Gdx.app.getPreferences("Player");
		pref.flush();

		Map<String,?> map = pref.get();

		sound.playMenu();
		if(!pref.getBoolean(User.FIRSTTIME)){
			user = new User();
			gsm.push(new FirstState(gsm, sound));
		}
		else{
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

}
