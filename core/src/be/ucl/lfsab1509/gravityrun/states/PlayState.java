package be.ucl.lfsab1509.gravityrun.states;

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

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Hole;
import be.ucl.lfsab1509.gravityrun.sprites.LargeHole;
import be.ucl.lfsab1509.gravityrun.sprites.LeftWall;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Obstacle;
import be.ucl.lfsab1509.gravityrun.sprites.RightWall;
import be.ucl.lfsab1509.gravityrun.sprites.SlowDown;
import be.ucl.lfsab1509.gravityrun.tools.Skin;

public class PlayState extends State {

    static int score = 0;
    private static final int OBSTACLE_COUNT = 4;
    private static final int OBSTACLE_SPACING = 80;

    private Array<Obstacle> obstacles;
    private boolean gameOver = false, isClickedPauseButton = false;
    private Label scoreLabel;
    private Marble marble;
    private Stage scoreStage;
    private Skin skin;
    private Texture gameOverImage, pauseImage;
    //private Vector2 bg1, bg2;

    PlayState(GameStateManager gsm) {
        super(gsm);

        if (GravityRun.scoreList == null)
            GravityRun.scoreList = new ArrayList<Integer>();

        cam.setToOrtho(false, GravityRun.WIDTH / 2, GravityRun.HEIGHT / 2);

        int h = Gdx.graphics.getHeight(), w = Gdx.graphics.getWidth();

        // bg1 = new Vector2(0, cam.position.y - cam.viewportHeight/2); //will be usefull when we got a background picture
        //  bg2 = new Vector2(0, (cam.position.y - cam.viewportHeight/2) + bg.getHeight() );//will be usefull when we got a backgr

        gameOverImage = new Texture("gameover.png");
        pauseImage = new Texture("pause.png");
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseImage)));
        pauseButton.setSize(w / 10, w / 10);
        pauseButton.setPosition(0, h - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedPauseButton = true;
            }
        });

        marble = new Marble(100, 0);
        obstacles = new Array<Obstacle>();

        skin = new Skin();
        skin.createSkin(28);

        scoreLabel = new Label(string.format("score"), skin, "optional");
        scoreLabel.setText(string.format("score", score));

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(scoreLabel);
        scoreStage.addActor(pauseButton);

        for (int i = 1; i <= OBSTACLE_COUNT; ) {
            obstacles.add(new Hole(i++ * (OBSTACLE_SPACING + Hole.HOLE_WIDTH)));
            obstacles.add(new LargeHole( i++ * (OBSTACLE_SPACING + LargeHole.HOLE_WIDTH)));
            obstacles.add(new LeftWall(i++ * (OBSTACLE_SPACING + LeftWall.HOLE_WIDTH)));
            obstacles.add(new RightWall(i++ * (OBSTACLE_SPACING + RightWall.HOLE_WIDTH)));
        }

        cam.position.y = marble.getPosition().y + 80;
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
        // updateGround(); //will be usefull when we get a background picture
        marble.update(dt, gameOver);

        score = (int) (marble.getPosition().y / 10);
        scoreLabel.setText(string.format("score", score));

        cam.position.add(0,(Marble.MOVEMENT + Marble.speed )*dt,0);

        for (int i = 0; i < obstacles.size; i++){
            Obstacle obs = obstacles.get(i);

            if ((cam.position.y - cam.viewportHeight / 2) >= obs.getPosition().y + obs.getObstacleTexture().getHeight())
                obs.reposition(obs.getPosition().y + (Hole.HOLE_WIDTH + OBSTACLE_SPACING) * OBSTACLE_COUNT);

            if (obs.collides(marble)) {
                cam.position.y = marble.getPosition().y + 80;
                marble.colliding = true;
                gameOver = true;
            }
        }

        if (marble.getPosition().x <= 0 || marble.getPosition().x >= (cam.viewportWidth - marble.getWidth())) {
            cam.position.y = marble.getPosition().y + 80;
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

    //will be usefull when we got a background picture
   /* private void updateGround(){
        if(cam.position.y - (cam.viewportHeight/2) > bg1.y + bg.getHeight())
            bg1.add(0, bg.getHeight()*2);

        if(cam.position.y - (cam.viewportHeight/2) > bg2.y + bg.getHeight())
            bg2.add(0, bg.getHeight()*2);

    }*/


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
