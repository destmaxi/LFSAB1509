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

    GameOverScreen(GravityRun gravityRun, AbstractPlayScreen playScreen) {
        this(gravityRun, playScreen instanceof AbstractMultiPlayScreen, playScreen.playerMarble.getScore());

        if (multiplayer)
            return;

        boolean deadBottom = playScreen.deadBottom;
        boolean deadHole = playScreen.deadHole;
        int difficulty = playScreen.playerMarble.getDifficulty();
        int finalScore = playScreen.playerMarble.getScore();
        int nbInvincible = playScreen.nbInvincible;
        int nbNewLife = playScreen.nbNewLife;
        int nbScoreBonus = playScreen.nbScoreBonus;
        int nbSlowDown = playScreen.nbSlowDown;

        submitGpgs(finalScore, difficulty, deadBottom, deadHole, nbInvincible, nbNewLife, nbScoreBonus, nbSlowDown);

    }

    GameOverScreen(GravityRun gravityRun, boolean multiplayer, int finalScore) {
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

    private void submitGpgs(int score, int difficulty, boolean deadBottom, boolean deadHole, int nbInvincible, int nbNewLife, int nbScoreBonus, int nbSlowDown) {
        if (score >= 1_000)
            game.gpgs.unlockAchievement("SCORE_1_000");
        if (score >= 10_000)
            game.gpgs.unlockAchievement("SCORE_10_000");
        if (score >= 100_000)
            game.gpgs.unlockAchievement("SCORE_100_000");
        if (score >= 1_000_000)
            game.gpgs.unlockAchievement("SCORE_1_000_000");

        if (deadBottom) {
            game.gpgs.incrementAchievement("BOTTOM_100", 1);
            game.gpgs.incrementAchievement("BOTTOM_500", 1);
            game.gpgs.incrementAchievement("BOTTOM_1000", 1);
        }
        if (deadHole) {
            game.gpgs.incrementAchievement("HOLE_100", 1);
            game.gpgs.incrementAchievement("HOLE_500", 1);
            game.gpgs.incrementAchievement("HOLE_1000", 1);
        }

        if (nbInvincible >= 10)
            game.gpgs.unlockAchievement("INVINCIBLE_10");
        if (nbInvincible >= 50)
            game.gpgs.unlockAchievement("INVINCIBLE_50");
        if (nbInvincible >= 100)
            game.gpgs.unlockAchievement("INVINCIBLE_100");

        if (nbNewLife >= 10)
            game.gpgs.unlockAchievement("NEWLIFE_10");
        if (nbNewLife >= 50)
            game.gpgs.unlockAchievement("NEWLIFE_50");
        if (nbNewLife >= 100)
            game.gpgs.unlockAchievement("NEWLIFE_100");

        if (nbScoreBonus >= 10)
            game.gpgs.unlockAchievement("SCOREBONUS_10");
        if (nbScoreBonus >= 50)
            game.gpgs.unlockAchievement("SCOREBONUS_50");
        if (nbScoreBonus >= 100)
            game.gpgs.unlockAchievement("SCOREBONUS_100");

        if (nbSlowDown >= 10)
            game.gpgs.unlockAchievement("SLOWDOWN_10");
        if (nbSlowDown >= 50)
            game.gpgs.unlockAchievement("SLOWDOWN_50");
        if (nbSlowDown >= 100)
            game.gpgs.unlockAchievement("SLOWDOWN_100");

        game.gpgs.submitScore("LEADERBOARD_" + difficulty, score);
    }

}
