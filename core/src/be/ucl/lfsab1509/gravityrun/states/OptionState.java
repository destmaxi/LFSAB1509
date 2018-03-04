package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.tools.Skin;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Locale;

import be.ucl.lfsab1509.gravityrun.GravityRun;

/**
 * Created by maxime on 04/03/2018.
 */

public class OptionState extends State {

    private Stage stage;
    private final List listBox;
    private boolean isClickedLangButton, isCheckedLangButton, isClickedTextButton1;

    public OptionState (GameStateManager gsm) {

        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "CA", "VAR1");
        I18NBundle string = I18NBundle.createBundle(baseFileHandle, locale);

        stage = new Stage(new ScreenViewport());

        Skin menuSkin = new Skin();
        Skin tableSkin = new Skin();

        tableSkin.createSkin(18);
        menuSkin.createSkin(62);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        isClickedLangButton = false;
        isCheckedLangButton = false;
        isClickedTextButton1 = false;

        Texture returnImage = new Texture("back.png");


        Label title = new Label(string.get("option"),menuSkin, "title");
        TextButton lvlButton = new TextButton(string.format("chose_lvl"),tableSkin, "round");
        ImageButton returnImageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnImage)));
        listBox = new List(tableSkin);

        listBox.setItems("Lvl 1", "Lvl 2","Lvl 3","Lvl 4","Lvl 5");
        listBox.setVisible(false);

        lvlButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!isCheckedLangButton){
                    isClickedLangButton = true;
                    isCheckedLangButton = true;
                }
                else{
                    isClickedLangButton = false;
                    isCheckedLangButton = false;
                }
            }
        });


        returnImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedTextButton1 = true;
            }
        });




        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(listBox.getSelected().equals("Lvl 2"))
                    Marble.LVL = 2;
                else if(listBox.getSelected().equals("Lvl 3"))
                    Marble.LVL=3;
                else if(listBox.getSelected().equals("Lvl 4"))
                    Marble.LVL=4;
                else if(listBox.getSelected().equals("Lvl 5"))
                    Marble.LVL = 5;
            }
        });


        table.add(returnImageButton).expand().top().width(40).padTop(40);
        table.add(title).expand().top().left();
        table.add().expand().top().left().width(100);
        table.row();
        table.add().expand().top().left().width(100);
        table.add(lvlButton).expand().top().padTop(20);
        table.add(listBox).expand().top().left();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    protected void handleInput() {
        if(isClickedTextButton1)
            gsm.set(new MenuState(gsm));
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

        if(isClickedLangButton){
            listBox.setVisible(true);
        }
        else{
            listBox.setVisible(false);
        }

    }

    @Override
    public void dispose() {

    }
}
