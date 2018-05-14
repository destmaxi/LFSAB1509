package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;
import java.util.Random;

public class MultiPlayFirstModeScreen extends AbstractMultiPlayScreen {

    private static final int INIT_RANDOMS = 3;
    private static final int OPPONENT_CAUGHT_BONUS = 2;
    private static final int OPPONENT_MARBLE_INHOLE = 14;
    private static final int POSITION_OPPONENT_MARBLE = 5;
    private static final int REPOSITION_OPPONENT_MARBLE = 1;

    private ArrayList<Integer> opponentCaughtBonusIds;
    private boolean gotSeed = false, initialized, positionRecover = false;
    private float opponentMarblePositionUpdateTime;
    private int bonusId;
    private long seed;
    private Marble opponentMarble;
    private Texture opponentMarblesImage, opponentMarblesInvincibleImage;

    public MultiPlayFirstModeScreen(GravityRun gravityRun, boolean startMultiPlayScreen) {
        super(gravityRun, startMultiPlayScreen);
    }

    MultiPlayFirstModeScreen(GravityRun gravityRun) {
        super(gravityRun);

        opponentCaughtBonusIds = new ArrayList<>();

        if (isHost()) {
            seed = System.currentTimeMillis();
            randomObstacle = new Random(seed);
            randomBonus = new Random(seed);
        }
    }

    @Override
    public void applyMessage(String[] message) {
        super.applyMessage(message);

        switch (getIntegerFromStr(message[0])) {
            case INIT_RANDOMS:
                initRandoms(message);
                break;
            case OPPONENT_CAUGHT_BONUS:
                addOpponentCaughtBonusIds(message);
                break;
            case OPPONENT_DEAD:
                opponentMarble.setDead();
                write("[" + ACK_DEAD + "]#");
                break;
            case OPPONENT_LIVES:
                setOpponentMarbleLives(message);
                break;
            case OPPONENT_MARBLE_INHOLE:
                opponentMarble.setInHole();
                break;
            case POSITION_OPPONENT_MARBLE:
                addPositionToOpponentMarble(message);
                break;
            case REPOSITION_OPPONENT_MARBLE:
                repositionOpponentMarble();
                break;
        }
    }

    @Override
    void bonusCollides(Bonus bonus, int i, Marble marble) {
        write("[" + OPPONENT_CAUGHT_BONUS + ":" + bonus.getBonusId() + "]");
        if (bonus instanceof CamReposition)
            write("[" + REPOSITION_OPPONENT_MARBLE + "]#");

        super.bonusCollides(bonus, i, marble);
    }

    @Override
    void disposeTextures() {
        super.disposeTextures();
        opponentMarblesImage.dispose();
        opponentMarblesInvincibleImage.dispose();
    }

    @Override
    void handleEndGame() {
        if (!gameOver)
            return;

        super.handleEndGame();

        screenManager.set(new MultiplayerGameOverScreen(game, playerMarble.getScore(), opponentMarble.getScore()));
    }

    @Override
    public void initGame(float dt) {
        super.initGame(dt);

        if (initDone || !opponentReady)
            return;

        if (gotSeed) {
            initObstacles();
            initBonuses();
            gotSeed = false;
        } else if (isHost() && !initialized) {
            write("[" + INIT_RANDOMS + ":" + seed + "]#");
            initialized = true;
            gotSeed = true;
        }
    }

    @Override
    void initializeTextures() {
        super.initializeTextures();
        opponentMarblesImage = new Texture("drawable-" + calculateStandardWidth(width) + "/marbles_opponent.png");
        opponentMarblesInvincibleImage = new Texture("drawable-" + calculateStandardWidth(width) + "/marbles_opponent_invincible.png");
    }

    @Override
    public void initMarbles() {
        int level = game.user.getMultiIndexSelected() + 1;

        playerMarble = isHost()
                ? new Marble(true, true, level, STANDARD_WIDTH, width / 3, height / 10, game.sensorHelper, marblesImage, marblesInvincibleImage)
                : new Marble(true, true, level, STANDARD_WIDTH, width * 2 / 3, height / 10, game.sensorHelper, marblesImage, marblesInvincibleImage);

        opponentMarble = isHost()
                ? new Marble(true, false, level, STANDARD_WIDTH, width * 2 / 3, height / 10, game.sensorHelper, opponentMarblesImage, opponentMarblesInvincibleImage)
                : new Marble(true, false, level, STANDARD_WIDTH, width / 3, height / 10, game.sensorHelper, opponentMarblesImage, opponentMarblesInvincibleImage);


        playerMarble.setLives(game.user.getMultiLives());
        opponentMarble.setLives(game.user.getMultiLives());

        marbles.add(playerMarble);
        marbles.add(opponentMarble);
    }

