package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;
import be.ucl.lfsab1509.gravityrun.sprites.SpeedUp;

import java.util.Random;

import static be.ucl.lfsab1509.gravityrun.sprites.Bonus.NEW_LIFE;
import static be.ucl.lfsab1509.gravityrun.sprites.Bonus.SCORE_BONUS;
import static be.ucl.lfsab1509.gravityrun.sprites.Bonus.SPEED_UP;

public class MultiPlaySecondModeScreen extends AbstractMultiPlayScreen {

    private static final int LOST_LIFE = 1;
    private static final int LOST_SCORE_BONUS = 3;
    private static final int OPPONENT_SCORE = 5;
    private static final int SLOW_DOWN = 2;

    private boolean initialized = false, opponentDead = false;
    private Integer opponentLives = 0, opponentScore = 0;

    MultiPlaySecondModeScreen(GravityRun gravityRun) {
        super(gravityRun);

        randomBonus = new Random();
        randomObstacle = new Random();
    }

    @Override
    public void applyMessage(String[] message) {
        super.applyMessage(message);

        switch (getIntegerFromStr(message[0])) {
            case LOST_LIFE:
                if (!playerMarble.isInvincible())
                    playerMarble.addMarbleLife(-1);
                break;
            case LOST_SCORE_BONUS:
                playerMarble.decreaseScoreBonus();
                break;
            case OPPONENT_SCORE:
                setOpponentScore(message);
                break;
            case OPPONENT_DEAD:
                opponentDead = true;
                write("[" + ACK_DEAD + "]");
                break;
            case OPPONENT_LIVES:
                setOpponentLives(message);
                break;
            case SLOW_DOWN:
                SlowDown slowdown = new SlowDown(0, 0, this, new Random(), slowDownImage);
                slowdown.activateSlowdown(playerMarble);
                playerMarble.addCaughtBonuses(slowdown);
                break;
            case SPEED_UP:
                SpeedUp speedUp = new SpeedUp(0, 0, this, new Random(), speedUpImage);
                speedUp.activateSpeedUp(playerMarble);
                playerMarble.addCaughtBonuses(speedUp);

        }
    }

    @Override
    void bonusCollides(Bonus bonus, int i, Marble marble) {
        switch (bonus.getValue()) {
            case NEW_LIFE:
                write("[" + LOST_LIFE + "]#");
                break;
            case SCORE_BONUS:
                write("[" + LOST_SCORE_BONUS + "]#");
                break;
            case SLOW_DOWN:
                write("[" + SLOW_DOWN + "]#");
                break;
            case SPEED_UP:
                write("[" + SPEED_UP + "]#");
        }

        super.bonusCollides(bonus, i, marble);
    }

    @Override
    void handleEndGame() {
        if (!gameOver)
            return;

        super.handleEndGame();

        screenManager.set(new MultiplayerGameOverScreen(game, playerMarble.getScore(), opponentScore));
    }

    @Override
    public void initGame(float dt) {
        super.initGame(dt);

        if (!initDone && !initialized && opponentReady) {
            initObstacles();
            initBonuses();
            initialized = true;
        }
    }

    @Override
    public void initMarbles() {
        playerMarble = new Marble(true, true, game.user.getMultiIndexSelected() + 1, STANDARD_WIDTH, width / 2, height / 10, game.sensorHelper, marblesImage, marblesInvincibleImage);
        playerMarble.setLives(game.user.getMultiLives());
        marbles.add(playerMarble);
    }

    @Override
    void renderLoseWin() {
        if (gameOver)
            renderLoseWin(playerMarble.getScore() >= opponentScore ? youWinImage : youLoseImage);
        else
            super.renderLoseWin();
    }

    @Override
    public void renderMarbles() {
        playerMarble.render(game.spriteBatch);
    }

    @Override
    void update(float dt) {
        super.update(dt);

        if (opponentDead && playerMarble.isDead())
            gameOver = true;
        else
            won = opponentDead;
    }

    @Override
    public void updateMarbles(float dt) {
        super.updateMarbles(dt);

        if (!playerMarble.isDead())
            write("[" + OPPONENT_SCORE + ":" + playerMarble.getScore() + "]#");
    }

    @Override
    void updateOpponentScore() {
        opponentLivesLabel.setText(opponentLives.toString());
        opponentScoreLabel.setText(opponentScore.toString());
    }

    private void setOpponentLives(String[] message) {
        try {
            opponentLives = getIntegerFromStr(message[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void setOpponentScore(String[] message) {
        try {
            opponentScore = getIntegerFromStr(message[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}
