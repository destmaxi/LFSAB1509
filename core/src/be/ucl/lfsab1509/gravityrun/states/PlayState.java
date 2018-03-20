package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Hole;
import be.ucl.lfsab1509.gravityrun.sprites.LargeHole;
import be.ucl.lfsab1509.gravityrun.sprites.LeftWall;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Obstacle;
import be.ucl.lfsab1509.gravityrun.sprites.RightWall;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;

public class PlayState extends State {

    static int score = 0;
    private static int obstacleCount = 4;
    private static int obstacleSpacing = 80;

    private Array<Obstacle> obstacles;
    private boolean gameOver = false, isClickedPauseButton = false;
    private Label scoreLabel;
    private float d, h, w;//, tubeCount, tubeSpacing;
    private int sw;
    private String sd;
    private Marble marble;
    private Stage scoreStage;
    private Skin skin;
    private Texture gameOverImage, pauseImage;

    PlayState(GameStateManager gsm) {
        super(gsm);

        if (GravityRun.scoreList == null)
            GravityRun.scoreList = new ArrayList<Integer>();

        d = GravityRun.DENSITY;
        h = GravityRun.HEIGHT;
        w = GravityRun.WIDTH;

        cam.setToOrtho(false, w, h);

        if (w >= 1600)
            sw = 1600;
        else if (w >= 1440)
            sw = 1440;
        else if (w >= 1280)
            sw = 1280;
        else if (w >= 960)
            sw = 960;
        else if (w >= 840)
            sw = 840;
        else if (w >= 600)
            sw = 600;
        else
            sw = 480;

        gameOverImage = new Texture("drawable-" + sw + "/gameover.png");
        pauseImage = new Texture("drawable-" + sw + "/pause.png");
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseImage)));
        // pauseButton.setSize(w / 10, w / 10);
        pauseButton.setPosition(0, GravityRun.HEIGHT - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedPauseButton = true;
            }
        });

        marble = new Marble(100, 0, sw);
        obstacles = new Array<Obstacle>();
        marble = new Marble((int) w / 2,0, sw);

        skin = new Skin();
        skin.createSkin((int) (0.75f * GravityRun.WIDTH / GravityRun.DENSITY / 10));

        scoreLabel = new Label(string.format("score"), skin, "optional");
        scoreLabel.setText(string.format("score", score));

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(scoreLabel);
        scoreStage.addActor(pauseButton);

        int marbleWidth = (int) marble.getWidth();
        for (int i = 1; i <= obstacleCount; ) {
            obstacles.add(new Hole(i++ * (obstacleSpacing + Hole.HOLE_WIDTH), sw));
            obstacles.add(new LargeHole( i++ * (obstacleSpacing + LargeHole.HOLE_WIDTH), sw));
            obstacles.add(new LeftWall(i++ * (obstacleSpacing + LeftWall.HOLE_WIDTH), sw));
            obstacles.add(new RightWall(i++ * (obstacleSpacing + RightWall.HOLE_WIDTH), sw));
        }
        /*Tube tube = new Tube(tubeSpacing + Tube.TUBE_HEIGHT, true, marbleWidth, sw);
        tubeSpacing = (int) (2 * Tube.TUBE_HEIGHT);
        tubeCount = (int) (1.5 * h / (tubeSpacing + Tube.TUBE_HEIGHT));
        System.out.println("tubeSpacing = " + tubeSpacing);
        System.out.println("tubeCount = " + tubeCount);
        tube.dispose();
        for (int i = 1; i <= tubeCount; i++)
            tubes.add(new Tube(i * (tubeSpacing + Tube.TUBE_HEIGHT), i <= 1000 * Marble.LVL, marbleWidth, sw));
            */
    }

    @Override
    protected void handleInput() {
        if (gameOver && (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))) {
            GravityRun.scoreList.add(score);
            gsm.set(new GameOverState(gsm));
        }

        if (!gameOver && (isClickedPauseButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))) {
            isClickedPauseButton = false;
            gsm.push(new PauseState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(scoreStage);
        handleInput();
        marble.update(dt, gameOver);

        score = (int) (marble.getPosition().y / GravityRun.HEIGHT * 100);
        scoreLabel.setText(string.format("score", score));

        cam.position.y = marble.getPosition().y + 3 * marble.getWidth();

        for (int i = 0; i < obstacles.size; i++){
            Obstacle obs = obstacles.get(i);

            if ((cam.position.y - cam.viewportHeight / 2) >= obs.getPosition().y + obs.getObstacleTexture().getHeight())
                obs.reposition(obs.getPosition().y + (Hole.HOLE_WIDTH + obstacleSpacing) * obstacleCount);

            if (obs.collides(marble)) {
                marble.colliding = true;
                gameOver = true;
            }
        }

        if (marble.getPosition().x <= 0 || marble.getPosition().x >= (cam.viewportWidth - marble.getWidth())) {
            marble.colliding = true;
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

        for (Obstacle obs : obstacles)
            sb.draw(obs.getObstacleTexture(), obs.getPosition().x, obs.getPosition().y);

        sb.draw(marble.getMarble(), marble.getPosition().x, marble.getPosition().y);

        if (gameOver) {
            sb.draw(gameOverImage,
                    cam.position.x - gameOverImage.getWidth() / 2,
                    cam.position.y - gameOverImage.getHeight() / 2);
        }

        sb.end();

        scoreStage.act();
        scoreStage.draw();
    }

    @Override
    public void dispose() {
        gameOverImage.dispose();
        marble.dispose();
        pauseImage.dispose();
        scoreStage.dispose();
        skin.dispose();

        for (Obstacle obstacle : obstacles)
            obstacle.dispose();
    }
}
