package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;

public abstract class AbstractMultiPlayScreen extends AbstractPlayScreen {

    private static final long COUNTDOWN = 3000L;

    private boolean diedReceved = false;
    boolean startMultiPlayState = false, opponentReady = false, initDone = false;

    private long hostTimeStamp1, startTime = Long.MAX_VALUE;

    private Long countDown = 0L;

    private Texture opponentLivesImage;
    private Stage countDownStage;
    private Label countDownLabel;
    Label opponentScoreLabel, opponentLivesLabel;
    Marble opponentMarble;

    AbstractMultiPlayScreen(GravityRun gravityRun, boolean startMultiPlayState) {
        super(gravityRun);
        this.startMultiPlayState = startMultiPlayState;
    }

    AbstractMultiPlayScreen(GravityRun gravityRun) {
        super(gravityRun, true);

        countDownLabel = new Label(countDown.toString(), game.titleSkin, "title");
        countDownLabel.setPosition((GravityRun.WIDTH - countDownLabel.getWidth()) / 2, (GravityRun.HEIGHT - countDownLabel.getHeight()) / 2);

        countDownStage = new Stage(new ScreenViewport());
        countDownStage.addActor(countDownLabel);

        opponentScoreLabel = new Label("0", game.aaronScoreSkin, "opponent_score");
        opponentScoreLabel.setAlignment(Align.center);
        opponentScoreLabel.setPosition((GravityRun.WIDTH - opponentScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * INIT_IMAGE_SIZE);

        opponentLivesLabel = new Label("0", game.aaronScoreSkin, "opponent_score");
        opponentLivesLabel.setAlignment(Align.center);
        opponentLivesLabel.setPosition(GravityRun.WIDTH - (INIT_IMAGE_SIZE + opponentLivesLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * INIT_IMAGE_SIZE);

        ImageButton opponentLivesButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(opponentLivesImage)));
        opponentLivesButton.setPosition(GravityRun.WIDTH - 1.95f * INIT_IMAGE_SIZE, GravityRun.HEIGHT - 2 * INIT_IMAGE_SIZE);

        scoreStage.addActor(opponentLivesButton);
        scoreStage.addActor(opponentLivesLabel);
        scoreStage.addActor(opponentScoreLabel);

        startMultiPlayState = true;
    }

    abstract void updateOpponentScore();

    @Override
    void disposeTextures() {
        super.disposeTextures();

        opponentLivesImage.dispose();
    }

    @Override
    void handleEndGame() {
        super.handleEndGame();

        if (!gameOver)
            return;

        MultiplayerConnectionScreen.ready = false;
        startMultiPlayState = false;
        write("[10]#");
    }

    @Override
    public void initGame(float dt) {

        if (isHost() && !initialized) {
            hostTimeStamp1 = System.currentTimeMillis();
            write("[7]#");
            initialized = true;
        }

        if (startTime <= System.currentTimeMillis())
            initDone = true;
    }

    @Override
    public void initMarbles() {

    }

    @Override
    void initialiseTextures() {
        super.initialiseTextures();

        opponentLivesImage = new Texture("drawable-" + calculateStandardWidth(GravityRun.WIDTH) + "/newlife.png");
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
    void render() {
        if (!initDone) {
            countDown = (startTime == Long.MAX_VALUE)
                    ? countDown = 3L
                    : (long) Math.ceil((startTime - System.currentTimeMillis()) / 1000f);

            countDownLabel.setText(countDown.toString());
            countDownStage.act();
            countDownStage.draw();
        }

        super.render();
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

        if (startMultiPlayState)
            updateOpponentScore();

        if (gameOver)
            return;

        if (!playerMarble.isDead())
            return;

        if (!diedReceved)
            write("[8]#");

    }

    @Override
    void updateMarbles(float dt) {
        super.updateMarbles(dt);

        write("[13:" + playerMarble.getLives() + "]#");
    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {

    }

    public void applyMessage(String[] message) {
        int messagetype = getIntegerFromStr(message[0]);

        switch (messagetype) {
            case 0:
                if (startMultiPlayState && !opponentReady) {
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
                long clientTimestamp = 0;
                try {
                    clientTimestamp = getLongFromStr(message[1]) + (hostTimestamp2 - hostTimeStamp1) / 2;
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                long rtt = hostTimestamp2 - hostTimeStamp1;
                long clientStartTime = clientTimestamp + COUNTDOWN;
                startTime = hostTimestamp2 + COUNTDOWN;
                write("[12:" + clientStartTime + "]#");
                break;
            case 12:
                try {
                    startTime = getLongFromStr(message[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    int getIntegerFromStr(String str) {
        int returnInt = -1;
        if (str != null) {
            try {
                returnInt = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
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
        if (!startMultiPlayState)
            return;

        MultiplayerConnectionScreen.ready = false;
        setMultiPlayScreen(new MultiPlayFirstModeScreen(game, false));

        while (!(screenManager.peek() instanceof HomeScreen))
            screenManager.pop();

        ((AbstractMenuScreen) screenManager.peek()).spawnErrorDialog(game.i18n.format("error_connection"), game.i18n.format("error_connection_lost"));
    }
}
