package be.ucl.lfsab1509.gravityrun.screens;

import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;

public class MultiPlaySecondModeScreen extends AbstractMultiPlayScreen {

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
        int messageType = getIntegerFromStr(message[0]);
        switch (messageType) {
            case 1:
                playerMarble.addMarbleLife(-1);
                break;
            case 2:
                Bonus slowDown = new SlowDown(0, 0, this, new Random(), slowDownImage);
                ((SlowDown) slowDown).activateSlowdown(playerMarble);
                playerMarble.addCaughtBonuses(slowDown);
                break;
            case 3:
                playerMarble.decreaseScoreBonus();
                break;
            case 5:
                try {
                    opponentScore = getIntegerFromStr(message[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                opponentDead = true;
                write("[9]");
                break;
            case 13:
                try {
                    opponentLives = getIntegerFromStr(message[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    void bonusCollides(Bonus bonus, int i, Marble marble) {
        switch (bonus.getValue()) {
            case 1:
                write("[1]#");
                break;
            case 2:
                write("[2]#");
                break;
            case 3:
                write("[3]#");
                break;
        }

        super.bonusCollides(bonus, i, marble);
    }

    @Override
    public void initGame(float dt) {
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
    void renderGameOver() {
        if (playerMarble.isDead())
            game.spriteBatch.draw(gameOverImage,
                    camera.position.x - gameOverImage.getWidth() / 2,
                    camera.position.y);
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
    public void updateMarbles(float dt) {
        super.updateMarbles(dt);

        if (!playerMarble.isDead())
            write("[5:" + playerMarble.getScore() + "]#");
    }

    @Override
    void updateOpponentScore() {
        opponentLivesLabel.setText(opponentLives.toString());
        opponentScoreLabel.setText(opponentScore.toString());
    }

}
