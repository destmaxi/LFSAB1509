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
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
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
    private final List<String> listBox;
    private boolean isClickedLangButton, isCheckedLangButton, isClickedReturnImButton;

    public OptionState (GameStateManager gsm) {

        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "CA", "VAR1");
        final I18NBundle string = I18NBundle.createBundle(baseFileHandle, locale);

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
        isClickedReturnImButton = false;

        Texture returnImage = new Texture("back.png");


        Label title = new Label(string.get("option"),menuSkin, "title");
        TextButton lvlButton = new TextButton(string.format("chose_lvl"),tableSkin,"round");
        TextButton scoreButton = new TextButton(string.format("my_score"),tableSkin,"round");
        ImageButton returnImageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnImage)));
        listBox = new List<String>(tableSkin);

        listBox.setItems(string.format("beginer"), string.format("inter"), string.format("expert"));
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

        scoreButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        returnImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedReturnImButton = true;
            }
        });




        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listBox.getSelected().equals(string.format("beginer")))
                    Marble.LVL=1;
                else if(listBox.getSelected().equals(string.format("inter")))
                    Marble.LVL = 2;
                else if(listBox.getSelected().equals(string.format("expert")))
                    Marble.LVL=3;
            }
        });


        table.add(returnImageButton).expandX().top().width(50).padTop(40);
        table.add(title).expandX().top().left();
        table.add().expandX().top().left().width(100);
        table.row();
        table.add().top().left().width(90);
        table.add(scoreButton).top().padTop(20);
        table.row();
        table.add().top().left().width(90);
        table.add(lvlButton).top().center();
        table.row();
        table.add().top().left().width(90);
        table.add(listBox).top().center();

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);

    }

    @Override
    protected void handleInput() {
        if(isClickedReturnImButton)
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
        stage.dispose();
    }
}
