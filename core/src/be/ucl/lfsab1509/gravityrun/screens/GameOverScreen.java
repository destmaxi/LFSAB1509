package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

class GameOverScreen extends AbstractMenuScreen {
    private boolean multiplayer;

    GameOverScreen(GravityRun gravityRun, int finalScore, boolean multiplayer) {
        super(gravityRun);

        this.multiplayer = multiplayer;
        int previousHighScore = game.user.getHighScore();
        boolean isNewHighScore = game.user.addScore(finalScore);
        game.user.write();

        TextButton menuButton = new TextButton(game.i18n.format("menu"), game.tableSkin, "round");
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.pop();
                disconnect();
            }
        });

        TextButton replayButton = new TextButton(game.i18n.format("replay"), game.tableSkin, "round");
        replayButton.setVisible(isHost() || !isConnected());
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (multiplayer) {
                    int lives = game.user.getMultiLives();
                    int difficulty = game.user.getMulti_IndexSelected();
                    write("[" + 4 + ":" + lives + ":" + difficulty + ":" + game.user.getMultiMode() + "]#");
                    AbstractMultiPlayScreen abstractMultiPlayScreen = game.user.getMultiMode() == 0
                            ? new MultiPlayFirstModeScreen(game)
                            : new MultiPlaySecondModeScreen(game);

                    setMultiPlayScreen(abstractMultiPlayScreen);
                    screenManager.set(abstractMultiPlayScreen);
                } else
                    screenManager.set(new SoloPlayScreen(game));
            }
        });

        Label title = new Label(game.i18n.format("game_over"), game.titleSkin, "title");

        Table table = new Table();
        table.add(title).top();
        table.row();

        Label label1, label2;
        if (isNewHighScore) {
            label1 = newCenteredLabel(game.i18n.format("new_high_score", finalScore));
            label2 = newCenteredLabel(game.i18n.format("previous_high_score", previousHighScore));
        } else {
            label1 = newCenteredLabel(game.i18n.format("high_score", previousHighScore));
            label2 = newCenteredLabel(game.i18n.format("final_score", finalScore));
        }

        table.add(label1).padTop(height - containerHeight).width(containerWidth).expandX();
        table.row();
        table.add(label2).padTop(height - containerHeight).width(containerWidth).expandX();
        table.row();
        table.add(replayButton).expandX().fillX().padTop((height - containerHeight) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

    @Override
    public boolean isHost() {
        return bluetoothManager.isHost();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        handelInput();
        onDisconnect();
    }

    private void handelInput() {
        if (MultiplayerConnectionScreen.isClient && MultiplayerConnectionScreen.ready) {
            AbstractMultiPlayScreen abstractMultiPlayScreen = game.user.getMultiMode() == 0
                    ? new MultiPlayFirstModeScreen(game)
                    : new MultiPlaySecondModeScreen(game);

            setMultiPlayScreen(abstractMultiPlayScreen);
            screenManager.set(abstractMultiPlayScreen);
        }
    }

    private Label newCenteredLabel(String labelText) {
        Label label = new Label(labelText, game.aaronScoreSkin);
        label.setAlignment(Align.center);
        label.setWrap(true);
        return label;
    }

    private void onDisconnect() {
        if (!multiplayer || isConnected())
            return;

        MultiplayerConnectionScreen.ready = false;
        setMultiPlayScreen(new MultiPlayFirstModeScreen(game, false));

        while (!(screenManager.peek() instanceof HomeScreen))
            screenManager.pop();

        ((AbstractMenuScreen) screenManager.peek()).spawnErrorDialog(game.i18n.format("error_connection"), game.i18n.format("error_connection_lost"));
    }

}
