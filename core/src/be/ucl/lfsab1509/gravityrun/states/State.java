package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;

public abstract class State {

    protected GameStateManager gsm;
    protected I18NBundle string;
    protected OrthographicCamera cam;
    protected Vector3 mouse;

    protected State (GameStateManager gsm){
        this.gsm = gsm;
        cam = new OrthographicCamera();
        mouse = new Vector3();
        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        string = I18NBundle.createBundle(baseFileHandle);
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();

}