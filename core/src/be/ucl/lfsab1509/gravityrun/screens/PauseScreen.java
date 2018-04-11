package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PauseScreen extends AbstractMenuScreen {

    PauseScreen(GravityRun gravityRun) {
        super(gravityRun);

        TextButton continueButton = new TextButton(game.i18n.format("continue"), tableSkin, "round");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
            }
        });
        TextButton quitButton = new TextButton(game.i18n.format("quit"), tableSkin, "round");
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
                screenManager.pop();
            }
        });

        Label score = new Label(game.i18n.format("score", PlayScreen.score), aaronScoreSkin);
        Label title = new Label(game.i18n.format("pause"), titleSkin, "title");

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(containerWidth, containerHeight);
        tableContainer.setPosition((width - containerWidth) / 2, (height - containerHeight) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(score).padTop(height - containerHeight);
        table.row();
        table.add(continueButton).expandX().fillX().padTop((height - containerHeight) * 2);
        table.row();
        table.add(quitButton).expandX().fillX().padTop(height - containerHeight);

        stage.addActor(tableContainer);
    }

    @Override
    public void render(float dt) {
        if (clickedBack()) {
            screenManager.pop();
            return;
        }

        super.render(dt);
    }

}
