package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Random;

public class SoloPlayScreen extends AbstractPlayScreen {

    private Texture pauseImage;

    SoloPlayScreen(GravityRun gravityRun) {
        super(gravityRun, false);

        randomObstacle = new Random();
        randomBonus = new Random();

        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseImage)));
        pauseButton.setPosition(0, height - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handlePause();
            }
        });

        scoreStage.addActor(pauseButton);
    }

    @Override
    void disposeTextures() {
        super.disposeTextures();
        pauseImage.dispose();
    }

    @Override
    void handleEndGame() {
        if (!gameOver)
            return;

        soundManager.replayMenu();
        screenManager.set(new SoloGameOverScreen(game, this));
    }

    @Override
    void handleInput() {
        super.handleInput();

        if (clickedBack())
            handlePause();
    }

    @Override
    public void initGame(float dt) {
        if (initialized)
            return;

        initObstacles();
        initBonuses();
        initialized = true;
    }

    @Override
    void initializeTextures() {
        super.initializeTextures();
        pauseImage = new Texture("drawable-" + calculateStandardWidth(GravityRun.WIDTH) + "/pause.png");
    }

    @Override
    public void initMarbles() {
        int level = game.user.getIndexSelected() + 1;
        playerMarble = new Marble(false, true, level, STANDARD_WIDTH, width / 2, height / 10, game.sensorHelper, marblesImage, marblesInvincibleImage);
        marbles.add(playerMarble);
    }

    @Override
    public boolean isInitDone() {
        return true;
    }

    @Override
    void renderLoseWin() {
        renderLoseWin(youLoseImage);
    }

    @Override
    public void renderMarbles() {
        playerMarble.render(game.spriteBatch);
    }

    @Override
    void update(float dt) {
        super.update(dt);

        if (playerMarble.isDead())
            gameOver = true;
    }

    private void handlePause() {
        if (!playerMarble.isDead())
            screenManager.push(new PauseScreen(game, playerMarble.getScore()));
        //FIXME le SoundManager n'arrête pas la musique.
    }

}
