package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

class SoloGameOverScreen extends AbstractGameOverScreen {

    SoloGameOverScreen(GravityRun gravityRun, AbstractPlayScreen playScreen) {
        super(gravityRun);

        int finalScore = playScreen.playerMarble.getScore();
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

        submitGpgs(playScreen);
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

    private void gpgsIncrement(boolean dead, int[] deads, String string) {
        for (int d : deads)
            if (dead)
                game.gpgs.incrementAchievement(string + d, 1);
    }

    private void gpgsUnlock(int score, int[] scores, String string) {
        for (int s : scores)
            if (score >= s)
                game.gpgs.unlockAchievement(string + s);
    }

    private void submitGpgs(AbstractPlayScreen playScreen) {
        int[] scores = {1_000, 10_000, 100_000, 1_000_000};
        gpgsUnlock(playScreen.playerMarble.getScore(), scores, "SCORE_");

        int[] nbDead = {100, 500, 1000};
        gpgsIncrement(playScreen.deadBottom, nbDead, "BOTTOM_");
        gpgsIncrement(playScreen.deadHole, nbDead, "HOLE_");

        int[] nbBonuses = {10, 50, 100};
        gpgsUnlock(playScreen.nbInvincible, nbBonuses, "INVINCIBLE_");
        gpgsUnlock(playScreen.nbNewLife, nbBonuses, "NEW_LIFE_");
        gpgsUnlock(playScreen.nbScoreBonus, nbBonuses, "SCORE_BONUS_");
        gpgsUnlock(playScreen.nbSlowDown, nbBonuses, "SLOW_DOWN_");
        gpgsUnlock(playScreen.nbSpeedUp, nbBonuses, "SPEED_UP_");

        game.gpgs.submitScore("LEADERBOARD_" + playScreen.playerMarble.getDifficulty(), playScreen.playerMarble.getScore());
    }

}
