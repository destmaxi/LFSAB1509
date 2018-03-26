package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

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
    private Skin menuSkin, tableSkin;
    private Stage stage;

    public MenuState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);

        float ch = h * 0.9f;
        float cw = w * 0.9f;

        menuSkin = new Skin();
        menuSkin.createSkin((int) (1.5f * w / d / 10));
        Label title = new Label(string.format("menu"), menuSkin, "title");

        tableSkin = new Skin();
        tableSkin.createSkin((int) (w / d / 10));
        TextButton optionButton = new TextButton(string.format("option"), tableSkin, "round");
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedOptionButton = true;
            }
        });
        TextButton startGameButton = new TextButton(string.format("new_game"), tableSkin, "round");
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedStartGameButton = true;
            }
        });
        Label hyLabel = new Label(string.format("hello") + GravityRun.pref.getString("username"), tableSkin);
        hyLabel.setWrap(true);
        hyLabel.setWidth(cw);
        hyLabel.setAlignment(Align.center);

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((w - cw) / 2, (h - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(hyLabel).expandX().width(cw).padTop(h - ch);
        table.row();
        table.add(optionButton).expandX().fillX().padTop(h - ch);
        table.row();
        table.add(startGameButton).expandX().fillX().padTop(h - ch);
        table.row();

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);
    }

    @Override
    public void handleInput() {
        if (isClickedOptionButton) {
            isClickedOptionButton = false;
            gsm.push(new OptionState(gsm, soundManager));
        }

        if (isClickedStartGameButton) {
            isClickedStartGameButton = false;
            soundManager.replayGame();
            gsm.push(new PlayState(gsm, soundManager));
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
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        menuSkin.dispose();
        stage.dispose();
        tableSkin.dispose();
    }

}