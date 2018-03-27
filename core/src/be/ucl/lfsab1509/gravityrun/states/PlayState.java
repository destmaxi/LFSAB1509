package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Hole;
import be.ucl.lfsab1509.gravityrun.sprites.Invincible;
import be.ucl.lfsab1509.gravityrun.sprites.LargeHole;
import be.ucl.lfsab1509.gravityrun.sprites.LeftWall;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Obstacle;
import be.ucl.lfsab1509.gravityrun.sprites.RightWall;
import be.ucl.lfsab1509.gravityrun.sprites.ScoreBonus;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
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
    private static int OBSTACLE_SPACING;
    private static int WIDTH;

    public static boolean isCollideWall = false, gameOver = false;
    public static int collidedWall = 0, score = 0;

    private Array<Bonus> bonuses;
    private Array<Obstacle> obstacles;
    private Array<Vector2> bgs;
    private boolean isClickedPauseButton = false;
    private int scoreBonus = 0;
    private Label scoreLabel;
    private Marble marble;
    private Random random;
    private Skin skin;
    private Stage scoreStage;
    private Texture bg, gameOverImage, pauseImage;

    PlayState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);

        if (GravityRun.scoreList == null)
            GravityRun.scoreList = new ArrayList<Integer>();

        cam.setToOrtho(false, w, h);

        if (w <= 480)
            WIDTH = 480;
        else if (w <= 600)
            WIDTH = 600;
        else if (w <= 840)
            WIDTH = 840;
        else if (w <= 960)
            WIDTH = 960;
        else if (w <= 1280)
            WIDTH = 1280;
        else if (w <= 1440)
            WIDTH = 1440;
        else
            WIDTH = 1600;
        Obstacle.OBSTACLE_HEIGHT = WIDTH / 5;

        bg = new Texture("drawable-" + WIDTH + "/background.png");
        bgs = new Array<Vector2>();
        for (int i = 0; i < 3; i++)
            bgs.add(new Vector2((w - bg.getWidth()) / 2, -h / 2 + i * bg.getHeight()));

        gameOverImage = new Texture("drawable-" + WIDTH + "/gameover.png");
        pauseImage = new Texture("drawable-" + WIDTH + "/pause.png");
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseImage)));
        pauseButton.setPosition(0, h - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedPauseButton = true;
            }
        });

        bonuses = new Array<Bonus>();
        obstacles = new Array<Obstacle>();
        marble = new Marble((int) w / 2, 0, WIDTH);

        skin = new Skin();
        skin.createSkin((int) (0.75f * w / d / 10));

        scoreLabel = new Label(string.format("score"), skin, "score");
        scoreLabel.setText(string.format("score", score));
        scoreLabel.setPosition((w - scoreLabel.getWidth()) / 2, h - scoreLabel.getHeight());

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(scoreLabel);
        scoreStage.addActor(pauseButton);

        OBSTACLE_SPACING = (int) (1.5f * Obstacle.OBSTACLE_HEIGHT);
        OBSTACLE_COUNT = (int) (1.5f * h / (OBSTACLE_SPACING + Obstacle.OBSTACLE_HEIGHT));

        random = new Random();
        for (int i = 1; i <= OBSTACLE_COUNT; i++)
            obstacles.add(newObstacle(i <= Marble.lvl, marble.getDiameter(), (i + 1) * (OBSTACLE_SPACING + Obstacle.OBSTACLE_HEIGHT)));

        for (int i = 1; i <= OBSTACLE_COUNT; i++) {
            int offset = random.nextInt(OBSTACLE_SPACING - pauseImage.getHeight());
            bonuses.add(newBonus((i + 1) * (OBSTACLE_SPACING + Obstacle.OBSTACLE_HEIGHT) + Obstacle.OBSTACLE_HEIGHT + offset, offset));
        }

        cam.position.y = marble.getPosition().y + 80;
    }

    @Override
    protected void handleInput() {
        if (gameOver && (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))) {
            GravityRun.scoreList.add(score);
            soundManager.replayMenu();
            gsm.set(new GameOverState(gsm, soundManager));
        }

        if (!gameOver && (isClickedPauseButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))) {
            isClickedPauseButton = false;
            gsm.push(new PauseState(gsm, soundManager));
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(scoreStage);
        handleInput();
        updateGround();
        marble.update(dt, gameOver);

        score = (int) (marble.getPosition().y / h * 100) + scoreBonus;
        scoreLabel.setText(string.format("score", score));

        if (!gameOver)
            cam.position.add(0, Marble.lvl * Marble.MOVEMENT * marble.speed * SlowDown.slowDown * dt, 0);

        for (int i = 0; i < bonuses.size; i++) {
            Bonus bonus = bonuses.get(i);
            bonus.update(dt);

            int offset = random.nextInt(OBSTACLE_SPACING - pauseImage.getHeight());

            if ((cam.position.y - cam.viewportHeight / 2) >= bonus.getPosition().y + bonus.getObstacleTexture().getHeight()) {
                bonuses.get(i).dispose();
                bonuses.set(i, newBonus(bonus.getPosition().y - bonus.getOffset() + offset + (OBSTACLE_SPACING + Obstacle.OBSTACLE_HEIGHT) * OBSTACLE_COUNT, offset));
            }

            if (bonuses.get(i).collides(marble)) {
                bonuses.add(new ScoreBonus(bonus.getPosition().y - bonus.getOffset() + offset + (OBSTACLE_SPACING + Obstacle.OBSTACLE_HEIGHT) * OBSTACLE_COUNT, WIDTH, offset));
                bonuses.get(i).dispose();
                bonuses.removeIndex(i);
                scoreBonus += 100;
                soundManager.gotBonus();
            }
        }

        for (int i = 0; i < obstacles.size; i++) {
            Obstacle obs = obstacles.get(i);

            if ((cam.position.y - cam.viewportHeight / 2) >= obs.getPosition().y + obs.getObstacleTexture().getHeight()) {
                obstacles.get(i).dispose();
                obstacles.set(i, newObstacle(false, marble.getDiameter(), obs.getPosition().y + (OBSTACLE_SPACING + Obstacle.OBSTACLE_HEIGHT) * OBSTACLE_COUNT));
            }

            boolean temp = gameOver; //TODO: éviter les sides-effects
            if (isCollideWall) {
                if (obs.equals(obstacles.get(collidedWall)) && !gameOver)
                    obs.collides((marble));
            } else {
                obs.collides(marble);
                collidedWall = i;
            }
            if (gameOver != temp)
                soundManager.marbleBreak();
        }

        if (marble.getPosition().x <= 0 || marble.getPosition().x >= (cam.viewportWidth - marble.getDiameter()) || marble.getPosition().y <= cam.position.y - h / 2) {
            if (!gameOver)
                soundManager.marbleBreak();
            gameOver = true;
        }

        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();

        sb.setProjectionMatrix(cam.combined);

        for (Vector2 v : bgs)
            sb.draw(bg, v.x, v.y);

        for (Obstacle obs : obstacles)
            sb.draw(obs.getObstacleTexture(), obs.getPosition().x, obs.getPosition().y);

        for (Bonus bonus : bonuses)
            sb.draw(bonus.getObstacleTexture(), bonus.getPosition().x, bonus.getPosition().y);

        sb.draw(marble.getMarble(), marble.getPosition().x, marble.getPosition().y);

        if (gameOver) {
            sb.draw(gameOverImage,
                    cam.position.x - gameOverImage.getWidth() / 2,
                    cam.position.y);
        }

        sb.end();

        scoreStage.act();
        scoreStage.draw();
    }

    @Override
    public void dispose() {
        bg.dispose();
        gameOverImage.dispose();
        marble.dispose();
        pauseImage.dispose();
        scoreStage.dispose();
        skin.dispose();

        for (Obstacle obstacle : obstacles)
            obstacle.dispose();
    }

    private Obstacle newObstacle(boolean first, int marbleWidth, float position) {
        Obstacle obstacle;
        switch (random.nextInt(6)) {
            case 0:
                obstacle = new Hole(position, first, marbleWidth, WIDTH);
                break;
            case 1:
                obstacle = new LargeHole(position, WIDTH);
                break;
            case 2: case 3:
                obstacle = new LeftWall(position, first, marbleWidth, WIDTH);
                break;
            case 4: case 5:
                obstacle = new RightWall(position, first, marbleWidth, WIDTH);
                break;
            default:
                obstacle = null;
        }
        return obstacle;
    }

    private Bonus newBonus(float position, int offset) {
        Bonus bonus;
        switch (random.nextInt(6)) {
            case 0:
                bonus = new Invincible(position, WIDTH, offset);
                break;
            case 1: case 2: case 3: case 4:
                bonus = new ScoreBonus(position, WIDTH, offset);
                break;
            case 5:
                bonus = new SlowDown(position, WIDTH, offset);
                break;
            default:
                bonus = null;
        }
        return bonus;
    }

    private void updateGround() {
        for (Vector2 v : bgs)
            if (cam.position.y - h / 2 > v.y + bg.getHeight())
                v.add(0, 3 * bg.getHeight());
    }

}
