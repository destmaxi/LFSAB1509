package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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

    private static int OBSTACLE_COUNT;
    private static int OBSTACLE_HEIGHT;
    private static int OBSTACLE_SPACING;
    static int STANDARD_WIDTH;
    static int INIT_IMAGE_SIZE;

    private Array<Vector2> backgroundPositions;
    private Array<Obstacle> obstacles;

    ArrayList<Bonus> bonuses;
    ArrayList<Marble> marbles;

    boolean endGameReceived = false, gameOver = false, initialized = false;

    private Label playerScoreLabel, playerLivesLabel;

    Marble playerMarble;
    OrthographicCamera camera;
    Random randomBonus, randomObstacle;
    Stage scoreStage;

    private Texture background, camRepositionImage, holeImage, invincibleImage, largeHoleImage, scoreBonusImage, wallImage, playerLivesImage;
    Texture gameOverImage, newLifeImage, slowDownImage;

    Viewport viewport;


    AbstractPlayScreen(GravityRun gravityRun) {
        super(gravityRun);
    }

    AbstractPlayScreen(GravityRun gravityRun, boolean multiplayer) {
        this(gravityRun);

        INIT_IMAGE_SIZE = GravityRun.WIDTH / 9;

        if (multiplayer) {
            width = GravityRun.MULTI_WIDTH;
            height = GravityRun.MULTI_HEIGHT;
        }

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        viewport = new FitViewport(width, height, camera);
        viewport.apply(true);

        STANDARD_WIDTH = calculateStandardWidth(width);

        bonuses = new ArrayList<>();
        obstacles = new Array<>();
        marbles = new ArrayList<>();

        initialiseTextures();
        initMarbles();

        backgroundPositions = new Array<>();
        for (int i = 0; i < 3; i++)
            backgroundPositions.add(new Vector2((width - background.getWidth()) / 2, -height / 2 + i * background.getHeight()));

        playerScoreLabel = new Label(playerMarble.getScore().toString(), game.aaronScoreSkin, "player_score");
        playerScoreLabel.setAlignment(Align.center);
        playerScoreLabel.setPosition((GravityRun.WIDTH - playerScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - INIT_IMAGE_SIZE);

        playerLivesLabel = new Label(playerMarble.getLives().toString(), game.aaronScoreSkin, "player_score");
        playerLivesLabel.setAlignment(Align.center);
        playerLivesLabel.setPosition(GravityRun.WIDTH - (INIT_IMAGE_SIZE + playerLivesLabel.getWidth()) / 2, GravityRun.HEIGHT - INIT_IMAGE_SIZE);

        ImageButton playerLivesButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(playerLivesImage)));
        playerLivesButton.setPosition(GravityRun.WIDTH - 1.95f * INIT_IMAGE_SIZE, GravityRun.HEIGHT - INIT_IMAGE_SIZE);

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(playerLivesButton);
        scoreStage.addActor(playerScoreLabel);
        scoreStage.addActor(playerLivesLabel);

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
        handleInput();
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

    void bonusCollides(Bonus bonus, int i, Marble marble) {
        marble.addCaughtBonuses(bonus);
        Bonus emptyBonus = new EmptyBonus(bonus.getPosition().y, bonus.getOffset(), newLifeImage);
        emptyBonus.setBonusId(bonus.getBonusId());
        bonuses.set(i, emptyBonus);

        soundManager.gotBonus();
    }

    private void bonusReposition(Bonus bonus, int i, Marble marble) {

        updateOpponentCaughtBonus(bonus);

        if (bonus.isOutOfScreen(camera.position.y, height))
            setBonus(i, bonus);

        if (bonus.collides(marble)) {
            bonusCollides(bonus, i, marble);
        }
    }

    int calculateStandardWidth(int width) {
        int standardWidth;

        if (width <= 480)
            standardWidth = 480;
        else if (width <= 600)
            standardWidth = 600;
        else if (width <= 840)
            standardWidth = 840;
        else if (width <= 960)
            standardWidth = 960;
        else if (width <= 1280)
            standardWidth = 1280;
        else if (width <= 1440)
            standardWidth = 1440;
        else
            standardWidth = 1600;

        return standardWidth;
    }

    void checkCamReposition(Marble marble) {
        if (camera.position.y <= marble.getCenterPosition().y)
            marble.setRepositioning(1f);
    }

    private void disposeMarbles() {
        for (Marble marble : marbles)
            marble.dispose();
    }

    void disposeTextures() {
        background.dispose();
        camRepositionImage.dispose();
        gameOverImage.dispose();
        holeImage.dispose();
        invincibleImage.dispose();
        largeHoleImage.dispose();
        newLifeImage.dispose();
        playerLivesImage.dispose();
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
        screenManager.set(new GameOverScreen(game, playerMarble.getScore(), this instanceof AbstractMultiPlayScreen));
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

    void initialiseTextures() {
        String basePath = "drawable-" + STANDARD_WIDTH + "/";
        background = new Texture(basePath + "background.png");
        camRepositionImage = new Texture(basePath + "camreposition.png");
        gameOverImage = new Texture(basePath + "gameover.png");
        holeImage = new Texture(basePath + "hole.png");
        invincibleImage = new Texture(basePath + "invincible.png");
        largeHoleImage = new Texture(basePath + "largehole.png");
        newLifeImage = new Texture(basePath + "newlife.png");
        playerLivesImage = new Texture("drawable-" + calculateStandardWidth(GravityRun.WIDTH) + "/newlife.png");
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
        if (obstacle.isOutOfScreen(camera.position.y, height))
            setObstacle(i);

        if (!marble.isCollideWall() || marble.getCollidedWall() == i) {
            sendInHole(obstacle, marble);
            obstacle.collides(marble);
            marble.setCollidedWall(i);
        }
    }

    void render() {

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

        game.spriteBatch.end();

        scoreStage.act();
        scoreStage.draw();
    }

    void renderGameOver() {
        if (gameOver)
            game.spriteBatch.draw(gameOverImage,
                    camera.position.x - gameOverImage.getWidth() / 2,
                    camera.position.y);
    }

    void renderMarbles() {
        for (Marble marble : marbles)
            marble.render(game.spriteBatch);
    }

    void sendInHole(Obstacle obstacle, Marble marble) {

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
        if (endGameReceived)
            return;

        initGame(dt);

        if (!isInitDone())
            return;

        updateGame(dt);

        playerScoreLabel.setText(playerMarble.getScore().toString());
        playerLivesLabel.setText(playerMarble.getLives().toString());

        if (!playerMarble.isInvincible() && playerMarble.isOutOfScreen(camera.position.y)) {
            soundManager.marbleBreak(playerMarble.isDead());
            playerMarble.setLives(0);
            playerMarble.setDead();
        }

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
        updateMarbles(dt);

        if (gameOver)
            return;

        updateBonuses(playerMarble);
        updateCaughtBonus(dt, playerMarble);
        updateObstacles(playerMarble);
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

            if (marble.getLives() == 0)
                marble.setDead();
        }
    }

}