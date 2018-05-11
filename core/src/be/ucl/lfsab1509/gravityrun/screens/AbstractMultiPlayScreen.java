package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class AbstractMultiPlayScreen extends AbstractPlayScreen {

    static final int ACK_DEAD = 9;
    private static final long COUNTDOWN = 3000L;
    private static final int END_GAME = 10;
    private static final int GAME_INFO = 4;
    private static final int INIT_MSG = 0;
    private static final int INIT_SYNCH_MSG = 7;
    static final int OPPONENT_DEAD = 8;
    static final int OPPONENT_LIVES = 13;
    private static final int START_TIME = 12;
    private static final int SYNC_TIME = 11;

    boolean won = false;
    private boolean ackDiedReceved = false, initDone = false, opponentReady = false, startMultiPlayState;
    Label opponentLivesLabel, opponentScoreLabel;
    private Label countDownLabel;
    private long hostTimeStamp1, startTime = Long.MAX_VALUE;
    private Long countDown = 0L;
    private Stage countDownStage;
    Texture youWinImage;
    private Texture opponentLivesImage;

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

        ImageButton opponentLivesButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(opponentLivesImage)));
        opponentLivesButton.setPosition(GravityRun.WIDTH - 1.95f * INIT_IMAGE_SIZE, GravityRun.HEIGHT - 2 * INIT_IMAGE_SIZE);

        opponentLivesLabel = new Label("0", game.aaronScoreSkin, "opponent_score");
        opponentLivesLabel.setAlignment(Align.center);
        opponentLivesLabel.setPosition(GravityRun.WIDTH - (INIT_IMAGE_SIZE + opponentLivesLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * INIT_IMAGE_SIZE);

        opponentScoreLabel = new Label("0", game.aaronScoreSkin, "opponent_score");
        opponentScoreLabel.setAlignment(Align.center);
        opponentScoreLabel.setPosition((GravityRun.WIDTH - opponentScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - 2 * INIT_IMAGE_SIZE);

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
        write("[" + END_GAME + "]#");
    }

    @Override
    public void initGame(float dt) {
        if (initDone)
            return;

        if (!opponentReady) {
            write("[0]#");
            return;
        }

        if (isHost() && !initialized) {
            hostTimeStamp1 = System.currentTimeMillis();
            write("[" + INIT_SYNCH_MSG + "]#");
            initialized = true;
        }

        if (startTime <= System.currentTimeMillis())
            initDone = true;
    }

    @Override
    void initializeTextures() {
        super.initializeTextures();
        opponentLivesImage = new Texture("drawable-" + calculateStandardWidth(GravityRun.WIDTH) + "/opponentlives.png");
        youWinImage = new Texture("drawable-" + calculateStandardWidth(GravityRun.WIDTH) + "/youwin.png");
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

        if (gameOver || !playerMarble.isDead())
            return;

        if (!ackDiedReceved)
            write("[" + OPPONENT_DEAD + "]#");
    }

    @Override
    void updateMarbles(float dt) {
        super.updateMarbles(dt);

        write("[" + OPPONENT_LIVES + ":" + playerMarble.getLives() + "]#");
    }

    public void applyMessage(String[] message) {
        switch (getIntegerFromStr(message[0])) {
            case ACK_DEAD:
                ackDiedReceved = true;
                break;
            case END_GAME:
                endGameReceived = true;
                break;
            case GAME_INFO:
                saveInUser(message);
                break;
            case INIT_MSG:
                initializeGame();
                break;
            case INIT_SYNCH_MSG:
                write("[" + SYNC_TIME + ":" + System.currentTimeMillis() + "]#");
                break;
            case START_TIME:
                setStartTime(message);
                break;
            case SYNC_TIME:
                syncTime(message);
                break;
        }
    }

    int getIntegerFromStr(String str) {
        int returnInt = -1;
        if (str != null)
            try {
                returnInt = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        return returnInt;
    }

    long getLongFromStr(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException ex) {
            Gdx.app.error("ERROR", "parse Long error");
            return 0L;
        }
    }

    public void incomingMessage(String str) {
        int index, end;
        String[] firstSplit = str.split("#");

        if (isValidMessage(firstSplit))
            for (String string : firstSplit) {
                index = string.indexOf("[");
                end = string.indexOf("]");
                String pos = string.substring(index + 1, end);
                String[] splited = pos.split(":");

                applyMessage(splited);
            }
    }

    private void initializeGame() {
        if (startMultiPlayState && !opponentReady) {
            opponentReady = true;
            write("[" + INIT_MSG + "]#");
        } else
            MultiplayerConnectionScreen.setReady(true);
    }

    private boolean isValidMessage(String[] strings) {
        int index, end;
        for (String string : strings) {
            index = string.indexOf("[");
            end = string.indexOf("]");
            if (!(index != -1 && end > index))
                return false;
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

    private void saveInUser(String[] message) {
        try {
            game.user.setMultiLives(getIntegerFromStr(message[1]));
            game.user.setMulti_IndexSelected(getIntegerFromStr(message[2]));
            game.user.setMultiMode(getIntegerFromStr(message[3]));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void setStartTime(String[] message) {
        try {
            startTime = getLongFromStr(message[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void syncTime(String[] message) {
        long hostTimestamp2 = System.currentTimeMillis();
        long clientTimestamp = 0;
        try {
            clientTimestamp = getLongFromStr(message[1]) + (hostTimestamp2 - hostTimeStamp1) / 2;
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        long clientStartTime = clientTimestamp + COUNTDOWN;
        startTime = hostTimestamp2 + COUNTDOWN;
        write("[" + START_TIME + ":" + clientStartTime + "]#");
    }

}
