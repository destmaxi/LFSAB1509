package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen extends Screen {

    private Stage stage;

    PauseScreen(GravityRun gravityRun) {
        super(gravityRun);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        TextButton continueButton = new TextButton(i18n.format("continue"), tableSkin, "round");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
            }
        });
        TextButton quitButton = new TextButton(i18n.format("quit"), tableSkin, "round");
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
                screenManager.pop();
            }
        });

        Label score = new Label(i18n.format("score", PlayScreen.score), aaronScoreSkin);

        Label title = new Label(i18n.format("pause"), titleSkin, "title");

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((width - cw) / 2, (height - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(score).padTop(height - ch);
        table.row();
        table.add(continueButton).expandX().fillX().padTop((height - ch) * 2);
        table.row();
        table.add(quitButton).expandX().fillX().padTop(height - ch);

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        stage.draw();
    }

    @Override
    public void update(float dt) {
        if (clickedBack())
            screenManager.pop();
    }

}
