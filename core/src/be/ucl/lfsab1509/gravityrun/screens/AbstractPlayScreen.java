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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractPlayScreen extends Screen {

    static int INIT_IMAGE_SIZE;
    private static int OBSTACLE_COUNT;
    private static int OBSTACLE_HEIGHT;
    private static int OBSTACLE_SPACING;
    static int STANDARD_WIDTH;

    ArrayList<Bonus> bonuses;
    ArrayList<Marble> marbles;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Vector2> backgroundPositions;
    boolean deadBottom = false, endGameReceived = false, gameOver = false, initialized = false;
    public boolean deadHole = false;
    public int nbInvincible = 0, nbNewLife = 0, nbScoreBonus = 0, nbSlowDown = 0, nbSpeedUp = 0;
    private Label playerLivesLabel, playerScoreLabel;
    Marble playerMarble;
    OrthographicCamera camera;
    Random randomBonus, randomObstacle;
    Stage scoreStage;
    Texture marblesImage, marblesInvincibleImage, slowDownImage, speedUpImage, youLoseImage;
    private Texture background, camRepositionImage, holeImage, invincibleImage, largeHoleImage, newLifeImage, playerLivesImage, scoreBonusImage, wallImage;
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
        marbles = new ArrayList<>();
        obstacles = new ArrayList<>();

        initializeTextures();
        initMarbles();

        backgroundPositions = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            backgroundPositions.add(new Vector2((width - background.getWidth()) / 2, -height / 2 + i * background.getHeight()));

        ImageButton playerLivesButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(playerLivesImage)));
        playerLivesButton.setPosition(GravityRun.WIDTH - 1.95f * INIT_IMAGE_SIZE, GravityRun.HEIGHT - INIT_IMAGE_SIZE);

        playerLivesLabel = new Label(playerMarble.getLives().toString(), game.aaronScoreSkin, "player_score");
        playerLivesLabel.setAlignment(Align.center);
        playerLivesLabel.setPosition(GravityRun.WIDTH - (INIT_IMAGE_SIZE + playerLivesLabel.getWidth()) / 2, GravityRun.HEIGHT - INIT_IMAGE_SIZE);

        playerScoreLabel = new Label(playerMarble.getScore().toString(), game.aaronScoreSkin, "player_score");
        playerScoreLabel.setAlignment(Align.center);
        playerScoreLabel.setPosition((GravityRun.WIDTH - playerScoreLabel.getWidth()) / 2, GravityRun.HEIGHT - INIT_IMAGE_SIZE);

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(playerLivesButton);
        scoreStage.addActor(playerLivesLabel);
        scoreStage.addActor(playerScoreLabel);

        OBSTACLE_HEIGHT = STANDARD_WIDTH / 5;
        OBSTACLE_SPACING = (int) (1.5f * OBSTACLE_HEIGHT);
        OBSTACLE_COUNT = (int) (1.5f * height / (OBSTACLE_SPACING + OBSTACLE_HEIGHT));

        camera.position.y = height / 10;
    }

    abstract void handleEndGame();

    public abstract void initGame(float dt);

    public abstract void initMarbles();

    public abstract boolean isInitDone();

    abstract void renderLoseWin();

    @Override
    public void dispose() {
        scoreStage.dispose();

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
        bonus.stopRender();
        setBonus(i, bonuses, bonus);

        soundManager.gotBonus();
    }

    private void bonusReposition(Bonus bonus, int i, Marble marble) {
        updateOpponentCaughtBonus(bonus);

        if (bonus.isOutOfScreen(camera.position.y, height))
            setBonus(i, bonuses, bonus);

        if (bonus.collides(marble))
            bonusCollides(bonus, i, marble);
    }

    void checkCamReposition(Marble marble) {
        if (camera.position.y <= marble.getCenterPosition().y)
            marble.setRepositioning(1f);
    }

    void disposeTextures() {
        background.dispose();
        camRepositionImage.dispose();
        youLoseImage.dispose();
        holeImage.dispose();
        invincibleImage.dispose();
        largeHoleImage.dispose();
        marblesImage.dispose();
        marblesInvincibleImage.dispose();
        newLifeImage.dispose();
        playerLivesImage.dispose();
        scoreBonusImage.dispose();
        slowDownImage.dispose();
        speedUpImage.dispose();
        wallImage.dispose();
    }

    public Vector3 getCameraPosition() {
        return camera.position;
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    void handleInput() {
        if (Gdx.input.justTouched() || clickedBack() || endGameReceived)
            handleEndGame();
    }

    void initBonuses() {
        for (int i = 1; i <= OBSTACLE_COUNT; i++) {
            float off = randomBonus.nextFloat();
            int offset = (int) (off * (OBSTACLE_SPACING - STANDARD_WIDTH / 5));
            bonuses.add(newBonus((i + playerMarble.getDifficulty()) * (OBSTACLE_SPACING + OBSTACLE_HEIGHT) + OBSTACLE_HEIGHT + offset, offset));
        }
    }

    void initializeTextures() {
        String basePath = "drawable-" + STANDARD_WIDTH + "/";
        background = new Texture(basePath + "background.png");
        camRepositionImage = new Texture(basePath + "camreposition.png");
        holeImage = new Texture(basePath + "hole.png");
        invincibleImage = new Texture(basePath + "invincible.png");
        largeHoleImage = new Texture(basePath + "largehole.png");
        marblesImage = new Texture(basePath + "marbles.png");
        marblesInvincibleImage = new Texture(basePath + "marbles_invincible.png");
        newLifeImage = new Texture(basePath + "newlife.png");
        playerLivesImage = new Texture("drawable-" + calculateStandardWidth(GravityRun.WIDTH) + "/playerlives.png");
        scoreBonusImage = new Texture(basePath + "scorebonus.png");
        slowDownImage = new Texture(basePath + "slowdown.png");
        speedUpImage = new Texture(basePath + "speedup.png");
        wallImage = new Texture(basePath + "wall.png");
        youLoseImage = new Texture(basePath + "youlose.png");
    }

    void initObstacles() {
        for (int i = 1; i <= OBSTACLE_COUNT; i++)
            obstacles.add(newObstacle((i + playerMarble.getDifficulty()) * (OBSTACLE_SPACING + OBSTACLE_HEIGHT)));
    }

    Bonus newBonus(float position, int offset) {
        Bonus bonus;
        switch (randomBonus.nextInt(20)) {
            case 0:
                bonus = new CamReposition(position, offset, this, randomBonus, camRepositionImage);
                break;
            case 2:
            case 3:
                bonus = new Invincible(position, offset, this, randomBonus, invincibleImage);
                break;
            case 4:
            case 5:
                bonus = new NewLife(position, offset, this, randomBonus, newLifeImage);
                break;
            case 6:
            case 7:
                bonus = new SlowDown(position, offset, this, randomBonus, slowDownImage);
                break;
            case 8:
                bonus = new SpeedUp(position, offset, this, randomBonus, speedUpImage);
                break;
            default:
                bonus = new ScoreBonus(position, offset, this, randomBonus, scoreBonusImage);
        }
        return bonus;
    }

    private Obstacle newObstacle(float position) {
        Obstacle obstacle;
        switch (randomObstacle.nextInt(5)) {
            case 1:
                obstacle = new Hole(position, this, playerMarble, randomObstacle, holeImage);
                break;
            case 3:
                obstacle = new LargeHole(position, playerMarble, this, largeHoleImage);
                break;
            default:
                obstacle = new Wall(position, randomObstacle, playerMarble, this, wallImage);
        }
        return obstacle;
    }

    private void obstacleReposition(Obstacle obstacle, int i, Marble marble) {
        if (obstacle.isOutOfScreen(camera.position.y, height))
            setObstacle(i);

        if (!marble.isCollidingWall() || marble.getCollidedWall() == i) {
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

        renderBackgrounds();
        renderSprites();
        renderMarbles();
        renderLoseWin();

        game.spriteBatch.end();

        scoreStage.act();
        scoreStage.draw();
    }

    private void renderBackgrounds() {
        for (Vector2 backgroundPosition : backgroundPositions)
            game.spriteBatch.draw(background, backgroundPosition.x, backgroundPosition.y);
    }

    void renderLoseWin(Texture loseWinImage) {
        if (playerMarble.isDead())
            game.spriteBatch.draw(loseWinImage,
                    camera.position.x - loseWinImage.getWidth() / 2,
                    camera.position.y + height / 4 - loseWinImage.getHeight() / 4);
    }

    void renderMarbles() {
        for (Marble marble : marbles)
            marble.render(game.spriteBatch);
    }

    private void renderSprites() {
        for (Obstacle obstacle : obstacles)
            obstacle.render(game.spriteBatch);

        for (Bonus bonus : bonuses)
            bonus.render(game.spriteBatch);
    }

    void sendInHole(Obstacle obstacle, Marble marble) {

    }

    private void setBonus(int index, ArrayList<Bonus> bonuses, Bonus bonus) {
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
            deadBottom = true;
        }

    }

    private void updateBonuses(Marble marble) {
        for (int i = 0; i < bonuses.size(); i++)
            bonusReposition(bonuses.get(i), i, marble);
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
            if (bonus.isFinished(marble))
                marble.getCaughtBonuses().remove(i--);
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
        for (int i = 0; i < obstacles.size(); i++) {
            obstacleReposition(obstacles.get(i), i, marble);

            if (marble.getLives() == 0)
                marble.setDead();
        }
    }

    public void updateOpponentCaughtBonus(Bonus bonus) {

    }

}