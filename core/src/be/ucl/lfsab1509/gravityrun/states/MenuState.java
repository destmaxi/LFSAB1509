package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import be.ucl.lfsab1509.gravityrun.GravityRun;


/**
 * Created by maxde on 28-02-18.
 */

public class MenuState extends State {
    private boolean isClickedTextButton1;
    private Stage stage;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        int row_height = Gdx.graphics.getHeight() / 12;
        int col_width = Gdx.graphics.getWidth() / 12;
        isClickedTextButton1 = false;

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        TextButton textButton1 = new TextButton("Start Game",skin);
        textButton1.setPosition((Gdx.graphics.getWidth() - textButton1.getWidth())/2,(Gdx.graphics.getHeight()-textButton1.getHeight())/2);
        textButton1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                isClickedTextButton1 = true;
            }

        });
        stage.addActor(textButton1);

        Label title = new Label("Menu",skin,"title" );
        title.setSize(Gdx.graphics.getWidth(), row_height);
        title.setPosition(0, Gdx.graphics.getHeight() - row_height);
        title.setAlignment(Align.center);
        stage.addActor(title);



    }

    @Override
    public void handleInput() {
        if(isClickedTextButton1)
            gsm.set(new PlayState(gsm));
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        stage.act();
        stage.draw();
        if(isClickedTextButton1){
            gsm.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
    }
}