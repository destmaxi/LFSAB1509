package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class PauseScreen extends AbstractMenuScreen {

    PauseScreen(GravityRun gravityRun, int currentScore) {
        super(gravityRun);

        TextButton continueButton = new TextButton(game.i18n.format("continue"), game.tableSkin, "round");
        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
            }
        });
        TextButton quitButton = new TextButton(game.i18n.format("quit"), game.tableSkin, "round");
        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
                screenManager.pop();
            }
        });

        Label score = new Label(game.i18n.format("score", currentScore), game.aaronScoreSkin);
        Label title = new Label(game.i18n.format("pause"), game.titleSkin, "title");

        Table table = new Table();
        table.add(title).top();
        table.row();
        table.add(score).padTop(height - containerHeight);
        table.row();
        table.add(continueButton).expandX().fillX().padTop((height - containerHeight) * 2);
        table.row();
        table.add(quitButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }
}
