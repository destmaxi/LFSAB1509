package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public abstract class AbstractGameOverScreen extends AbstractMenuScreen {

    Label label1, label2;

    AbstractGameOverScreen(GravityRun gravityRun) {
        super(gravityRun);

        TextButton menuButton = new TextButton(game.i18n.format("menu"), game.tableSkin, "round");
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backToMenu();
            }
        });

        TextButton replayButton = new TextButton(game.i18n.format("replay"), game.tableSkin, "round");
        replayButton.setVisible(isHost());
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                replayGame();
            }
        });

        Label title = new Label(game.i18n.format("game_over"), game.titleSkin, "title");

        Table table = new Table();
        table.add(title).top();
        table.row();

        label1 = newCenteredLabel("");
        label2 = newCenteredLabel("");

        table.add(label1).padTop(height - containerHeight).width(containerWidth).expandX();
        table.row();
        table.add(label2).padTop(height - containerHeight).width(containerWidth).expandX();
        table.row();
        table.add(replayButton).expandX().fillX().padTop((height - containerHeight) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

    abstract void backToMenu();
    abstract void handleInput();
    abstract void replayGame();

    @Override
    public void render(float dt) {
        super.render(dt);
        handleInput();
    }

    private Label newCenteredLabel(String labelText) {
        Label label = new Label(labelText, game.aaronScoreSkin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        return label;
    }

}
