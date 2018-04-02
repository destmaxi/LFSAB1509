package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class HomeState extends AbstractMenuState {

    private boolean isClickedStartGameButton = false;
    private boolean isClickedScoreBoardButton = false;
    private boolean isClickedOptionButton = false;
    private Label hyLabel;

    public HomeState(GameStateManager gameStateManager, SoundManager soundManager) {
        super(gameStateManager, soundManager);

        Label title = new Label(GravityRun.i18n.format("menu"), titleSkin, "title");

        TextButton startGameButton = new TextButton(GravityRun.i18n.format("new_game"), tableSkin, "round");
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedStartGameButton = true;
            }
        });
        TextButton scoreBoardButton = new TextButton(GravityRun.i18n.format("my_score"), tableSkin, "round");
        scoreBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedScoreBoardButton = true;
            }
        });
        TextButton optionButton = new TextButton(GravityRun.i18n.format("option"), tableSkin, "round");
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedOptionButton = true;
            }
        });
        // TODO ici, ça prend environ 10ms

        hyLabel = new Label(GravityRun.i18n.format("hello", GravityRun.pref.getString("username")), tableSkin);
        hyLabel.setWrap(true);
        hyLabel.setWidth(containerWidth);
        hyLabel.setAlignment(Align.center);

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(containerWidth, containerHeight);
        tableContainer.setPosition((width - containerWidth) / 2, (height - containerHeight) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(hyLabel).expandX().width(containerWidth).padTop(height - containerHeight);
        table.row();
        table.add(startGameButton).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(scoreBoardButton).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(optionButton).expandX().fillX().padTop(height - containerHeight);
        table.row();

        stage.addActor(tableContainer);
    }

    @Override
    public void handleInput() {
        if (isClickedStartGameButton) {
            isClickedStartGameButton = false;
            soundManager.replayGame(); // FIXME le placement de cette instruction laisse à désirer. 20 msec de délai
            gameStateManager.push(new PlayState(gameStateManager, soundManager));
        }

        if (isClickedScoreBoardButton) {
            isClickedScoreBoardButton = false;
            gameStateManager.push(new ScoreboardState(gameStateManager, soundManager));
        }

        if (isClickedOptionButton) {
            isClickedOptionButton = false;
            gameStateManager.push(new OptionState(gameStateManager, soundManager));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(stage);
        hyLabel.setText(GravityRun.i18n.format("hello", GravityRun.user.getUsername()));
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        State.disposeSkins();
    }
}