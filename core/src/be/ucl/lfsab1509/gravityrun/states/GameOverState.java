package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Locale;

public class GameOverState extends State {

    private boolean isClickedMenuTextButton = false, isClickedReplayTextButton = false;
    private I18NBundle string;
    private Stage stage;
    private Skin buttonSkin, scoreSkin, titleSkin;

    public GameOverState(GameStateManager gsm) {
        super(gsm);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "BE", "VAR1");
        string = I18NBundle.createBundle(baseFileHandle, locale);

        titleSkin = new Skin();
        titleSkin.createSkin(62);
        Label title = new Label(string.format("game_over"), titleSkin, "title");

        scoreSkin = new Skin();
        scoreSkin.createSkin(28);
        Label score = new Label(string.format("final_score", GravityRun.lastScore), scoreSkin, "optional");

        buttonSkin = new Skin();
        buttonSkin.createSkin(42);
        TextButton menuButton = new TextButton(string.format("menu"), buttonSkin, "round");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedMenuTextButton = true;
            }
        });
        TextButton replayButton = new TextButton(string.format("replay"), buttonSkin, "round");
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedReplayTextButton = true;
            }
        });

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(title);
        table.row();
        table.add(score).padTop(Gdx.graphics.getHeight() * 150 / GravityRun.HEIGHT);
        table.row();
        table.add(replayButton).padTop(Gdx.graphics.getHeight() * 150 / GravityRun.HEIGHT);
        table.row();
        table.add(menuButton).padTop(Gdx.graphics.getHeight() * 30 / GravityRun.HEIGHT);

        stage = new Stage(new ScreenViewport());
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedMenuTextButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            gsm.pop();
        if (isClickedReplayTextButton)
            gsm.set(new PlayState(gsm));
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
    }

}
