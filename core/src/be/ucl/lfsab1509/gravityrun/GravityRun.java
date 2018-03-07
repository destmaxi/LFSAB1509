package be.ucl.lfsab1509.gravityrun;

import be.ucl.lfsab1509.gravityrun.states.*;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GravityRun extends ApplicationAdapter {

    public static final int HEIGHT = 800;
    public static final int WIDTH = 480;
	public static final String TITLE = "Gravity Run";

	private GameStateManager gsm;
	private SpriteBatch batch;

	public static int lastScore = 0;

	@Override
	public void create() {
        Gdx.input.setCatchBackKey(true);
        batch = new SpriteBatch();
		gsm = new GameStateManager();
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
