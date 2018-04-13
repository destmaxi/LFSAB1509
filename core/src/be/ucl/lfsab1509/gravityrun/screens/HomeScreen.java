package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class HomeScreen extends AbstractMenuScreen {

    private final ImageButton gpgsButton;
    private Label hyLabel;
    private Texture gpgsImage;

    public HomeScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.format("menu"), titleSkin, "title");

        gpgsImage = new Texture("drawable-" + calculateStandardWidth() + "/gpgs.png");

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

        TextureRegion gpgsRegionConnected = new TextureRegion(gpgsImage, 0, 0, gpgsImage.getWidth(), gpgsImage.getHeight() / 2);
        TextureRegion gpgsRegionDisconnected = new TextureRegion(gpgsImage, 0, gpgsImage.getHeight() / 2, gpgsImage.getWidth(), gpgsImage.getHeight() / 2);
        gpgsButton = new ImageButton(new TextureRegionDrawable(gpgsRegionDisconnected), new TextureRegionDrawable(gpgsRegionConnected), new TextureRegionDrawable(gpgsRegionConnected));
        gpgsButton.setChecked(game.gsClient.isSessionActive());
        gpgsButton.setPosition(width - gpgsImage.getWidth(), 0);
        gpgsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connect();
            }
        });
        stage.addActor(gpgsButton);

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
        gpgsImage.dispose();
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

    private void connect() {
        if (game.gsClient.isSessionActive())
            game.gsClient.logOff();
        else {
            if (!game.gsClient.logIn())
                Gdx.app.error("GPGS_ERROR", "Cannot sign in");
        }
        gpgsButton.setChecked(game.gsClient.isSessionActive());
    }

}