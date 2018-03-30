package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Hole;
import be.ucl.lfsab1509.gravityrun.sprites.Invincible;
import be.ucl.lfsab1509.gravityrun.sprites.LargeHole;
import be.ucl.lfsab1509.gravityrun.sprites.CamReposition;
import be.ucl.lfsab1509.gravityrun.sprites.Wall;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Obstacle;
import be.ucl.lfsab1509.gravityrun.sprites.ScoreBonus;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class PlayState extends State {

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
    private boolean isClickedPauseButton = false;
    private Label scoreLabel;
    private Marble marble;
    private Random random;
    private Stage scoreStage;
    private Texture background, gameOverImage, pauseImage;

    PlayState(GameStateManager gameStateManager, SoundManager soundManager) {
        super(gameStateManager, soundManager);

        camera.setToOrtho(false, width, height);

        if (GravityRun.scoreList == null)
            GravityRun.scoreList = new ArrayList<Integer>();

        calculateStandardWidth();

        marble = new Marble((int) width / 2, 0, STANDARD_WIDTH);
        gameOver = false;
        isCollideWall = false;
        scoreBonus = 0;

        Invincible.resetBonus();
        SlowDown.resetBonus();
        Bonus.initMarble(marble);

        bonuses = new Array<Bonus>();
        catchedBonuses = new Array<Bonus>();
        obstacles = new Array<Obstacle>();

        background = new Texture("drawable-" + STANDARD_WIDTH + "/background.png");
        gameOverImage = new Texture("drawable-" + STANDARD_WIDTH + "/gameover.png");
        pauseImage = new Texture("drawable-" + STANDARD_WIDTH + "/pause.png");

        backgroundPositions = new Array<Vector2>();
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


        // TODO ça prend 100-200 msec

        scoreLabel = new Label(i18n.format("score"), aaronScoreSkin, "score");
        scoreLabel.setText(i18n.format("score", score));
        scoreLabel.setPosition((width - scoreLabel.getWidth()) / 2, height - scoreLabel.getHeight());

        scoreStage = new Stage(new ScreenViewport());
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
    protected void handleInput() {
        if (Gdx.input.justTouched() || clickedBack())
            handleEndGame();

        if (clickedBack())
            handlePause();
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(scoreStage);

        marble.update(dt, gameOver);

        updateCamera(dt);
        updateGround();
        updateCatchedBonuses(dt);
        updateBonuses();
        updateObstacles();

        score = (int) (marble.getCenterPosition().y / height * 100) + scoreBonus;
        scoreLabel.setText(i18n.format("score", score));

        if (!marble.isInvincible() && marble.isOutOfScreen(camera.position.y)) {
            soundManager.marbleBreak(gameOver);
            gameOver = true;
        }

        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();

        spriteBatch.setProjectionMatrix(camera.combined);

        for (Vector2 backgroundPosition : backgroundPositions)
            spriteBatch.draw(background, backgroundPosition.x, backgroundPosition.y);

        for (Obstacle obstacle : obstacles)
            obstacle.render(spriteBatch);

        for (Bonus bonus : bonuses)
            if (bonus != null)
                bonus.render(spriteBatch);

        float marbleX = marble.getCenterPosition().x - marble.getDiameter() / 2;
        float marbleY = marble.getCenterPosition().y - marble.getDiameter() / 2;
        spriteBatch.draw(marble.getMarble(), marbleX, marbleY);

        if (gameOver) {
            spriteBatch.draw(gameOverImage,
                    camera.position.x - gameOverImage.getWidth() / 2,
                    camera.position.y);
        }

        spriteBatch.end();

        scoreStage.act();
        scoreStage.draw();
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

    private boolean clickedBack() {
        return Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

    private Obstacle newObstacle(float position) {
        Obstacle obstacle;
        switch (random.nextInt(5)) {
            case 0:
                obstacle = new Hole(position, STANDARD_WIDTH);
                break;
            case 3:
                obstacle = new LargeHole(position, STANDARD_WIDTH);
                break;
            default:
                obstacle = new Wall(position, STANDARD_WIDTH, marble.getNormalDiameter());
        }
        return obstacle;
    }

    private void handleEndGame() {
        if (gameOver) {
            GravityRun.scoreList.add(score);
            soundManager.replayMenu();
            gameStateManager.set(new GameOverState(gameStateManager, soundManager));
        }
    }

    private void handlePause() {
        if (!gameOver)
            gameStateManager.push(new PauseState(gameStateManager, soundManager));
    }

    private Bonus newBonus(float position, int offset) {
        Bonus bonus;
        switch (random.nextInt(9)) {
            case 1:
            case 7:
                bonus = new ScoreBonus(position, offset, STANDARD_WIDTH);
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
                bonus = null;
        }
        return bonus;
    }

    private void updateBonuses() {
        for (int i = 0; i < bonuses.size; i++) {
            Bonus bonus = bonuses.get(i);
            if (bonus != null)
                bonusReposition(bonus, i);
        }
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

    private void updateCamera(float dt) {
        if (!gameOver) {
            checkCamReposition();
            camera.position.add(0, Marble.difficulty * Marble.MOVEMENT * marble.speed * marble.getSlowDown() * marble.getRepositioning() * dt, 0);
        }
        camera.update();
    }

    private void updateGround() {
        for (Vector2 v : backgroundPositions)
            if (camera.position.y - height / 2 > v.y + background.getHeight())
                v.add(0, 3 * background.getHeight());
    }

    private void updateObstacles() {
        for (int i = 0; i < obstacles.size; i++) {
            Obstacle obstacle = obstacles.get(i);

            if ((camera.position.y - camera.viewportHeight / 2) >= obstacle.getPosition().y + obstacle.getObstacleTexture().getHeight()) {
                obstacles.get(i).dispose();
                obstacles.set(i, newObstacle(obstacle.getPosition().y + (OBSTACLE_SPACING + OBSTACLE_HEIGHT) * OBSTACLE_COUNT));
            }

            boolean temp = gameOver; //TODO: éviter les sides-effects
            if (isCollideWall) {
                if (obstacle.equals(obstacles.get(collidedWall)) && !gameOver)
                    obstacle.collides((marble));
            } else {
                obstacle.collides(marble);
                collidedWall = i;
            }
            if (gameOver != temp)
                soundManager.marbleBreak(gameOver);
        }
    }

}
