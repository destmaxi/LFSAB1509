package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Tube;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;

public class PlayState extends State {

    public static int score = 0;
    // private static final int TUBE_COUNT = 6;
    // private static final int TUBE_SPACING = 400;
    private static int tubeCount;// = 6;
    private static int tubeSpacing;// = 400;

    private Array<Tube> tubes;
    private boolean gameOver = false, isClickedPauseButton = false;
    private float d, h, w;//, tubeCount, tubeSpacing;
    private int sw;
    private String sd;
    private Label timeLabel;
    private Marble marble;
    private Stage scoreStage;
    private Skin skin;
    private Texture gameOverImage, pauseImage;
    // private Texture bg; // Will be usefull when we got a background picture
    // private Vector2 bg1, bg2; // Will be usefull when we got a background picture

    public PlayState(GameStateManager gsm) {
        super(gsm);

        if (GravityRun.scoreList == null)
            GravityRun.scoreList = new ArrayList<Integer>();

        // Will be usefull when we got a background picture
        // bg1 = new Vector2(0, cam.position.y - cam.viewportHeight / 2);
        // bg2 = new Vector2(0, (cam.position.y - cam.viewportHeight / 2) + bg.getHeight() );

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

        if (d >= 3.5f)          // xxxhdpi
            sd = "xxxhdpi";         // 4x
        else if (d >= 2.5f)     // xxhdpi
            sd = "xxhdpi";          // 3x
        else if (d >= 1.75f)    // xhdpi
            sd = "xhdpi";           // 2x
        else if (d >= 1.25f)    // hdpi
            sd = "hdpi";            // 1.5x
        else if (d >= 0.875f)   // mdpi
            sd = "mdpi";            // 1x
        else                    // ldpi
            sd = "ldpi";            // 0.75x

        gameOverImage = new Texture("drawable-" + sw + "/gameover.png");
        pauseImage = new Texture("drawable-" + sd + "/pause.png");
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(pauseImage)));
        // pauseButton.setSize(w / 10, w / 10);
        pauseButton.setPosition(0, GravityRun.HEIGHT - pauseButton.getHeight());
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedPauseButton = true;
            }
        });

        marble = new Marble((int) w / 2,0, sw);
        tubes = new Array<Tube>();

        skin = new Skin();
        skin.createSkin((int) (0.75f * GravityRun.WIDTH / GravityRun.DENSITY / 10));
        timeLabel = new Label(string.format("score"), skin, "optional");
        timeLabel.setText(string.format("score", score));

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(timeLabel);
        scoreStage.addActor(pauseButton);

        int marbleWidth = marble.getWidth();
        Tube tube = new Tube(tubeSpacing + Tube.TUBE_HEIGHT, true, marbleWidth, sw);
        tubeSpacing = (int) (2 * Tube.TUBE_HEIGHT);
        tubeCount = (int) (1.5 * h / (tubeSpacing + Tube.TUBE_HEIGHT));
        System.out.println("tubeSpacing = " + tubeSpacing);
        System.out.println("tubeCount = " + tubeCount);
        tube.dispose();
        for (int i = 1; i <= tubeCount; i++)
            tubes.add(new Tube(i * (tubeSpacing + Tube.TUBE_HEIGHT), i <= 1000 * Marble.LVL, marbleWidth, sw));
    }

    @Override
    protected void handleInput() {
        if (gameOver && (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))) {
            gsm.set(new GameOverState(gsm));
        }

        if (!gameOver && (isClickedPauseButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isClickedPauseButton = false;
            gsm.push(new PauseState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(scoreStage);
        handleInput();
        // updateGround(); // Will be usefull when we got a background picture
        marble.update(dt, gameOver);
        score = (int) (marble.getPosition().y / GravityRun.HEIGHT * 100);
        timeLabel.setText(string.format("score",score));

        cam.position.y = marble.getPosition().y + 3 * marble.getWidth();

        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);

            if ((cam.position.y - cam.viewportHeight / 2) >= tube.getPosRightTube().y + tube.getRightTube().getHeight())
                tube.reposition(tube.getPosRightTube().y + ((Tube.TUBE_HEIGHT + tubeSpacing) * tubeCount));

            if (tube.collides(marble.getBounds())) {
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
        for (Tube tube : tubes) {
            sb.draw(tube.getLeftTube(), tube.getPosLeftTube().x, tube.getPosLeftTube().y);
            sb.draw(tube.getRightTube(), tube.getPosRightTube().x, tube.getPosRightTube().y);
        }
        sb.draw(marble.getMarble(), marble.getPosition().x, marble.getPosition().y);

        if (gameOver) {
            if(GravityRun.scoreList == null)
                GravityRun.scoreList = new ArrayList<Integer>();

            GravityRun.scoreList.add(score);
            sb.draw(gameOverImage,
                    cam.position.x - gameOverImage.getWidth() / 2,
                    cam.position.y - gameOverImage.getHeight() / 2);
            if (Gdx.input.justTouched())
                handleInput();
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
        for (Tube tube : tubes) {
            tube.dispose();
        }
    }

    //will be usefull when we got a background picture
   /* private void updateGround(){
        if (cam.position.y - (cam.viewportHeight / 2) > bg1.y + bg.getHeight())
            bg1.add(0, bg.getHeight() * 2);
        if (cam.position.y - (cam.viewportHeight / 2) > bg2.y + bg.getHeight())
            bg2.add(0, bg.getHeight() * 2);
    }*/

}