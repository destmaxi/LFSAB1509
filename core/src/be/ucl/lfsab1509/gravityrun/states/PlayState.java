package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Tube;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Locale;

public class PlayState extends State {

    private static final int TUBE_COUNT = 4;
    private static final int TUBE_SPACING = 80;

    public static int lvl;

    private Array<Tube> tubes;
    private boolean gameOver = false;
    private float time = 0f;
    private I18NBundle string;
    private int score = 0;
    private Label scoreLabel, timeLabel;
    private Marble marble;
    private Stage scoreStage;
    private Skin skin;
    private Texture gameOverImage;
    // private Texture bg; //will be usefull when we got a background picture
    // private Vector2 bg1, bg2; //will be usefull when we got a background picture

    public PlayState(GameStateManager gsm) {
        super(gsm);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "BE", "VAR1");
        string = I18NBundle.createBundle(baseFileHandle, locale);

        // Will be usefull when we got a background picture
        // bg1 = new Vector2(0, cam.position.y - cam.viewportHeight/2);
        // bg2 = new Vector2(0, (cam.position.y - cam.viewportHeight/2) + bg.getHeight() );

        cam.setToOrtho(false, GravityRun.WIDTH / 2, GravityRun.HEIGHT / 2);

        gameOverImage = new Texture("gameover.png");
        marble = new Marble(100,0);
        tubes = new Array<Tube>();

        skin = new Skin();
        skin.createSkin(28);
        scoreLabel = new Label(string.format("final_score"), skin, "error");
        timeLabel = new Label(string.format("score"), skin, "optional");

        timeLabel.setText(string.format("score", score));

        scoreStage = new Stage(new ScreenViewport());
        scoreStage.addActor(timeLabel);

        for (int i = 1; i <= TUBE_COUNT; i++)
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
    }

    @Override
    protected void handleInput() {
            if (gameOver && Gdx.input.justTouched()) {
                GravityRun.lastScore = score;
                gsm.set(new GameOverState(gsm));
            }
    }

    @Override
    public void update(float dt) {
        handleInput();
        // updateGround(); // Will be usefull when we got a background picture
        marble.update(dt);
        time += dt;
        if (time >= 1 && !gameOver){
            score++;
            timeLabel.setText(string.format("score", score));
            time = 0;
        }

        cam.position.y = marble.getPosition().y + 80;

        for(int i=0; i<tubes.size;i++) {
            Tube tube = tubes.get(i);

            if ((cam.position.y - cam.viewportHeight / 2) >= tube.getPosTopTube().y + tube.getTopTube().getHeight())
                tube.reposition(tube.getPosTopTube().y + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));

            if(tube.collides(marble.getBounds())){
                marble.colliding = true;
                gameOver = true;
            }
        }

        if (marble.getPosition().x <= 0 || marble.getPosition().x >= (cam.viewportWidth - marble.getWidth())){
            marble.colliding = true;
            gameOver = true;
        }

        cam.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.setProjectionMatrix(cam.combined);
        for (Tube tube : tubes) {
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
        }
        sb.draw(marble.getMarble(), marble.getPosition().x, marble.getPosition().y);
        if (gameOver) {
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