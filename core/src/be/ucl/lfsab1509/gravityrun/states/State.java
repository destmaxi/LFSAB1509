package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.I18NBundle;

public abstract class State {

    static float h, w;
    static Skin aaronScoreSkin, labelScoreBoardSkin, tableSkin, tableScoreBoardSkin, titleSkin;

    GameStateManager gsm;
    I18NBundle i18n;
    OrthographicCamera cam;
    SoundManager soundManager;

    State (GameStateManager gsm, SoundManager soundManager) {
        this.gsm = gsm;
        cam = new OrthographicCamera();
        this.soundManager = soundManager;
        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        i18n = I18NBundle.createBundle(baseFileHandle);
    }

    public static void initializeSkins() {
        float d = GravityRun.DENSITY;
        h = GravityRun.HEIGHT;
        w = GravityRun.WIDTH;

        tableScoreBoardSkin = new Skin();
        tableScoreBoardSkin.createSkin((int) (0.5f * w / d / 10));

        aaronScoreSkin = new Skin();
        aaronScoreSkin.createSkin((int) (0.75f * w / d / 10));

        labelScoreBoardSkin = new Skin();
        labelScoreBoardSkin.createSkin((int) (0.9f * w / d / 10));

        tableSkin = new Skin();
        tableSkin.createSkin((int) (w / d / 10));

        titleSkin = new Skin();
        titleSkin.createSkin((int)(1.5f * w / d / 10));
    }

    protected abstract void handleInput();

    public abstract void update(float dt);

    public abstract void render(SpriteBatch sb);

    public abstract void dispose();

}