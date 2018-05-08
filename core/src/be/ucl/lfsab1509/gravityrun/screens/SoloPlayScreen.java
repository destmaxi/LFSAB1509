package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.*;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.Random;

public class SoloPlayScreen extends AbstractPlayScreen {

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
    public void disposeMarbles() {
        playerMarble.dispose();
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

        initMarbles();
        initObstacles();
        initBonuses();
        initialized = true;
    }

    @Override
    public void initMarbles() {
        int level = game.user.getIndexSelected() + 1;
        playerMarble = new Marble(true,false,width / 2, height / 10, STANDARD_WIDTH, level, this);
    }

    @Override
    public boolean isInitDone() {
        return true;
    }

    @Override
    public void renderMarbles() {
        playerMarble.render(game.spriteBatch);
    }

    @Override
    void update(float dt) {
        super.update(dt);

        if(died)
            gameOver = true;
    }

    @Override
    public void updateMarbles(float dt) {
        playerMarble.update(dt, died);
    }

    @Override
    public void updateOpponentCaughtBonus(Bonus bonus) {

    }

    private void handlePause() {
        if (!died)
            screenManager.push(new PauseScreen(game, score));
        //FIXME le SoundManager n'arrête pas la musique.
    }

}