    @Override
    Bonus newBonus(float position, int offset) {
        Bonus bonus;

        if (opponentCaughtBonusIds.contains(bonusId)) {
            randomBonus.nextInt();  // FIXME qu'est-ce que c'est que ce random non utilisé ?
            bonus = new EmptyBonus(position, offset, newLifeImage);
            opponentCaughtBonusIds.remove((Integer)bonusId);
        } else
            bonus = super.newBonus(position, offset);

        bonus.setBonusId(bonusId++);

        return bonus;
    }

    @Override
    void sendInHole(Obstacle obstacle, Marble marble) {
        if (obstacle instanceof Hole && obstacle.collides(marble))
            write("[" + OPPONENT_MARBLE_INHOLE + "]#");
    }

    @Override
    void renderLoseWin() {
        renderLoseWin(won ? youWinImage : youLoseImage);
    }

    @Override
    void update(float dt) {
        super.update(dt);

        opponentMarblePositionUpdateTime += dt;

        if (opponentMarble.isDead() && playerMarble.isDead())
            gameOver = true;
        else
            won = opponentMarble.isDead();
    }

    @Override
    void updateCamera(float dt) {
        if (playerMarble.isDead() && !opponentMarble.isDead()) {
            checkCamReposition(opponentMarble, dt);
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (!gameOver)
            super.updateCamera(dt);
    }

    @Override
    public void updateMarbles(float dt) {
        super.updateMarbles(dt);

        if (!playerMarble.isDead()) {
            // TODO remplacer Gdx.input.getGyroscopeY() par playerMarble.getPosition().x/width pour savoir comment envoyer
            String marbleUpdate = "[" + POSITION_OPPONENT_MARBLE + ":" + Gdx.input.getGyroscopeY() + ":" + playerMarble.getSlowDown() + ":" + playerMarble.isBlockedOnLeft() + ":" + playerMarble.isBlockedOnRight() + ":" + playerMarble.isBlockedOnTop() + ":" + playerMarble.getCenterPosition().z + ":" + playerMarble.getSpeed() + ":" + playerMarble.getScore() + ":" + playerMarble.isInvincible() + ":" + playerMarble.getCenterPosition().x + ":" + playerMarble.getCenterPosition().y + "]#";
            write(marbleUpdate);
        }
    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {
        if (opponentCaughtBonusIds.contains(bonus.getBonusId())) {
            int index = bonuses.indexOf(bonus);
            bonuses.set(index, new EmptyBonus(bonus.getPosition().y, bonus.getOffset(), newLifeImage));
            opponentCaughtBonusIds.remove((Integer)bonus.getBonusId());
        }
    }

    @Override
    void updateOpponentScore() {
        opponentLivesLabel.setText(opponentMarble.getLives().toString());
        opponentScoreLabel.setText(opponentMarble.getScore().toString());
    }

    private void addOpponentCaughtBonusIds(String[] message) {
        try {
            opponentCaughtBonusIds.add(getIntegerFromStr(message[1]));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void addPositionToOpponentMarble(String[] message) {
        try {
            opponentMarble.addPosition(getFloatFromStr(message[1]), getFloatFromStr(message[6]));
            opponentMarble.setBlockedObstacle(getBooleanFromStr(message[3]), getBooleanFromStr(message[4]), getBooleanFromStr(message[5]));
            opponentMarble.setBonusStatus(getBooleanFromStr(message[9]), getFloatFromStr(message[2]), getFloatFromStr(message[7]));
            opponentMarble.setScore((int) getFloatFromStr(message[8]));

            if (opponentMarblePositionUpdateTime >= 1f) {
                opponentMarble.getCenterPosition().x = getFloatFromStr(message[10]);
                opponentMarble.getCenterPosition().y = getFloatFromStr(message[11]);
                opponentMarblePositionUpdateTime %= 1f;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void checkCamReposition(Marble marble, float dt) {
        if (positionRecover) {
            super.checkCamReposition(marble);
            camera.position.add(0, marble.getSpeedFactor() * dt, 0);
        } else if (marble.getCenterPosition().y > camera.position.y) {
            camera.position.add(0, 4 * marble.getSpeedFactor() * dt, 0);
            if (marble.getCenterPosition().y <= camera.position.y)
                positionRecover = true;
        }
    }

    private boolean getBooleanFromStr(String str) {
        return Boolean.parseBoolean(str);
    }

    private float getFloatFromStr(String str) {
        try {
            return Float.parseFloat(str);
        } catch (NumberFormatException ex) {
            Gdx.app.error("ERROR", "parse float error");
            return 0f;
        }
    }

    private void initRandoms(String[] message) {
        try {
            seed = getLongFromStr(message[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        gotSeed = true;
        randomBonus = new Random(seed);
        randomObstacle = new Random(seed);
    }

    private void repositionOpponentMarble() {
        if (playerMarble.isDead())
            opponentMarble.setRepositioning(.5f);
    }

    private void setOpponentMarbleLives(String[] message) {
        try {
            opponentMarble.setLives(getIntegerFromStr(message[1]));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

}