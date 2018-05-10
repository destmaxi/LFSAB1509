package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.CamReposition;
import be.ucl.lfsab1509.gravityrun.sprites.EmptyBonus;
import be.ucl.lfsab1509.gravityrun.sprites.Hole;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Obstacle;

//FIXME revenir en arriere quand perdu (sauvegarder obstacle / bonus)
public class MultiPlayFirstModeScreen extends AbstractMultiPlayScreen {

    private static final int REPOSITION_OPPONENT_MARBLE = 1;
    private static final int OPPONENT_CAUGHT_BONUS = 2;
    private static final int INIT_RANDOMS = 3;
    private static final int POSITION_OPPONENT_MARBLE = 5;
    private static final int OPPONENT_MARBLE_INHOLE = 14;

    private ArrayList<Integer> opponentCaughtBonusIds;

    private boolean gotSeed = false, positionRecover = false, initialized;
    private int bonusId;
    private float opponentMarblePositionUpdateTime;

    private long seed;

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
        int messageType = getIntegerFromStr(message[0]);

        switch (messageType) {
            case REPOSITION_OPPONENT_MARBLE:
                repositionOpponentMarble();
                break;
            case OPPONENT_CAUGHT_BONUS:
                addOpponentCaughtBonusIds(message);
                break;
            case INIT_RANDOMS:
                initRandoms(message);
                break;
            case POSITION_OPPONENT_MARBLE:
                addPositionToOpponentMarble(message);
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
    public void initGame(float dt) {

        if (initDone)
            return;

        if (!opponentReady) {
            write("[" + INIT_MESSAGE + "]#");
            return;
        }

        super.initGame(dt);

        if (!gotSeed && isHost() && !initialized) {
            write("[" + INIT_RANDOMS + ":" + seed + "]#");
            initialized = true;
            gotSeed = true;
        }

        if (gotSeed) {
            initObstacles();
            initBonuses();
            gotSeed = false;
        }
    }

    @Override
    public void initMarbles() {
        int level = game.user.getMulti_IndexSelected() + 1;

        playerMarble = isHost()
                ? new Marble(true, true, width / 3, height / 10, STANDARD_WIDTH, level)
                : new Marble(true, true, width * 2 / 3, height / 10, STANDARD_WIDTH, level);

        opponentMarble = isHost()
                ? new Marble(false, true, width * 2 / 3, height / 10, STANDARD_WIDTH, level)
                : new Marble(false, true, width / 3, height / 10, STANDARD_WIDTH, level);


        playerMarble.setLives(game.user.getMultiLives());
        opponentMarble.setLives(game.user.getMultiLives());

        marbles.add(playerMarble);
        marbles.add(opponentMarble);
    }

    @Override
    Bonus newBonus(float position, int offset) {
        Bonus bonus;
        int bonusType = randomBonus.nextInt(10);

        if (opponentCaughtBonusIds.contains(bonusId)) {
            randomBonus.nextInt();
            bonus = genereateNewBonus(2, offset, position);
            opponentCaughtBonusIds.remove(opponentCaughtBonusIds.indexOf(bonusId));
        } else {
            bonus = genereateNewBonus(bonusType, offset, position);
        }

        bonus.setBonusId(bonusId++);

        return bonus;
    }

    @Override
    void sendInHole(Obstacle obstacle, Marble marble) {
        if (obstacle instanceof Hole && obstacle.collides(marble)) {
            write("[" + OPPONENT_MARBLE_INHOLE + "]#");
        }

    }

    @Override
    void update(float dt) {
        super.update(dt);

        opponentMarblePositionUpdateTime += dt;

        if (opponentMarble.isDead() && playerMarble.isDead())
            gameOver = true;
    }

    @Override
    void updateCamera(float dt) {
        if (playerMarble.isDead() && !opponentMarble.isDead()) {
            checkCamReposition(opponentMarble, dt);
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (!gameOver) {
            super.updateCamera(dt);
        }
    }

    @Override
    public void updateMarbles(float dt) {
        super.updateMarbles(dt);

        if (!playerMarble.isDead()) {
            String marbleUpdate = "[" + POSITION_OPPONENT_MARBLE + ":" + Gdx.input.getGyroscopeY() + ":" + playerMarble.getSlowDown() + ":" + playerMarble.isBlockedOnLeft() + ":" + playerMarble.isBlockedOnRight() + ":" + playerMarble.isBlockedOnTop() + ":" + playerMarble.getCenterPosition().z + ":" + playerMarble.getSpeed() + ":" + playerMarble.getScore() + ":" + playerMarble.isInvincible() + ":" + playerMarble.getCenterPosition().x + ":" + playerMarble.getCenterPosition().y + "]#";
            write(marbleUpdate);
        }
    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {
        int index;

        if (opponentCaughtBonusIds.contains(bonus.getBonusId())) {
            index = bonuses.indexOf(bonus);
            bonuses.set(index, new EmptyBonus(bonus.getPosition().y, bonus.getOffset(), newLifeImage));
            opponentCaughtBonusIds.remove(opponentCaughtBonusIds.indexOf(bonus.getBonusId()));
        }
    }

    @Override
    void updateOpponentScore() {
        opponentScoreLabel.setText(opponentMarble.getScore().toString());
        opponentLivesLabel.setText(opponentMarble.getLives().toString());
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
            opponentMarble.addPosition(getFloatFromStr(message[1]),
                    getFloatFromStr(message[2]),
                    getBooleanFromStr(message[3]),
                    getBooleanFromStr(message[4]),
                    getBooleanFromStr(message[5]),
                    getFloatFromStr(message[6]),
                    getFloatFromStr(message[7]),
                    getBooleanFromStr(message[9]),
                    getFloatFromStr(message[8]));

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
        if (marble.getCenterPosition().y > camera.position.y && !positionRecover) {
            camera.position.add(0, 4 * marble.getSpeedFactor() * dt, 0);
            if (marble.getCenterPosition().y <= camera.position.y)
                positionRecover = true;
        } else if (positionRecover) {
            super.checkCamReposition(marble);
            camera.position.add(0, marble.getSpeedFactor() * dt, 0);
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
            return 0.0f;
        }
    }

    private void initRandoms(String[] message) {
        try {
            seed = getLongFromStr(message[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        randomObstacle = new Random(seed);
        randomBonus = new Random(seed);
        gotSeed = true;
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