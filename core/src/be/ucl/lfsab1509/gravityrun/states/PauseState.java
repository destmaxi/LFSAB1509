package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseState extends State {

    private boolean isClickedContinue = false, isClickedQuit = false;
    private Stage stage;

    PauseState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);

        float ch = h * 0.9f;
        float cw = w * 0.9f;

        TextButton continueButton = new TextButton(i18n.format("continue"), tableSkin, "round");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedContinue = true;
            }
        });
        TextButton quitButton = new TextButton(i18n.format("quit"), tableSkin, "round");
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedQuit = true;
            }
        });

        Label score = new Label(i18n.format("score", PlayState.score), aaronScoreSkin);

        Label title = new Label(i18n.format("pause"), titleSkin, "title");

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((w - cw) / 2, (h - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(score).padTop(h - ch);
        table.row();
        table.add(continueButton).expandX().fillX().padTop((h - ch) * 2);
        table.row();
        table.add(quitButton).expandX().fillX().padTop(h - ch);

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedContinue || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            gsm.pop();
        if (isClickedQuit) {
            soundManager.replayMenu();
            gsm.pop();
            gsm.pop();
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
        stage.dispose();
    }

}
