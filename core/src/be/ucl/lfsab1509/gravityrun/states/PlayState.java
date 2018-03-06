package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Locale;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.sprites.Tube;
import be.ucl.lfsab1509.gravityrun.tools.Skin;


/**
 * Created by maxde on 28-02-18.
 */

public class PlayState extends State {
    private static final int TUBE_SPACING = 80;
    private static final int TUBE_COUNT = 4;


    private Marble marble;
    private ScoreboardState dataScore;
    //private Texture bg; //will be usefull when we got a background picture
    //private Vector2 bg1, bg2; //will be usefull when we got a background picture
    private boolean isClickedMenuTextButton,isClickedReplayTextButton,gameOver;
    private static Integer score = null;
    private Label timeLabel,scoreLabel;
    private Stage endStage, scoreStage;
    private float time;
    private Array<Tube> tubes;
    public static ArrayList<Integer> scoreList = null;
    private I18NBundle string;

    public static int lvl;

    public PlayState(GameStateManager gsm) {
        // bg1 = new Vector2(0, cam.position.y - cam.viewportHeight/2); //will be usefull when we got a background picture
        //  bg2 = new Vector2(0, (cam.position.y - cam.viewportHeight/2) + bg.getHeight() );//will be usefull when we got a background picture

        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        Texture gameOverImage = new Texture("gameover.png");
        marble = new Marble(100,0);
        tubes = new Array<Tube>();
        endStage = new Stage(new ScreenViewport());
        scoreStage = new Stage(new ScreenViewport());
        Table endTable = new Table();
        Skin skin = new Skin();

        skin.createSkin(28);
        endTable.top();
        endTable.setFillParent(true);

        dataScore = new ScoreboardState();

        isClickedMenuTextButton = false;
        isClickedReplayTextButton = false;
        gameOver = false;

        if(scoreList == null)
            scoreList = new ArrayList<Integer>();

        score = 0;
        time = 0;

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "CA", "VAR1");
        string = I18NBundle.createBundle(baseFileHandle, locale);


        ImageButton gameOverButton = new ImageButton(new TextureRegionDrawable(new TextureRegion( gameOverImage)));
        TextButton menuTextButton = new TextButton(string.format("menu"), skin,"round" );
        TextButton replayTextButton = new TextButton(string.format("replay"), skin,"round");
        timeLabel = new Label(string.format("score"), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(string.format("final_score"), skin,"title");

        timeLabel.setText(string.format("score", score));


        menuTextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                isClickedMenuTextButton = true;
            }

        });

        replayTextButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                isClickedReplayTextButton = true;
            }

        });

        endTable.add(gameOverButton).expandY();
        endTable.row();
        endTable.add(scoreLabel);
        endTable.row();
        endTable.add(menuTextButton).expandY();
        endTable.row();
        endTable.add(replayTextButton).expandY();

        endStage.addActor(endTable);
        scoreStage.addActor(timeLabel);


        for(int i=1; i<=TUBE_COUNT;i++)
            tubes.add(new Tube(i*(TUBE_SPACING + Tube.TUBE_WIDTH)));

        Gdx.input.setInputProcessor(endStage);

    }

    @Override
    protected void handleInput() {
            if(isClickedMenuTextButton){
                gsm.set(new MenuState(gsm));
            }
            if(isClickedReplayTextButton){
                gsm.set(new PlayState(gsm));
            }
    }

    @Override
    public void update(float dt) {
        handleInput();
        // updateGround(); //will be usefull when we got a background picture
        marble.update(dt);
        score = (Integer)(int) marble.getPosition().y;
        timeLabel.setText(string.format("score",score));


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

        if(marble.getPosition().x <= 0 || marble.getPosition().x >= (cam.viewportWidth - marble.getWidth())){
            marble.colliding = true;
            gameOver = true;
        }

        cam.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.setProjectionMatrix(cam.combined);
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for(Tube tube : tubes){
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }

        sb.draw(marble.getMarble(), marble.getPosition().x, marble.getPosition().y);

        if(gameOver){
            scoreList.add(score);
            dataScore.add();
            scoreLabel.setText(string.format("final_score", score));
            endStage.act();
            endStage.draw();

            if(isClickedMenuTextButton || isClickedReplayTextButton)
                handleInput();
        }

        sb.end();
        scoreStage.act();
        scoreStage.draw();
    }

    @Override
    public void dispose() {
        marble.dispose();
        for(Tube tube : tubes){
            tube.dispose();
        }
    }

    //will be usefull when we got a background picture
   /* private void updateGround(){
        if(cam.position.y - (cam.viewportHeight/2) > bg1.y + bg.getHeight())
            bg1.add(0, bg.getHeight()*2);
        if(cam.position.y - (cam.viewportHeight/2) > bg2.y + bg.getHeight())
            bg2.add(0, bg.getHeight()*2);
    }*/
}