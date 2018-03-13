package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.states.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Map;

public class GravityRun extends ApplicationAdapter {

    public static final int HEIGHT = 800;
    public static final int WIDTH = 480;
	public static final String TITLE = "Gravity Run";

    public static ArrayList<Integer> scoreList;
    public static int indexSelected = 0;
    public static Preferences pref;

    private GameStateManager gsm;
	private SpriteBatch batch;

	@Override
	public void create() {
        Gdx.input.setCatchBackKey(true);
        batch = new SpriteBatch();
		gsm = new GameStateManager();
		pref = Gdx.app.getPreferences("Player");
		//pref.putBoolean("firstTime",true);
		pref.flush();
		if(!pref.getBoolean("firstTime"))
			gsm.push(new FirstState(gsm));
		else
			gsm.push(new MenuState(gsm));
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
	}

}
