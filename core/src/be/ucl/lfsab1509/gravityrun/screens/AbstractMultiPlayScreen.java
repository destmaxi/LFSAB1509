package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;

public abstract class AbstractMultiPlayScreen extends AbstractPlayScreen {

    private static final long COUNTDOWN = 3000L;

    private boolean diedReceved = false;
    boolean startMultiPlayState = false, opponentReady = false, initDone = false;

    private long hostTimeStamp1, clientTimestamp, clientStartTime = Long.MAX_VALUE, hostStartTime = Long.MAX_VALUE;

    Label opponentScoreLabel;
    Marble opponentMarble;

    AbstractMultiPlayScreen(GravityRun gravityRun, boolean startMultiPlayState) {
        super(gravityRun);
        this.startMultiPlayState = startMultiPlayState;
    }

    AbstractMultiPlayScreen(GravityRun gravityRun ) {
        super(gravityRun, true);

        opponentScoreLabel = new Label(game.i18n.format("opponent_score", 0), game.aaronScoreSkin, "score");
        opponentScoreLabel.setPosition((GravityRun.WIDTH - opponentScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * opponentScoreLabel.getHeight());

        scoreStage.addActor(opponentScoreLabel);

        startMultiPlayState = true;
    }

    abstract void updateOpponentScore();

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

        if (isHost() && !initialized) {
            hostTimeStamp1 = System.currentTimeMillis();
            write("[7]#");
            initialized = true;
        }

        if (isHost() && hostStartTime <= System.currentTimeMillis())
            initDone = true;
        else if (!isHost() && (clientStartTime <= System.currentTimeMillis()))
            initDone = true;
    }

    @Override
    public void initMarbles() {

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

        super.renderMarbles();
    }

    @Override
    void update(float dt) {
        super.update(dt);

        if (gameOver)
            return;

        updateOpponentScore();

        if (!playerMarble.isDead())
            return;

        if (!diedReceved)
            write("[8]#");

    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {

    }

    public void applyMessage(String[] message) {
        int messagetype = getIntegerFromStr(message[0]);

        switch (messagetype) {
            case 0:
                if (startMultiPlayState && !opponentReady) {
                    System.out.println("opponent ready");
                    opponentReady = true;
                    write("[0]#");
                } else {
                    MultiplayerConnectionScreen.ready = true;
                }
                break;
            case 4:
                try {
                    game.user.setMultiLives(getIntegerFromStr(message[1]));
                    game.user.setMulti_IndexSelected(getIntegerFromStr(message[2]));
                    game.user.setMultiMode(getIntegerFromStr(message[3]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                break;
            case 7:
                System.out.println("Client TIME 1 : " + System.currentTimeMillis());
                write("[11:" + System.currentTimeMillis() + "]#");
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

    int getIntegerFromStr(String str) {
        int returnInt = -1;
        if (str != null) {
            returnInt = Integer.parseInt(str);
        }

        return returnInt;
    }

    long getLongFromStr(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            Gdx.app.error("ERROR", "parse Long error");
            return 1;
        }
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

    public void onDisconnect() {
        System.out.println("startMultiPlayState = " + startMultiPlayState);
        if (!startMultiPlayState)
            return;

        MultiplayerConnectionScreen.ready = false;
        setMultiPlayScreen(new MultiPlayFirstModeScreen(game, false));

        while (!(screenManager.peek() instanceof  HomeScreen))
            screenManager.pop();

        ((AbstractMenuScreen) screenManager.peek()).spawnErrorDialog(game.i18n.format("error_connection"), game.i18n.format("error_connection_lost"));
    }
}
