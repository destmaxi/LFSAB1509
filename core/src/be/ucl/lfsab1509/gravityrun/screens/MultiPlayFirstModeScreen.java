package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.CamReposition;
import be.ucl.lfsab1509.gravityrun.sprites.EmptyBonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;

//FIXME revenir en arriere quand perdu (sauvegarder obstacle / bonus)
public class MultiPlayScreen extends AbstractMultiPlayScreen {

    private ArrayList<Integer> caughtBonusIds;

    private boolean gotSeed = false, positionRecover = false, initialized;
    private int bonusId;

    private long seed;

    public MultiPlayScreen(GravityRun gravityRun, boolean startMultiPlayScreen) {
        super(gravityRun, startMultiPlayScreen);
    }

    MultiPlayScreen(GravityRun gravityRun) {
        super(gravityRun);

        caughtBonusIds = new ArrayList<>();

        if (isHost()) {
            seed = 123456789;
            randomObstacle = new Random(seed);
            randomBonus = new Random(seed);
        }
    }

    @Override
    public void applyMessage(String[] message) {
        super.applyMessage(message);
        int messageType = getIntegerFromStr(message[0]);

        switch (messageType) {
            case 0:
                if (startMultiPlayState && !opponentReady) {
                    System.out.println("opponent ready");
                    opponentReady = true;
                    write("[0]#");
                } else {
                    MultiplayerConnectionScreen.ready = true;
                }
                break;
            case 1:
                if (playerMarble.isDead())
                    opponentMarble.setRepositioning(.5f);
                break;
            case 2:
                try {
                    caughtBonusIds.add(getIntegerFromStr(message[1]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    seed = getLongFromStr(message[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                System.out.println("seed : " + seed);
                randomObstacle = new Random(seed);
                randomBonus = new Random(seed);
                gotSeed = true;
                break;
            case 6:
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

                    if(System.currentTimeMillis() % 500 == 0) {
                        opponentMarble.getCenterPosition().x = getFloatFromStr(message[10]);
                        opponentMarble.getCenterPosition().y = getFloatFromStr(message[11]);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    void bonusCollides(Bonus bonus, int i, Marble marble) {
        write("[2:" + bonus.getBonusId() + "]");
        if (bonus instanceof CamReposition)
            write("[1]#");

        super.bonusCollides(bonus, i, marble);
    }

    @Override
    public void initGame(float dt) {

        if (initDone)
            return;

        if (!opponentReady) {
            write("[0]#");
            return;
        }

        super.initGame(dt);

        if (!gotSeed && isHost() && !initialized) {
            write("[3:" + seed + "]#");
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


        playerMarble.setMarbleLife(game.user.getMultiLives());
        opponentMarble.setMarbleLife(game.user.getMultiLives());

        marbles.add(playerMarble);
        marbles.add(opponentMarble);
    }

    @Override
    Bonus newBonus(float position, int offset) {
        Bonus bonus;
        int bonusType = randomBonus.nextInt(10);

        if (caughtBonusIds.contains(bonusId)) {
            randomBonus.nextInt();
            bonus = genereateNewBonus(2, offset, position);
            caughtBonusIds.remove(caughtBonusIds.indexOf(bonusId));
        } else {
            bonus = genereateNewBonus(bonusType, offset, position);
        }

        bonus.setBonusId(bonusId++);

        return bonus;
    }

    @Override
    void renderLives(Marble marble) {
        if(playerMarble.isDead())
            super.renderLives(opponentMarble);
        else
            super.renderLives(marble);
    }

    @Override
    void update(float dt) {
        super.update(dt);

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
            String marbleUpdate = "[6" + ":" + Gdx.input.getGyroscopeY() + ":" + playerMarble.getSlowDown() + ":" + playerMarble.isBlockedOnLeft() + ":" + playerMarble.isBlockedOnRight() + ":" + playerMarble.isBlockedOnTop() + ":" + playerMarble.getCenterPosition().z + ":" + playerMarble.getSpeed() + ":" + playerMarble.getScore() + ":" + playerMarble.isInvincible() + ":" + playerMarble.getCenterPosition().x + ":" + playerMarble.getCenterPosition().y + "]#";
            write(marbleUpdate);
        }
    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {
        int index;

        if (caughtBonusIds.contains(bonus.getBonusId())) {
            bonus.dispose();
            index = bonuses.indexOf(bonus);
            bonuses.set(index, new EmptyBonus(bonus.getPosition().y, bonus.getOffset(), newLifeImage));
            caughtBonusIds.remove(caughtBonusIds.indexOf(bonus.getBonusId()));
        }
    }

    @Override
    void updateOpponentScore() {
        opponentScoreLabel.setText(game.i18n.format("opponent_score", opponentMarble.getScore()));
    }

    private void checkCamReposition(Marble marble, float dt) {
        if (marble.getCenterPosition().y > camera.position.y && !positionRecover) {
            camera.position.add(0, 4 * marble.getSpeedFactor() * dt, 0);
            if (marble.getCenterPosition().y <= camera.position.y)
                positionRecover = true;
        } else if (positionRecover){
            super.checkCamReposition(marble);
            camera.position.add(0,  marble.getSpeedFactor() * dt, 0);
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

}