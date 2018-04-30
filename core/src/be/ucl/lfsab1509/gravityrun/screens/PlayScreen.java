package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Random;

public class PlayScreen extends Screen {

    private static int OBSTACLE_COUNT;
    private static int OBSTACLE_HEIGHT;
    private static int OBSTACLE_SPACING;
    private static int STANDARD_WIDTH;

    public static boolean isCollideWall = false, gameOver = false;
    private static int collidedWall = 0;
    public static int score = 0, scoreBonus = 0;

    private Array<Bonus> bonuses, catchedBonuses;
    private Array<Obstacle> obstacles;
    private Array<Vector2> backgroundPositions;
    private Label scoreLabel;
    private Marble marble;
    private Random random;
    private Stage scoreStage;
    private OrthographicCamera camera;
    private Texture background, gameOverImage, pauseImage;

    PlayScreen(GravityRun gravityRun) {
        super(gravityRun);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        if (game.scoreList == null)
            game.scoreList = new ArrayList<>();

        calculateStandardWidth();

        marble = new Marble((int) width / 2, 0, STANDARD_WIDTH, user.getIndexSelected() + 1);
        gameOver = false;
        isCollideWall = false;
        scoreBonus = 0;

        Bonus.initMarble(marble);
        Invincible.resetBonus();
        SlowDown.resetBonus();

        bonuses = new Array<>();
        catchedBonuses = new Array<>();
        obstacles = new Array<>();

        background = new Texture("drawable-" + STANDARD_WIDTH + "/background.png");
        gameOverImage = new Texture("drawable-" + STANDARD_WIDTH + "/gameover.png");
        pauseImage = new Texture("drawable-" + STANDARD_WIDTH + "/pause.png");

        backgroundPositions = new Array<>();
        for (int i = 0; i < 3; i++)
            backgroundPositions.add(new Vector2((width - background.getWidth()) / 2, -height / 2 + i * background.getHeight()));

        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseImage)));
        pauseButton.setPosition(0, height - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handlePause();
            }
        });

        scoreLabel = new Label(game.i18n.format("score"), game.aaronScoreSkin, "score");
        scoreLabel.setText(game.i18n.format("score", score));

        // TODO ça prend 100-200 msec

        scoreLabel.setPosition((width - scoreLabel.getWidth()) / 2, height - scoreLabel.getHeight());

        // TODO: mettre des coeurs pour montrer le nombre de vie restante

        scoreStage = new Stage(new ScreenViewport(), game.spriteBatch);
        scoreStage.addActor(scoreLabel);
        scoreStage.addActor(pauseButton);

        OBSTACLE_HEIGHT = STANDARD_WIDTH / 5;
        OBSTACLE_SPACING = (int) (1.5f * OBSTACLE_HEIGHT);
        OBSTACLE_COUNT = (int) (1.5f * height / (OBSTACLE_SPACING + OBSTACLE_HEIGHT));

        random = new Random();
        for (int i = 1; i <= OBSTACLE_COUNT; i++)
            obstacles.add(newObstacle((i + 1) * (OBSTACLE_SPACING + OBSTACLE_HEIGHT)));

        for (int i = 1; i <= OBSTACLE_COUNT; i++) {
            int offset = random.nextInt(OBSTACLE_SPACING - STANDARD_WIDTH / 5);
            bonuses.add(newBonus((i + 1) * (OBSTACLE_SPACING + OBSTACLE_HEIGHT) + OBSTACLE_HEIGHT + offset, offset));
        }

        camera.position.y = marble.getCenterPosition().y;
    }

    @Override
    public void dispose() {
        background.dispose();
        gameOverImage.dispose();
        marble.dispose();
        pauseImage.dispose();
        scoreStage.dispose();

        for (Obstacle obstacle : obstacles)
            obstacle.dispose();
    }

    @Override
    public void render(float dt) {
        update(dt);
        render();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(scoreStage);
        soundManager.replayGame();
    }

    private void bonusReposition(Bonus bonus, int i) {
        int offset = random.nextInt(OBSTACLE_SPACING - STANDARD_WIDTH / 10);
        float position = bonus.getPosition().y - bonus.getOffset() + offset + (OBSTACLE_SPACING + OBSTACLE_HEIGHT) * OBSTACLE_COUNT;

        if (bonus.isOutOfScreen(camera.position.y)) {
            bonus.dispose();
            bonuses.set(i, newBonus(position, offset));
        }

        if (bonus.collidesMarble()) {
            catchedBonuses.add(bonus);
            bonuses.set(i, newBonus(position, offset));
            soundManager.gotBonus();
        }
    }

    private void calculateStandardWidth() {
        if (width <= 480)
            STANDARD_WIDTH = 480;
        else if (width <= 600)
            STANDARD_WIDTH = 600;
        else if (width <= 840)
            STANDARD_WIDTH = 840;
        else if (width <= 960)
            STANDARD_WIDTH = 960;
        else if (width <= 1280)
            STANDARD_WIDTH = 1280;
        else if (width <= 1440)
            STANDARD_WIDTH = 1440;
        else
            STANDARD_WIDTH = 1600;
    }

    private void checkCamReposition() {
        if (camera.position.y <= marble.getCenterPosition().y)
            marble.setRepositioning(1f);
    }

    private void handleEndGame() {
        if (gameOver) {
            game.scoreList.add(score);
            soundManager.replayMenu();
            screenManager.set(new GameOverScreen(game));
        }
    }

    private void handleInput() {
        if (Gdx.input.justTouched() || clickedBack())
            handleEndGame();

        if (clickedBack())
            handlePause();
    }

    private void handlePause() {
        if (!gameOver)
            screenManager.push(new PauseScreen(game));
        // FIXME le SoundManager n'arrête pas la musique.
    }

    private Bonus newBonus(float position, int offset) {
        Bonus bonus;
        switch (random.nextInt(10)) {
            case 1:
                bonus = new NewLife(position,offset, STANDARD_WIDTH);
                break;
            case 2:
                bonus = null;
                break;
            case 3:
                bonus = new CamReposition(position, offset, STANDARD_WIDTH);
                break;
            case 4:
                bonus = new Invincible(position, offset, STANDARD_WIDTH);
                break;
            case 5:
                bonus = new SlowDown(position, offset, STANDARD_WIDTH);
                break;
            default:
                bonus = new ScoreBonus(position, offset, STANDARD_WIDTH);
        }
        return bonus;
    }

    private Obstacle newObstacle(float position) {
        Obstacle obstacle;
        switch (random.nextInt(5)) {
            case 0:
                obstacle = new Hole(position, STANDARD_WIDTH, marble.getNormalDiameter());
                break;
            case 3:
                obstacle = new LargeHole(position, STANDARD_WIDTH, marble.getNormalDiameter());
                break;
            default:
                obstacle = new Wall(position, STANDARD_WIDTH, marble.getNormalDiameter());
        }
        return obstacle;
    }

    private void obstacleReposition(Obstacle obstacle, int i) {
        float position = obstacle.getPosition().y + (OBSTACLE_SPACING + OBSTACLE_HEIGHT) * OBSTACLE_COUNT;

        if (obstacle.isOutOfScreen(camera.position.y)) {
            obstacle.dispose();
            obstacles.set(i, newObstacle(position));
        }

        if (!isCollideWall || collidedWall == i) {
            obstacle.collides(marble, soundManager);
            collidedWall = i;
        }
    }

    private void render() {
        game.spriteBatch.setProjectionMatrix(camera.combined);

        game.spriteBatch.begin();

        for (Vector2 backgroundPosition : backgroundPositions)
            game.spriteBatch.draw(background, backgroundPosition.x, backgroundPosition.y);

        for (Obstacle obstacle : obstacles)
            obstacle.render(game.spriteBatch);

        for (Bonus bonus : bonuses)
            if (bonus != null)
                bonus.render(game.spriteBatch);

        float marbleX = marble.getCenterPosition().x - marble.getDiameter() / 2;
        float marbleY = marble.getCenterPosition().y - marble.getDiameter() / 2;
        game.spriteBatch.draw(marble.getMarble(), marbleX, marbleY);

        if (gameOver)
            game.spriteBatch.draw(gameOverImage,
                    camera.position.x - gameOverImage.getWidth() / 2,
                    camera.position.y);

        game.spriteBatch.end();

        scoreStage.act();
        scoreStage.draw();
    }

    private void update(float dt) {
        marble.update(dt, gameOver);

        updateBonuses();
        updateCamera(dt);
        updateCatchedBonuses(dt);
        updateGround();
        updateObstacles();

        score = (int) (marble.getCenterPosition().y / height * 100) + scoreBonus;
        scoreLabel.setText(game.i18n.format("score", score));

        if (!marble.isInvincible() && marble.isOutOfScreen(camera.position.y)) {
            soundManager.marbleBreak(gameOver);
            gameOver = true;
        }

        handleInput();
    }

    private void updateBonuses() {
        for (int i = 0; i < bonuses.size; i++) {
            Bonus bonus = bonuses.get(i);
            if (bonus != null)
                bonusReposition(bonus, i);
        }
    }

    private void updateCamera(float dt) {
        if (!gameOver) {
            checkCamReposition();
            camera.position.add(0, marble.difficulty * Marble.MOVEMENT * marble.speed * marble.getSlowDown() * marble.getRepositioning() * dt, 0);
        }
        camera.update();
    }

    private void updateCatchedBonuses(float dt) {
        for (int i = 0; i < catchedBonuses.size; i++) {
            Bonus bonus = catchedBonuses.get(i);
            bonus.update(dt);
            if (bonus.isFinished()) {
                bonus.dispose();
                catchedBonuses.removeIndex(i--);
            }
        }
    }

    private void updateGround() {
        for (Vector2 v : backgroundPositions)
            if (camera.position.y - height / 2 > v.y + background.getHeight())
                v.add(0, 3 * background.getHeight());
    }

    private void updateObstacles() {
        for (int i = 0; i < obstacles.size; i++) {
            obstacleReposition(obstacles.get(i), i);

            if (marble.getMarbleLife() == 0)
                gameOver = true;
        }
    }

}
