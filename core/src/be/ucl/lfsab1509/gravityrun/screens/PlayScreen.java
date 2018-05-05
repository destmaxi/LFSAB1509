package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.*;

import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class PlayScreen extends Screen {

    private static int OBSTACLE_COUNT;
    private static int OBSTACLE_HEIGHT;
    private static int OBSTACLE_SPACING;
    private static int STANDARD_WIDTH;

    public static boolean isCollideWall = false, gameOver = false;
    private static int collidedWall = 0;
    private int score = 0, scoreBonus = 0;

    private Array<Bonus> bonuses, caughtBonuses;
    private Array<Obstacle> obstacles;
    private Array<Vector2> backgroundPositions;
    private Label scoreLabel;
    private Marble marble;
    private Random random;
    private Stage scoreStage;
    private OrthographicCamera camera;
    private Texture background, camRepositionImage, gameOverImage, holeImage, invicibleImage, largeHoleImage, newLifeImage, pauseImage, scoreBonusImage, slowDownImage, wallImage;

    PlayScreen(GravityRun gravityRun) {
        super(gravityRun);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);

        calculateStandardWidth();

        marble = new Marble((int) width / 2, 0, STANDARD_WIDTH, game.user.getIndexSelected() + 1, this);
        gameOver = false;
        isCollideWall = false;

        Invincible.resetBonus();
        SlowDown.resetBonus();

        bonuses = new Array<>();
        caughtBonuses = new Array<>();
        obstacles = new Array<>();

        initialiseTextures();

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

    public void addScoreBonus(int pointsEarned) {
        this.scoreBonus += pointsEarned;
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

    public Vector3 getCameraPosition() {
        return camera.position;
    }

    public int getScore() {
        return score;
    }

    public SoundManager getSoundManager() {
        return soundManager;
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

    private void handleEndGame() {
        if (gameOver) {
            soundManager.replayMenu();
            screenManager.set(new GameOverScreen(game, score));
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
            screenManager.push(new PauseScreen(game, score));
        // FIXME le SoundManager n'arrête pas la musique.
    }

    private void initialiseTextures() {
        String basePath = "drawable-" + STANDARD_WIDTH + "/";
        background = new Texture(basePath + "background.png");
        camRepositionImage = new Texture(basePath + "slowdown.png"); // FIXME
        gameOverImage = new Texture(basePath + "gameover.png");
        holeImage = new Texture(basePath + "hole.png");
        invicibleImage = new Texture(basePath + "invincible.png");
        largeHoleImage = new Texture(basePath + "largehole.png");
        newLifeImage = new Texture(basePath + "slowdown.png"); // FIXME
        pauseImage = new Texture(basePath + "pause.png");
        scoreBonusImage = new Texture(basePath + "scorebonus.png");
        slowDownImage = new Texture(basePath + "slowdown.png");
        wallImage = new Texture(basePath + "wall.png");
    }

    private Bonus newBonus(float positionY, int offset) {
        Bonus bonus;
        switch (random.nextInt(10)) {
            case 1:
                bonus = new NewLife(positionY, offset, newLifeImage);
                break;
            case 2:
                bonus = null;
                break;
            case 3:
                bonus = new CamReposition(positionY, offset, camRepositionImage, this);
                break;
            case 4:
                bonus = new Invincible(positionY, offset, invicibleImage);
                break;
            case 5:
                bonus = new SlowDown(positionY, offset, slowDownImage);
                break;
            default:
                bonus = new ScoreBonus(positionY, offset, scoreBonusImage, this);
        }
        return bonus;
    }

    private Obstacle newObstacle(float position) {
        Obstacle obstacle;
        switch (random.nextInt(5)) {
            case 0:
                obstacle = new Hole(position, marble.getNormalDiameter(), holeImage, this);
                break;
            case 3:
                obstacle = new LargeHole(position, marble.getNormalDiameter(), largeHoleImage, this);
                break;
            default:
                obstacle = new Wall(position, marble.getNormalDiameter(), wallImage, this);
        }
        return obstacle;
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

        marble.render(game.spriteBatch);

        if (gameOver)
            renderGameOver();

        game.spriteBatch.end();

        scoreStage.act();
        scoreStage.draw();
    }

    private void renderGameOver() {
        game.spriteBatch.draw(gameOverImage,
                camera.position.x - gameOverImage.getWidth() / 2,
                camera.position.y);
    }

    private void repositionBonus(Bonus bonus, int i) {
        int offset = random.nextInt(OBSTACLE_SPACING - STANDARD_WIDTH / 10);
        float position = bonus.getPosition().y - bonus.getOffset() + offset + (OBSTACLE_SPACING + OBSTACLE_HEIGHT) * OBSTACLE_COUNT;

        if (bonus.isOutOfScreen(camera.position.y)) {
            bonus.dispose();
            bonuses.set(i, newBonus(position, offset));
        }

        if (bonus.collides(marble)) {
            caughtBonuses.add(bonus);
            bonuses.set(i, newBonus(position, offset));
            soundManager.gotBonus();
        }
    }

    private void repositionObstacle(Obstacle obstacle, int i) {
        float position = obstacle.getPosition().y + (OBSTACLE_SPACING + OBSTACLE_HEIGHT) * OBSTACLE_COUNT;

        if (obstacle.isOutOfScreen(camera.position.y)) {
            obstacle.dispose();
            obstacles.set(i, newObstacle(position));
        }

        if (!isCollideWall || collidedWall == i) {
            obstacle.collides(marble);
            collidedWall = i;
        }
    }

    private void update(float dt) {
        updateBonuses();
        updateCaughtBonuses(dt);
        marble.update(dt, gameOver);
        updateCamera(dt);
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
                repositionBonus(bonus, i);
        }
    }

    private void updateCamera(float dt) {
        if (!gameOver) {
            camera.position.add(0, marble.difficulty * Marble.MOVEMENT * marble.speed * marble.getSlowDown() * marble.getRepositioning() * dt, 0);
        }
        camera.update();
    }

    private void updateCaughtBonuses(float dt) {
        for (int i = 0; i < caughtBonuses.size; i++) {
            Bonus bonus = caughtBonuses.get(i);
            bonus.update(dt);
            if (bonus.isFinished()) {
                bonus.dispose();
                caughtBonuses.removeIndex(i--);
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
            repositionObstacle(obstacles.get(i), i);

            if (marble.getMarbleLife() == 0)
                gameOver = true;
        }
    }

}
