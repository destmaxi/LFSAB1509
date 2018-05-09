package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.CamReposition;
import be.ucl.lfsab1509.gravityrun.sprites.EmptyBonus;
import be.ucl.lfsab1509.gravityrun.sprites.Hole;
import be.ucl.lfsab1509.gravityrun.sprites.Invincible;
import be.ucl.lfsab1509.gravityrun.sprites.LargeHole;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.NewLife;
import be.ucl.lfsab1509.gravityrun.sprites.Obstacle;
import be.ucl.lfsab1509.gravityrun.sprites.ScoreBonus;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;
import be.ucl.lfsab1509.gravityrun.sprites.Wall;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

public abstract class AbstractPlayScreen extends Screen {

    public static int OBSTACLE_COUNT;
    public static int OBSTACLE_HEIGHT;
    public static int OBSTACLE_SPACING;
    public static int STANDARD_WIDTH;

    //private static int collidedWall = 0;

    boolean
            initialized = false;

    ArrayList<Bonus> bonuses;
    Array<Obstacle> obstacles;

    ArrayList<Marble> marbles;
    private Array<Vector2> backgroundPositions;
    //  public boolean died = false, isCollideWall = false;
    boolean gameOver = false, endGameReceived = false;
    // private int scoreBonus = 0;
    //int score = 0;
    private Label scoreLabel;
    Marble playerMarble;
    OrthographicCamera camera;
    Random randomObstacle, randomBonus;
    Stage scoreStage;
    private Texture background, camRepositionImage, gameOverImage, holeImage, invincibleImage, largeHoleImage, scoreBonusImage, slowDownImage, wallImage;
    Texture pauseImage, newLifeImage;
    Viewport viewport;


    AbstractPlayScreen(GravityRun gravityRun) {
        super(gravityRun);
    }

