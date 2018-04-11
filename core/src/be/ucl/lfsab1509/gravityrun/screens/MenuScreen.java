package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuScreen extends Screen {

    private Label hyLabel;
    private Stage stage;

    public MenuScreen(GravityRun gravityRun) {
        super(gravityRun);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        Label title = new Label(i18n.format("menu"), titleSkin, "title");

        TextButton optionButton = new TextButton(i18n.format("option"), tableSkin, "round");
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new OptionScreen(game));
            }
        });
        // TODO ici, ça prend environ 10ms
        TextButton startGameButton = new TextButton(i18n.format("new_game"), tableSkin, "round");
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new PlayScreen(game));
            }
        });

        hyLabel = new Label("", tableSkin);
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
    }

    @Override
    public void dispose() {
        // TODO dispose of skins
        stage.dispose();
        tableSkin.dispose();
        titleSkin.dispose();
    }

    @Override
    public void render(float dt) {
        if (clickedBack()) {
            game.pref.put(game.user.toMap());
            game.pref.flush();

            game.exit();
            return;
        }

        game.spriteBatch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        hyLabel.setText(i18n.format("hello", game.user.getUsername()));
        soundManager.replayMenu();
    }

}