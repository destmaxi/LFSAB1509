package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class HomeScreen extends AbstractMenuScreen {

    private Label hyLabel;

    public HomeScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.format("menu"), titleSkin, "title");

        TextButton startGameButton = new TextButton(game.i18n.format("new_game"), tableSkin, "round");
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundManager.replayGame(); // FIXME le placement de cette instruction laisse à désirer. 20 msec de délai
                screenManager.push(new PlayScreen(game));
            }
        });
        TextButton scoreBoardButton = new TextButton(game.i18n.format("my_score"), tableSkin, "round");
        scoreBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new ScoreboardScreen(game));
            }
        });
        TextButton optionButton = new TextButton(game.i18n.format("option"), tableSkin, "round");
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new OptionScreen(game));
            }
        });
        // TODO ici, ça prend environ 10ms

        hyLabel = new Label("", tableSkin);
        hyLabel.setAlignment(Align.center);
        hyLabel.setWidth(containerWidth);
        hyLabel.setWrap(true);

        Table table = new Table();
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

        initStage(table);
    }

    @Override
    public void dispose() {
        super.dispose();
        disposeSkins();
    }

    @Override
    public void render(float dt) {
        if (clickedBack()) {
            user.write();
            game.exit();
            return;
        }

        super.render(dt);
    }

    @Override
    public void show() {
        super.show();
        hyLabel.setText(game.i18n.format("hello", user.getUsername()));
    }

}