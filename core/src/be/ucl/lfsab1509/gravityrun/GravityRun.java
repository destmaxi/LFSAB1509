package be.ucl.lfsab1509.gravityrun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import be.ucl.lfsab1509.gravityrun.states.GameStateManager;
import be.ucl.lfsab1509.gravityrun.states.MenuState;
import be.ucl.lfsab1509.gravityrun.states.OptionState;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

public class GravityRun extends ApplicationAdapter {
	public static final int WIDTH = 480;
	public static final	int HEIGHT = 800;

	public static final String TITLE = "Gravity Run";
	private GameStateManager gsm;


	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		gsm.push(new OptionState(gsm));
		gsm.push(new PlayState(gsm));
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor((float) 0.2 , (float)0.2 ,(float)0.2,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
