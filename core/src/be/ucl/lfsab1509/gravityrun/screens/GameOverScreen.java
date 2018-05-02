package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class GameOverScreen extends AbstractMenuScreen {

    GameOverScreen(GravityRun gravityRun, int finalScore, int difficulty, boolean deadBottom, boolean deadHole, int nbInvincible, int nbNewLife, int nbScoreBonus, int nbSlowDown) {
        super(gravityRun);

        int previousHighScore = game.user.getHighScore();
        boolean isNewHighScore = game.user.addScore(finalScore);

        submitGpgs(finalScore, difficulty, deadBottom, deadHole, nbInvincible, nbNewLife, nbScoreBonus, nbSlowDown);

        TextButton menuButton = new TextButton(game.i18n.format("menu"), game.tableSkin, "round");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
            }
        });
        TextButton replayButton = new TextButton(game.i18n.format("replay"), game.tableSkin, "round");
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.set(new PlayScreen(game));
            }
        });

        Label title = new Label(game.i18n.format("game_over"), game.titleSkin, "title");

        Table table = new Table();
        table.add(title).top();
        table.row();

        if (isNewHighScore) {
            Label newHighScoreLabel = new Label(game.i18n.format("new_high_score", finalScore), game.aaronScoreSkin);
            newHighScoreLabel.setAlignment(Align.center);
            newHighScoreLabel.setWrap(true);
            Label previousHighScoreLabel = new Label(game.i18n.format("previous_high_score", previousHighScore), game.aaronScoreSkin);
            previousHighScoreLabel.setAlignment(Align.center);
            previousHighScoreLabel.setWrap(true);
            table.add(newHighScoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
            table.add(previousHighScoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
        } else {
            Label highScoreLabel = new Label(game.i18n.format("high_score", previousHighScore), game.aaronScoreSkin);
            highScoreLabel.setAlignment(Align.center);
            highScoreLabel.setWrap(true);
            Label scoreLabel = new Label(game.i18n.format("final_score", finalScore), game.aaronScoreSkin);
            scoreLabel.setAlignment(Align.center);
            scoreLabel.setWrap(true);
            table.add(scoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
            table.add(highScoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
        }

        table.add(replayButton).expandX().fillX().padTop((height - containerHeight) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

    @Override
    public void hide() {
        game.user.write();
        super.hide();
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
