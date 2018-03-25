package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Bonus;
import be.ucl.lfsab1509.gravityrun.sprites.Hole;
import be.ucl.lfsab1509.gravityrun.sprites.LargeHole;
import be.ucl.lfsab1509.gravityrun.sprites.LeftWall;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Obstacle;
import be.ucl.lfsab1509.gravityrun.sprites.RightWall;
import be.ucl.lfsab1509.gravityrun.sprites.ScoreBonus;
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
import java.util.Random;

public class PlayState extends State {

    public static boolean isCollideWall = false;
    public static int collidedWall = 0;
    private static int obstacleCount;
    private static int obstacleSpacing;
    static int score = 0;

    private Array<Obstacle> obstacles;
    private Array<Bonus> bonuses;
    private boolean isClickedPauseButton = false;
    public static boolean gameOver = false;
    private Label scoreLabel;
    private int marbleWidth, sw, SCOREBONUS;
    private Marble marble;
    private Random random;
    private Stage scoreStage;
    private Skin skin;
    private Texture gameOverImage, pauseImage;
    // private Vector2 bg1, bg2;

    PlayState(GameStateManager gsm) {
        super(gsm);

        if (GravityRun.scoreList == null)
            GravityRun.scoreList = new ArrayList<Integer>();

        cam.setToOrtho(false, w, h);

        if (w <= 480)
            sw = 480;
        else if (w <= 600)
            sw = 600;
        else if (w <= 840)
            sw = 840;
        else if (w <= 960)
            sw = 960;
        else if (w <= 1280)
            sw = 1280;
        else if (w <= 1440)
            sw = 1440;
        else
            sw = 1600;
        Obstacle.OBSTACLE_HEIGHT = sw / 5;

        // bg1 = new Vector2(0, cam.position.y - cam.viewportHeight / 2);
        // bg2 = new Vector2(0, (cam.position.y - cam.viewportHeight / 2) + bg.getDiameter());

        gameOverImage = new Texture("drawable-" + sw + "/gameover.png");
        pauseImage = new Texture("drawable-" + sw + "/pause.png");
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseImage)));
        pauseButton.setPosition(0, h - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedPauseButton = true;
            }
        });

        obstacles = new Array<Obstacle>();
        marble = new Marble((int) w / 2,0, sw);

        skin = new Skin();
        skin.createSkin((int) (0.75f * w / d / 10));

        scoreLabel = new Label(string.format("score"), skin, "optional");
        scoreLabel.setText(string.format("score", score));

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(scoreLabel);
        scoreStage.addActor(pauseButton);

        marbleWidth = (int) marble.getDiameter();
        obstacleSpacing = (int) (1.5f * Obstacle.OBSTACLE_HEIGHT);
        obstacleCount = (int) (1.5f * h / (obstacleSpacing + Obstacle.OBSTACLE_HEIGHT));


        random = new Random();
        for (int i = 1; i <= obstacleCount; i++)
            obstacles.add(newObstacle(i <= Marble.LVL, marbleWidth, (i + 1) * (obstacleSpacing + Obstacle.OBSTACLE_HEIGHT)));

        bonuses = new Array<Bonus>();

        for (int i = 1; i <= obstacleCount; i++) {
            int offset = random.nextInt(obstacleSpacing - pauseImage.getHeight());
            bonuses.add(new ScoreBonus((i + 1) * (obstacleSpacing + Obstacle.OBSTACLE_HEIGHT) + Obstacle.OBSTACLE_HEIGHT + offset,sw, offset));
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

        score = (int) (marble.getPosition().y / h * 100) + SCOREBONUS;
        scoreLabel.setText(string.format("score", score));

        if (!gameOver)
            cam.position.add(0,(Marble.MOVEMENT + Marble.speed )*dt,0);

        for (int i = 0; i < bonuses.size; i++) {
            Bonus bonus = bonuses.get(i);
            int offset = random.nextInt(obstacleSpacing - pauseImage.getHeight());

            if ((cam.position.y - cam.viewportHeight / 2) >= bonus.getPosition().y + bonus.getObstacleTexture().getHeight()) {
                //int previous_Offset = bonus.getOffset();
                bonuses.get(i).dispose();
                bonuses.set(i,new ScoreBonus( bonus.getPosition().y - bonus.getOffset() + offset + (obstacleSpacing + Obstacle.OBSTACLE_HEIGHT) * obstacleCount ,sw, offset));
                //random.nextInt(obstacleSpacing)+(obstacles.get(i).getPosition().y + (obstacleSpacing + Obstacle.OBSTACLE_HEIGHT) * obstacleCount) - (obstacleSpacing - marble.getDiameter())
            }

           if (bonuses.get(i).collides(marble)) {
                bonuses.add(new ScoreBonus( bonus.getPosition().y - bonus.getOffset() + offset + (obstacleSpacing + Obstacle.OBSTACLE_HEIGHT) * obstacleCount ,sw, offset));
                bonuses.get(i).dispose();
                bonuses.removeIndex(i);
                SCOREBONUS += 100;
            }
        }

        for (int i = 0; i < obstacles.size; i++){
            Obstacle obs = obstacles.get(i);

            if ((cam.position.y - cam.viewportHeight / 2) >= obs.getPosition().y + obs.getObstacleTexture().getHeight()) {
                obstacles.get(i).dispose();
                obstacles.set(i, newObstacle(false, marbleWidth, obs.getPosition().y + (obstacleSpacing + Obstacle.OBSTACLE_HEIGHT) * obstacleCount));
            }

            if(isCollideWall) {
                if (obs.equals(obstacles.get(collidedWall)) && obs.collides(marble) && !gameOver) {
                    // cam.position.y = marble.getPosition().y + 80;
                    Marble.colliding = true;
                    //marble.update(dt,gameOver);
                    //  gameOver = true;
                }
            }
            else {
                if (obs.collides(marble) && !gameOver) {
                    collidedWall = i;
                    // cam.position.y = marble.getPosition().y + 80;
                    Marble.colliding = true;
                    //marble.update(dt,gameOver);
                    //  gameOver = true;
                }
            }
        }

        if (marble.getPosition().x <= 0 || marble.getPosition().x >= (cam.viewportWidth - marble.getDiameter()) || marble.getPosition().y <= cam.position.y - h/2) {
          //  cam.position.y = marble.getPosition().y + 80;
            Marble.colliding = true;
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

        for(Bonus bonus: bonuses)
            sb.draw(bonus.getObstacleTexture(),bonus.getPosition().x, bonus.getPosition().y);

        sb.draw(marble.getMarble(), marble.getPosition().x, marble.getPosition().y);

        if (gameOver) {
            //cam.position.y = ;
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
        if(cam.position.y - (cam.viewportHeight/2) > bg1.y + bg.getDiameter())
            bg1.add(0, bg.getDiameter()*2);

        if(cam.position.y - (cam.viewportHeight/2) > bg2.y + bg.getDiameter())
            bg2.add(0, bg.getDiameter()*2);

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

    private Obstacle newObstacle(boolean first, int marbleWidth, float position) {
        Obstacle obstacle;
        switch (random.nextInt(4)) {
            case 0:
                obstacle =  new Hole(position, first, marbleWidth, sw);
                break;
            case 1:
                obstacle =  new LargeHole(position, first, marbleWidth, sw);
                break;
            case 2:
                obstacle =  new LeftWall(position, first, marbleWidth, sw);
                break;
            case 3:
                obstacle =  new RightWall(position, first, marbleWidth, sw);
                break;
            default:
                obstacle = null;
        }
        return obstacle;
    }

}
