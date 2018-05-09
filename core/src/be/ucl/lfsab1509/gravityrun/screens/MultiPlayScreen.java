package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.CamReposition;
import be.ucl.lfsab1509.gravityrun.sprites.EmptyBonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;

//FIXME revenir en arriere quand perdu (sauvegarder obstacle / bonus)
public class MultiPlayScreen extends AbstractPlayScreen {

    private static final long COUNTDOWN = 3000L;
    private Marble opponentMarble;

    private ArrayList<Integer> caughtBonusIds;

    private boolean diedReceved = false, opponentReady = false, initDone = false, gotSeed = false, positionRecover = false;
    private boolean startMultiPlayState = false, opponentDied = false;
    private int bonusId, count = 0;
    private Label opponentScoreLabel;
    private long hostTimeStamp1, clientTimestamp, clientStartTime = Long.MAX_VALUE, hostStartTime = Long.MAX_VALUE;

    private long seed;

    public MultiPlayScreen(GravityRun gravityRun, boolean startMultiPlayState) {
        super(gravityRun);
        this.startMultiPlayState = startMultiPlayState;
    }

    MultiPlayScreen(GravityRun gravityRun) {
        super(gravityRun, true);

        caughtBonusIds = new ArrayList<>();


        opponentScoreLabel = new Label(game.i18n.format("opponent_score", opponentMarble.getScore()), game.aaronScoreSkin, "score");
        opponentScoreLabel.setPosition((GravityRun.WIDTH - opponentScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * opponentScoreLabel.getHeight());

        scoreStage.addActor(opponentScoreLabel);

        if (isHost()) {
            seed = 123456789;
            randomObstacle = new Random(seed);
            randomBonus = new Random(seed);
        }

        startMultiPlayState = true;
    }

    @Override
    void bonusCollides(Bonus bonus, int i, Marble marble) {
        write("[2:" + bonus.getBonusId() + "]");
        if (bonus instanceof CamReposition)
            write("[1]#");

        super.bonusCollides(bonus, i, marble);
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

        if (initDone)
            return;

        if (!opponentReady) {
            write("[0]#");
            return;
        }

        if (!gotSeed && isHost() && !initialized) {
            hostTimeStamp1 = System.currentTimeMillis();
            write("[7]#");
            write("[3:" + seed + "]#");
            initialized = true;
            gotSeed = true;
        }

        if (gotSeed) {
            initObstacles();
            initBonuses();
            gotSeed = false;
        }

        if (isHost() && hostStartTime <= System.currentTimeMillis())
            initDone = true;
        else if (!isHost() && (clientStartTime <= System.currentTimeMillis()))
            initDone = true;
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
        opponentMarble.setMarbleLife(game.user.getMultiLives());

        marbles.add(playerMarble);
        marbles.add(opponentMarble);
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
    public void renderMarbles() {
        if (!initDone)
            return;

        super.renderMarbles();
    }

    @Override
    void update(float dt) {
        super.update(dt);

        if (gameOver)
            return;

        opponentScoreLabel.setText(game.i18n.format("opponent_score", opponentMarble.getScore()));

        if (!playerMarble.isDead())
            return;

        if (!diedReceved)
            write("[8]#");

        if (opponentDied)
            gameOver = true;
    }

    @Override
    void updateCamera(float dt) {
        if (playerMarble.isDead() && !opponentDied) {
            checkCamReposition(opponentMarble, dt);
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else if (!gameOver) {
            super.updateCamera(dt);
        }
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
            case 4:
                try {
                    game.user.setMultiLives(getIntegerFromStr(message[1]));
                    game.user.setMulti_IndexSelected(getIntegerFromStr(message[2]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

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
            case 7:
                System.out.println("Client TIME 1 : " + System.currentTimeMillis());
                write("[11:" + System.currentTimeMillis() + "]#");
                break;
            case 8:
                opponentDied = true;
                opponentMarble.setDead(true);
                write("[9]");
                break;
            case 9:
                diedReceved = true;
                break;
            case 10:
                endGameReceived = true;
                break;
            case 11:
                long hostTimestamp2 = System.currentTimeMillis();
                System.out.println("hostTimestamp2 = " + hostTimestamp2);
                try {
                    clientTimestamp = getLongFromStr(message[1]) + (hostTimestamp2 - hostTimeStamp1) / 2;
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                System.out.println("getLongFromStr(message[1]) = " + getLongFromStr(message[1]));
                long rtt = hostTimestamp2 - hostTimeStamp1;
                System.out.println("rtt = " + rtt);
                System.out.println("clientTimestamp = " + clientTimestamp);
                clientStartTime = clientTimestamp + COUNTDOWN;
                System.out.println("clientStartTime = " + clientStartTime);
                hostStartTime = hostTimestamp2 + COUNTDOWN;
                System.out.println("hostStartTime = " + hostStartTime);
                write("[12:" + clientStartTime + "]#");
                break;
            case 12:
                try {
                    clientStartTime = getLongFromStr(message[1]);
                    System.out.println("clientStartTime = " + clientStartTime);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;

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
        System.out.println("startMultiPlayState = " + startMultiPlayState);
        if (!startMultiPlayState)
            return;

        MultiplayerConnectionScreen.ready = false;
        setMultiPlayScreen(new MultiPlayScreen(game, false));

        while (!(screenManager.peek() instanceof  HomeScreen))
            screenManager.pop();

        ((AbstractMenuScreen) screenManager.peek()).spawnErrorDialog(game.i18n.format("error_connection"), game.i18n.format("error_connection_lost"));
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