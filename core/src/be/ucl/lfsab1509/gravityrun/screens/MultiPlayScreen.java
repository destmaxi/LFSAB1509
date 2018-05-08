package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.EmptyBonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;

//FIXME revenir en arriere quand perdu (sauvegarder obstacle / bonus)
//FIXME Synchro entre les gsm
public class MultiPlayScreen extends AbstractPlayScreen {

    private Marble opponentMarble;

    private ArrayList<Boolean> wallPositionsX;
    private ArrayList<Float> holePositionsX, offsets, discaredOffsets;
    private ArrayList<Integer> obstacleTypes, bonusTypes, bonusIds, discaredIds, discaredTypes;

    private boolean diedReceved = false, opponentReady = false, initDone = false, obstaclesInitialized = false, gotSeed = false;
    private boolean startMultiPlayState = false, opponentDied = false;
    private int opponentScore = 0;
    private Label opponentScoreLabel;

    private long seed;

    public MultiPlayScreen(GravityRun gravityRun, boolean startMultiPlayState) {
        super(gravityRun);
        this.startMultiPlayState = startMultiPlayState;
    }

    MultiPlayScreen(GravityRun gravityRun) {
        super(gravityRun, true);

        Timestamp timestamp = new Timestamp(1000);

        obstacleTypes = new ArrayList<>();
        bonusIds = new ArrayList<>();
        holePositionsX = new ArrayList<>();
        wallPositionsX = new ArrayList<>();
        bonusTypes = new ArrayList<>();
        offsets = new ArrayList<>();
        discaredIds = new ArrayList<>();
        discaredOffsets = new ArrayList<>();
        discaredTypes = new ArrayList<>();

        opponentScoreLabel = new Label(game.i18n.format("opponent_score", score), game.aaronScoreSkin, "score");
        opponentScoreLabel.setPosition((GravityRun.WIDTH - opponentScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * opponentScoreLabel.getHeight());

        scoreStage.addActor(opponentScoreLabel);

        if (isHost()) {
            seed = 123456789;
            randomObstacle = new Random(seed);
            randomBonus = new Random(seed);
            gotSeed = true;
        }

        startMultiPlayState = true;
        write("[0]#");
    }

    @Override
    void bonusCollidesMarble(Bonus bonus, int i) {
        write("[2:" + bonus.getBonusId() + "]");
        super.bonusCollidesMarble(bonus, i);
    }

    @Override
    public void disposeMarbles() {
        playerMarble.dispose();
        opponentMarble.dispose();
    }

    @Override
    void handleEndGame() {
        super.handleEndGame();

        if (!gameOver)
            return;

        MultiplayerConnectionScreen.ready = false;
        write("[10]#");
    }

    @Override
    public void initGame(float dt) {

        if (!obstaclesInitialized && opponentReady) {
            if (isHost()) {
                initMarbles();
                write("[3:" + seed + "]#");
                gotSeed = true;
            } else {
                initMarbles();
            }

            obstaclesInitialized = true;
        } else if (!initialized && opponentReady && gotSeed) {

            initObstacles();
            initBonuses();
            write("[1]#");
            initDone = true;
            initialized = true;
        }
    }

    @Override
    public void initMarbles() {
        int level = game.user.getMulti_IndexSelected() + 1;

        playerMarble = isHost()
                ? new Marble(true, true, width / 3, height / 10, STANDARD_WIDTH, level, this)
                : new Marble(true, true, width * 2 / 3, height / 10, STANDARD_WIDTH, level, this);

        opponentMarble = isHost()
                ? new Marble(false, true, width * 2 / 3, height / 10, STANDARD_WIDTH, level, this)
                : new Marble(false, true, width / 3, height / 10, STANDARD_WIDTH, level, this);


        playerMarble.setMarbleLife(game.user.getMultiLives());
        playerMarble.setDifficulty(game.user.getMulti_IndexSelected() + 1);
        opponentMarble.setMarbleLife(game.user.getMultiLives());
        opponentMarble.setDifficulty(game.user.getMulti_IndexSelected() + 1);
    }

    @Override
    public boolean isHost() {
        return bluetoothManager.isHost();
    }

    @Override
    public boolean isInitDone() {
        return initDone;
    }

    @Override
    public void renderMarbles() {
        if (!initDone)
            return;

        playerMarble.render(game.spriteBatch);
        opponentMarble.render(game.spriteBatch);
    }

    @Override
    void update(float dt) {
        super.update(dt);

        opponentScoreLabel.setText(game.i18n.format("opponent_score", opponentScore));

        if (!died)
            return;

        if (!diedReceved)
            write("[8]#");

        if (!opponentDied)
            camera.position.y = opponentMarble.getCenterPosition().y;
        else
            gameOver = true;
    }

    @Override
    void updateCamera(float dt) {
        if (died && !opponentDied) {
            checkCamReposition(opponentMarble);
            camera.position.add(0, opponentMarble.getSpeedFactor() * dt, 0);
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (!gameOver){
            super.updateCamera(dt);
        }
    }

    @Override
    public void updateMarbles(float dt) {
        playerMarble.update(dt, died);

        opponentMarble.update(dt, opponentDied);

        if (!died) {
            String marbleUpdate = "[6" + ":" + Gdx.input.getGyroscopeY() + ":" + playerMarble.getSlowDown() + ":" + playerMarble.isBlockedOnLeft() + ":" + playerMarble.isBlockedOnRight() + ":" + playerMarble.isBlockedOnTop() + ":" + playerMarble.getCenterPosition().z + ":" + playerMarble.getSpeed() + ":" + score + ":" + playerMarble.isInvincible() + "]#";
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

    private void applyMessage(String[] message) {
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
                initDone = true;
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
                write("[5]#");
                break;
            case 4:
                try {
                    game.user.setMultiLives(getIntegerFromStr(message[1]));
                    game.user.setMulti_IndexSelected(getIntegerFromStr(message[2]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                break;
            case 5:
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
                            getBooleanFromStr(message[9]));

                    opponentScore = getIntegerFromStr(message[8]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                opponentDied = true;
                write("[9]");
                break;
            case 9:
                diedReceved = true;
                break;
            case 10:
                endGameReceived = true;
        }
    }

    private long getLongFromStr(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            Gdx.app.error("ERROR", "parse Long error");
            return 1;
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

    private int getIntegerFromStr(String str) {
        int returnInt = -1;
        if (str != null) {
            returnInt = Integer.parseInt(str);
        }

        return returnInt;
    }

    public void incomingMessage(String str) {

        int index, end;
        String[] firstSplit = str.split("#");

        if (isValidMessage(firstSplit)) {
            for (String string : firstSplit) {
                index = string.indexOf("[");
                end = string.indexOf("]");
                String pos = string.substring(index + 1, end);
                String[] splited = pos.split(":");

                applyMessage(splited);
            }
        }
    }

    public void onDisconnect() {
        MultiplayerConnectionScreen.ready = false;
    }

    private boolean isValidMessage(String[] strings) {
        int index, end;
        for (String string : strings) {
            index = string.indexOf("[");
            end = string.indexOf("]");
            if (!(index != -1 && end > index)) {
                return false;
            }
        }
        return true;
    }
}