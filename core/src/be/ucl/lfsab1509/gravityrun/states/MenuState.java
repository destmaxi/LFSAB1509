package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuState extends State {

    private boolean isClickedOptionButton = false, isClickedStartGameButton = false;
    private Label hyLabel;
    private Stage stage;

    public MenuState(GravityRun game, SoundManager soundManager) {
        super(game, soundManager);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        Label title = new Label(i18n.format("menu"), titleSkin, "title");

        TextButton optionButton = new TextButton(i18n.format("option"), tableSkin, "round");
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedOptionButton = true;
            }
        });
        // TODO ici, ça prend environ 10ms
        TextButton startGameButton = new TextButton(i18n.format("new_game"), tableSkin, "round");
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedStartGameButton = true;
            }
        });

        hyLabel = new Label(i18n.format("hello", GravityRun.pref.getString("username")), tableSkin);
        hyLabel.setWrap(true);
        hyLabel.setWidth(cw);
        hyLabel.setAlignment(Align.center);

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((width - cw) / 2, (height - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(hyLabel).expandX().width(cw).padTop(height - ch);
        table.row();
        table.add(optionButton).expandX().fillX().padTop(height - ch);
        table.row();
        table.add(startGameButton).expandX().fillX().padTop(height - ch);
        table.row();

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);

        render(game.batch);
    }

    @Override
    public void handleInput() {
        if (isClickedOptionButton) {
            isClickedOptionButton = false;
//            gameStateManager.push(new OptionState(gameStateManager, soundManager));
            game.setScreen(new OptionState(game, soundManager));
        }

        if (isClickedStartGameButton) {
            isClickedStartGameButton = false;
            soundManager.replayGame();
//            gameStateManager.push(new PlayState(gameStateManager, soundManager));
            game.setScreen(new PlayState(game, soundManager));
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
        hyLabel.setText(i18n.format("hello", GravityRun.user.getUsername()));
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // TODO dispose of skins
        titleSkin.dispose();
        stage.dispose();
        tableSkin.dispose();
    }

}