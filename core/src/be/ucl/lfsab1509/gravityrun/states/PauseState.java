package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.tools.Skin;
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
    private Skin buttonSkin, scoreSkin, titleSkin;

    PauseState(GameStateManager gsm) {
        super(gsm);

        titleSkin = new Skin();
        titleSkin.createSkin(62);
        Label title = new Label(string.format("pause"), titleSkin, "title");

        scoreSkin = new Skin();
        scoreSkin.createSkin(28);
        Label score = new Label(string.format("score", PlayState.score), scoreSkin);

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

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();
        stage = new Stage(new ScreenViewport());

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2 );
        tableContainer.top().fillX();

        table.add(title).top();
        table.row();
        table.add(score).padTop(sh - ch);
        table.row();
        table.add(continueButton).expandX().fillX().padTop((sh - ch) * 2);
        table.row();
        table.add(quitButton).expandX().fillX().padTop(sh - ch);

        stage = new Stage(new ScreenViewport());
        tableContainer.setActor(table);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedContinue || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            gsm.pop();
        if (isClickedQuit) {
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
        buttonSkin.dispose();
        scoreSkin.dispose();
        stage.dispose();
        titleSkin.dispose();
    }

}