    AbstractPlayScreen(GravityRun gravityRun, boolean multiplayer) {
        this(gravityRun);

        if (multiplayer) {
            width = GravityRun.MULTI_WIDTH;
            height = GravityRun.MULTI_HEIGHT;
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        viewport = new FitViewport(width, height, camera);
        viewport.apply(true);

        calculateStandardWidth();

        bonuses = new ArrayList<>();
        // catchedBonuses = new ArrayList<>();
        obstacles = new Array<>();
        marbles = new ArrayList<>();

        initialiseTextures();
        initMarbles();

        backgroundPositions = new Array<>();
        for (int i = 0; i < 3; i++)
            backgroundPositions.add(new Vector2((width - background.getWidth()) / 2, -height / 2 + i * background.getHeight()));

        scoreLabel = new Label(game.i18n.format("score", 0), game.aaronScoreSkin, "score");
        scoreLabel.setPosition((GravityRun.WIDTH - scoreLabel.getWidth()) / 2, GravityRun.HEIGHT - scoreLabel.getHeight());

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(scoreLabel);

        OBSTACLE_HEIGHT = STANDARD_WIDTH / 5;
        OBSTACLE_SPACING = (int) (1.5f * OBSTACLE_HEIGHT);
        OBSTACLE_COUNT = (int) (1.5f * height / (OBSTACLE_SPACING + OBSTACLE_HEIGHT));

        camera.position.y = height / 10;
    }

    public abstract void initGame(float dt);

    public abstract void initMarbles();

    public abstract boolean isInitDone();

    public abstract void updateOpponentCaughtBonus(Bonus bonus);

    @Override
    public void dispose() {
        scoreStage.dispose();

        disposeMarbles();
        disposeTextures();
    }

    @Override
    public void render(float dt) {
        update(dt);
        render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(scoreStage);
        soundManager.replayGame();
    }

/*    public void addScoreBonus(int pointsEarned) {
        this.scoreBonus += pointsEarned;
    }*/

    void bonusCollides(Bonus bonus, int i, Marble marble) {
        marble.addCaughtBonuses(bonus);
        Bonus emptyBonus = new EmptyBonus(bonus.getPosition().y, bonus.getOffset(), newLifeImage);
        emptyBonus.setBonusId(bonus.getBonusId());
        bonuses.set(i, emptyBonus);

        soundManager.gotBonus();
    }

    private void bonusReposition(Bonus bonus, int i, Marble marble) {

        updateOpponentCaughtBonus(bonus);

        if (bonus.isOutOfScreen(camera.position.y, height)) {
            bonus.dispose();

            setBonus(i, bonus);
        }

        if (bonus.collides(marble)) {
            bonusCollides(bonus, i, marble);
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

    void checkCamReposition(Marble marble) {
        if (camera.position.y <= marble.getCenterPosition().y)
            marble.setRepositioning(1f);
    }

    private void disposeMarbles() {
        for (Marble marble : marbles)
            marble.dispose();
    }

    private void disposeTextures() {
        background.dispose();
        camRepositionImage.dispose();
        gameOverImage.dispose();
        holeImage.dispose();
        invincibleImage.dispose();
        largeHoleImage.dispose();
        newLifeImage.dispose();
        pauseImage.dispose();
        scoreBonusImage.dispose();
        slowDownImage.dispose();
        wallImage.dispose();
    }

    Bonus genereateNewBonus(int bonusType, int offset, float positionY) {
        Bonus bonus;
        switch (bonusType) {
            case 1:
                bonus = new NewLife(positionY, offset, this, randomBonus, newLifeImage);
                break;
            case 2:
                bonus = new EmptyBonus(positionY, offset, newLifeImage);
                break;
            case 3:
                bonus = new CamReposition(positionY, offset, this, randomBonus, camRepositionImage);
                break;
            case 4:
                bonus = new Invincible(positionY, offset, this, randomBonus, invincibleImage);
                break;
            case 5:
                bonus = new SlowDown(positionY, offset, this, randomBonus, slowDownImage);
                break;
            default:
                bonus = new ScoreBonus(positionY, offset, this, randomBonus, scoreBonusImage);
        }

        return bonus;
    }

    public Vector3 getCameraPosition() {
        return camera.position;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    void handleEndGame() {
        if (!gameOver)
            return;

        soundManager.replayMenu();
        screenManager.set(new GameOverScreen(game, playerMarble.getScore()));
    }

    void handleInput() {
        if (Gdx.input.justTouched() || clickedBack() || endGameReceived)
            handleEndGame();
    }

    void initBonuses() {
        for (int i = 1; i <= OBSTACLE_COUNT; i++) {
            float off = randomBonus.nextFloat();
            int offset = (int) (off * (OBSTACLE_SPACING - STANDARD_WIDTH / 5));
            bonuses.add(newBonus((i + 1) * (OBSTACLE_SPACING + OBSTACLE_HEIGHT) + OBSTACLE_HEIGHT + offset, offset));
        }
    }

    private void initialiseTextures() {
        String basePath = "drawable-" + STANDARD_WIDTH + "/";
        background = new Texture(basePath + "background.png");
        camRepositionImage = new Texture(basePath + "camreposition.png");
        gameOverImage = new Texture(basePath + "gameover.png");
        holeImage = new Texture(basePath + "hole.png");
        invincibleImage = new Texture(basePath + "invincible.png");
        largeHoleImage = new Texture(basePath + "largehole.png");
        newLifeImage = new Texture(basePath + "newlife.png");
        pauseImage = new Texture(basePath + "pause.png");
        scoreBonusImage = new Texture(basePath + "scorebonus.png");
        slowDownImage = new Texture(basePath + "slowdown.png");
        wallImage = new Texture(basePath + "wall.png");
    }

    void initObstacles() {
        for (int i = 1; i <= OBSTACLE_COUNT; i++)
            obstacles.add(newObstacle((i + 1) * (OBSTACLE_SPACING + OBSTACLE_HEIGHT)));
    }

    Bonus newBonus(float position, int offset) {
        Bonus bonus;
        int bonusType = randomBonus.nextInt(10);
        bonus = genereateNewBonus(bonusType, offset, position);

        return bonus;
    }

    private Obstacle newObstacle(float position) {
        Obstacle obstacle;
        switch (randomObstacle.nextInt(5)) {
            case 0:
                obstacle = new Hole(position, this, playerMarble, randomObstacle, holeImage);
                break;
            case 3:
                obstacle = new LargeHole(position, playerMarble, randomObstacle, this, largeHoleImage);
                break;
            default:
                obstacle = new Wall(position, randomObstacle, playerMarble, this, wallImage);
        }

        return obstacle;
    }

    private void obstacleReposition(Obstacle obstacle, int i, Marble marble) {
        if (obstacle.isOutOfScreen(camera.position.y, height)) {
            obstacle.dispose();

            setObstacle(i);
        }

        if (!marble.isCollideWall() || marble.getCollidedWall() == i) {
            obstacle.collides(marble);
            marble.setCollidedWall(i);
        }
    }

    private void render() {

        if (!isInitDone())
            return;

        game.spriteBatch.setProjectionMatrix(camera.combined);

        game.spriteBatch.begin();

        for (Vector2 backgroundPosition : backgroundPositions)
            game.spriteBatch.draw(background, backgroundPosition.x, backgroundPosition.y);

        for (Obstacle obstacle : obstacles)
            obstacle.render(game.spriteBatch);

        for (Bonus bonus : bonuses)
            bonus.render(game.spriteBatch);

        renderMarbles();

        renderGameOver();
        renderLives();

        game.spriteBatch.end();

        scoreStage.act();
        scoreStage.draw();
    }

    private void renderGameOver() {
        if (gameOver)
            game.spriteBatch.draw(gameOverImage,
                    camera.position.x - gameOverImage.getWidth() / 2,
                    camera.position.y);
    }

    private void renderLives() {
        for (int i = 0, y = newLifeImage.getHeight() + pauseImage.getHeight(); i < playerMarble.getMarbleLife(); i++, y += newLifeImage.getHeight())
            game.spriteBatch.draw(newLifeImage, 0, camera.position.y + height / 2 - y);
    }

    void renderMarbles() {
        for (Marble marble : marbles)
            marble.render(game.spriteBatch);
    }

    private void setBonus(int index, Bonus bonus) {
        int offset = randomBonus.nextInt(OBSTACLE_SPACING - STANDARD_WIDTH / 10);
        float position = bonus.getPosition().y - bonus.getOffset() + offset + (OBSTACLE_SPACING + OBSTACLE_HEIGHT) * OBSTACLE_COUNT;
        bonuses.set(index, newBonus(position, offset));
    }

    private void setObstacle(int index) {
        float position = obstacles.get(index).getPosition().y + (OBSTACLE_SPACING + OBSTACLE_HEIGHT) * OBSTACLE_COUNT;
        obstacles.set(index, newObstacle(position));
    }

    void update(float dt) {
        initGame(dt);

        if (!isInitDone())
            return;

        updateGame(dt);

        scoreLabel.setText(game.i18n.format("score", playerMarble.getScore()));

        if (!playerMarble.isInvincible() && playerMarble.isOutOfScreen(camera.position.y)) {
            soundManager.marbleBreak(playerMarble.isDead());
            playerMarble.setDead(true);
        }

        handleInput();
    }

    private void updateBonuses(Marble marble) {
        for (int i = 0; i < bonuses.size(); i++) {
            Bonus bonus = bonuses.get(i);
            bonusReposition(bonus, i, marble);
        }
    }

    void updateCamera(float dt) {
        if (!playerMarble.isDead()) {
            checkCamReposition(playerMarble);
            camera.position.add(0, playerMarble.getSpeedFactor() * dt, 0);
        }
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void updateCaughtBonus(float dt, Marble marble) {
        for (int i = 0; i < marble.getCaughtBonuses().size(); i++) {
            Bonus bonus = marble.getCaughtBonuses().get(i);
            bonus.update(dt, marble);
            if (bonus.isFinished(marble)) {
                marble.getCaughtBonuses().remove(i--);
            }
        }
    }

    private void updateGame(float dt) {
        updateBonuses(playerMarble);
        updateCaughtBonus(dt, playerMarble);
        updateObstacles(playerMarble);
        updateMarbles(dt);
        updateCamera(dt);
        updateGround();
    }

    private void updateGround() {
        for (Vector2 v : backgroundPositions)
            if (camera.position.y - height / 2 > v.y + background.getHeight())
                v.add(0, 3 * background.getHeight());
    }

    void updateMarbles(float dt) {
        for (Marble marble : marbles)
            marble.update(dt);
    }

    private void updateObstacles(Marble marble) {
        for (int i = 0; i < obstacles.size; i++) {
            obstacleReposition(obstacles.get(i), i, marble);

            if (marble.getMarbleLife() == 0)
                marble.setDead(true);
        }
    }

}