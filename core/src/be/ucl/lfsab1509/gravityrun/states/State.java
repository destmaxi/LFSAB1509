package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

public abstract class State {

    float d = GravityRun.DENSITY, h = GravityRun.HEIGHT, w = GravityRun.WIDTH;
    GameStateManager gsm;
    I18NBundle string;
    OrthographicCamera cam;

    State(GameStateManager gsm) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        string = I18NBundle.createBundle(baseFileHandle);
    }

    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);

    public abstract void dispose();

}