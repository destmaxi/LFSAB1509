package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
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


public class MenuState extends State {

    private boolean isClickedStartGameButton = false, isClickedOptionButton = false;
    private I18NBundle string;
    private Stage stage;
    private Skin menuSkin, tableSkin;

    public MenuState(GameStateManager gsm) {
        super(gsm);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "BE", "VAR1");
        string = I18NBundle.createBundle(baseFileHandle, locale);

        cam.setToOrtho(false, GravityRun.WIDTH / 2, GravityRun.HEIGHT / 2);

        menuSkin = new Skin();
        menuSkin.createSkin(62);
        Label title = new Label(string.format("menu"), menuSkin, "title");

        tableSkin = new Skin();
        tableSkin.createSkin(42);
        TextButton optionButton = new TextButton(string.format("option"), tableSkin, "round");
        TextButton startGameButton = new TextButton(string.format("new_game"), tableSkin, "round");
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

        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(title).expandX();
        table.row();
        table.add(optionButton).padTop(Gdx.graphics.getHeight() * 150 / GravityRun.HEIGHT);
        table.row();
        table.add(startGameButton).padTop(Gdx.graphics.getHeight() * 30 / GravityRun.HEIGHT);

        stage = new Stage(new ScreenViewport());
        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void handleInput() {
        if (isClickedOptionButton)
            gsm.set(new OptionState(gsm));
        if (isClickedStartGameButton)
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
        if (isClickedOptionButton || isClickedStartGameButton){
            gsm.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
        menuSkin.dispose();
        stage.dispose();
        tableSkin.dispose();
    }

}