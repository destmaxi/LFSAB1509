package be.ucl.lfsab1509.gravityrun.screens;

import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;

public class MultiPlaySecondModeScreen extends AbstractMultiPlayScreen {

    private static final int LOST_LIFE = 1;
    private static final int SLOWDOWN = 2;
    private static final int LOST_SCORE_BONUS = 3;
    private static final int OPPONENT_SCORE = 5;

    private Integer opponentScore = 0, opponentLives = 0;
    private boolean initialized = false, opponentDead = false;

    MultiPlaySecondModeScreen(GravityRun gravityRun) {
        super(gravityRun);

        randomBonus = new Random();
        randomObstacle = new Random();
    }

    @Override
    public void applyMessage(String[] message) {
        super.applyMessage(message);
        int messageType = getIntegerFromStr(message[0]);
        switch (messageType) {
            case LOST_LIFE:
                playerMarble.addMarbleLife(-1);
                break;
            case SLOWDOWN:
                Bonus slowdown = new SlowDown(0, 0, this, new Random(), slowDownImage);
                ((SlowDown) slowdown).activateSlowdown(playerMarble);
                playerMarble.addCaughtBonuses(slowdown);
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
        }
    }

    @Override
    void bonusCollides(Bonus bonus, int i, Marble marble) {
        switch (bonus.getValue()) {
            case 1:
                write("[" + LOST_LIFE + "]#");
                break;
            case 2:
                write("[" + SLOWDOWN + "]#");
                break;
            case 3:
                write("[" + LOST_SCORE_BONUS + "]#");
                break;
        }

        super.bonusCollides(bonus, i, marble);
    }

    @Override
    public void initGame(float dt) {

        if (initDone)
            return;

        if (!opponentReady) {
            write("[" + INIT_MESSAGE + "]#");
            return;
        }

        super.initGame(dt);

        if (!initialized) {
            initObstacles();
            initBonuses();
            initialized = true;
        }
    }

    @Override
    public void initMarbles() {
        playerMarble = new Marble(true, true, width / 2, height / 10, STANDARD_WIDTH, game.user.getMulti_IndexSelected() + 1);
        playerMarble.setLives(game.user.getMultiLives());
        marbles.add(playerMarble);
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
    }

    @Override
    void renderGameOver() {
        if (playerMarble.isDead())
            game.spriteBatch.draw(gameOverImage,
                    camera.position.x - gameOverImage.getWidth() / 2,
                    camera.position.y);
    }

    @Override
    public void updateMarbles(float dt) {
        super.updateMarbles(dt);

        if (!playerMarble.isDead())
            write("[" + OPPONENT_SCORE + ":" + playerMarble.getScore() + "]#");
    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {

    }

    @Override
    void updateOpponentScore() {
        opponentScoreLabel.setText(opponentScore.toString());
        opponentLivesLabel.setText(opponentLives.toString());
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
