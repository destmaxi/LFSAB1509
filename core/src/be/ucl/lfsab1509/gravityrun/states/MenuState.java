package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Locale;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;


/**
 * Created by maxde on 28-02-18.
 */

public class MenuState extends State {
    private boolean isClickedStartGameButton,isClickedOptionButton;
    private Stage stage;


    public MenuState(GameStateManager gsm) {

        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "CA", "VAR1");
        I18NBundle string = I18NBundle.createBundle(baseFileHandle, locale);

        stage = new Stage(new ScreenViewport());

        Skin menuSkin = new Skin();
        Skin tableSkin = new Skin();

        tableSkin.createSkin(42);
        menuSkin.createSkin(62);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        isClickedStartGameButton = false;
        isClickedOptionButton = false;


        TextButton startGameButton = new TextButton(string.format("new_game"),tableSkin,"round");
        TextButton optionButton = new TextButton(string.format("option"),tableSkin,"round");
        Label title = new Label(string.format("menu"),menuSkin,"title" );


        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                isClickedStartGameButton = true;
            }

        });

        optionButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                isClickedOptionButton = true;
            }
        });

        table.add(title).expandX();
        table.row();
        table.add(optionButton).padTop(150);
        table.row();
        table.add(startGameButton).padTop(30);

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void handleInput() {
        if(isClickedStartGameButton){
            gsm.set(new PlayState(gsm));
        }

        if(isClickedOptionButton)
            gsm.set(new OptionState(gsm));
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
        if(isClickedOptionButton || isClickedStartGameButton){
            gsm.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
    }
}