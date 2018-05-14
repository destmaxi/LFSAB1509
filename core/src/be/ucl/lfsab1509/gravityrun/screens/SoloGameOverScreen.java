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

class SoloGameOverScreen extends AbstractGameOverScreen {


    SoloGameOverScreen(GravityRun gravityRun, AbstractPlayScreen playScreen) {
        super(gravityRun);

        boolean deadBottom = playScreen.deadBottom;
        boolean deadHole = playScreen.deadHole;
        int difficulty = playScreen.playerMarble.getDifficulty();
        int finalScore = playScreen.playerMarble.getScore();
        int nbInvincible = playScreen.nbInvincible;
        int nbNewLife = playScreen.nbNewLife;
        int nbScoreBonus = playScreen.nbScoreBonus;
        int nbSlowDown = playScreen.nbSlowDown;


        int previousHighScore = game.user.getHighScore();
        boolean isNewHighScore = game.user.addScore(finalScore);
        game.user.write();

        title.setText(game.i18n.format("game_over"));

        if (isNewHighScore) {
            label1.setText(game.i18n.format("new_high_score", finalScore));
            label2.setText(game.i18n.format("previous_high_score", previousHighScore));
        } else {
            label1.setText(game.i18n.format("high_score", previousHighScore));
            label2.setText(game.i18n.format("final_score", finalScore));
        }

        submitGpgs(finalScore, difficulty, deadBottom, deadHole, nbInvincible, nbNewLife, nbScoreBonus, nbSlowDown);

    }

    @Override
    void backToMenu() {
        screenManager.pop();
    }

    @Override
    void handleInput() {

    }

    @Override
    public boolean isHost() {
        return true;
    }

    @Override
    void replayGame() {
        screenManager.set(new SoloPlayScreen(game));
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
