package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Locale;

public class PauseState extends State {

    private boolean isClickedContinue = false, isClickedQuit = false;
    private I18NBundle string;
    private Stage stage;
    private Skin buttonSkin, scoreSkin, titleSkin;

    protected PauseState(GameStateManager gsm) {
        super(gsm);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "BE", "VAR1");
        string = I18NBundle.createBundle(baseFileHandle, locale);

        // Gdx.input.setCatchBackKey(true);

        titleSkin = new Skin();
        titleSkin.createSkin(62);
        Label title = new Label(string.format("pause"), titleSkin, "title");

        scoreSkin = new Skin();
        scoreSkin.createSkin(28);
        Label score = new Label(string.format("score"), scoreSkin);

        buttonSkin = new Skin();
        buttonSkin.createSkin(42);
        TextButton continueButton = new TextButton(string.format("continue"), buttonSkin, "round");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedContinue = true;
            }
        });
        TextButton quitButton = new TextButton(string.format("quit"), buttonSkin, "round");
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedQuit = true;
            }
        });

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(title);
        table.row();
        table.add(score).padTop(Gdx.graphics.getHeight() * 150 / GravityRun.HEIGHT);
        table.row();
        table.add(continueButton).padTop(Gdx.graphics.getHeight() * 150 / GravityRun.HEIGHT);
        table.row();
        table.add(quitButton).padTop(Gdx.graphics.getHeight() * 30 / GravityRun.HEIGHT);

        stage = new Stage(new ScreenViewport());
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedContinue)// || Gdx.input.isKeyPressed(Input.Keys.BACK))
            gsm.pop();
        if (isClickedQuit) {
            gsm.pop();
            gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        stage.draw();
    }

    @Override
    public void dispose() {
        buttonSkin.dispose();
        scoreSkin.dispose();
        stage.dispose();
        titleSkin.dispose();
        System.out.println("PauseState disposed");
    }

}
