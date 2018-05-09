package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;

public class MultiPlaySecondModeScreen extends AbstractPlayScreen {
    private Label opponentScoreLabel;
    int opponentScore = 0;
    private boolean opponentReady = false;


    public MultiPlaySecondModeScreen(GravityRun gravityRun) {
        super(gravityRun);
        opponentScoreLabel = new Label(game.i18n.format("opponent_score", opponentScore), game.aaronScoreSkin, "score");
        opponentScoreLabel.setPosition((GravityRun.WIDTH - opponentScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * opponentScoreLabel.getHeight());

        scoreStage.addActor(opponentScoreLabel);
    }

    void applyMessage(String[] message) {
        applyMessage(message);

        int messageType = getIntegerFromStr(message[0]);
        switch (messageType) {
            case 13:
                playerMarble.addMarbleLife(-1);
                break;
            case 14:
                Bonus slowDown = new SlowDown(0, 0, playerMarble, this, new Random(), slowDownImage);
                ((SlowDown) slowDown).activateSlowDown();
                catchedBonuses.add(slowDown);
                break;
            case 15:
                score -= 100;
                break;
            case 16:
                try {
                    opponentScore = getIntegerFromStr(message[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void disposeMarbles() {
        playerMarble.dispose();
    }

    @Override
    public void initGame(float dt) {
        if (initialized || !opponentReady)
            return;

        initMarbles();
        initObstacles();
        initBonuses();
        initialized = true;
    }

    @Override
    public void initMarbles() {
        playerMarble = new Marble(true, false, width / 2, height / 10, STANDARD_WIDTH, game.user.getMulti_IndexSelected(), this);
    }

    @Override
    public boolean isInitDone() {
        return false;
    }

    @Override
    public void renderMarbles() {
        playerMarble.render(game.spriteBatch);
    }

    @Override
    void update(float dt) {

    }

    @Override
    public void updateMarbles(float dt) {
        playerMarble.update(dt, died);
    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {

    }
}
