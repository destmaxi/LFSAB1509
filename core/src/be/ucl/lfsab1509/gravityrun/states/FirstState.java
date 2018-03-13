package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;

/**
 * Created by maxde on 13-03-18.
 */

public class FirstState extends State {
    private boolean isClickedStartButton = false;
    private Stage stage;
    private String username= string.format("username");
    private Skin menuSkin, tableSkin;
    private Label errorLabel;

    public FirstState(GameStateManager gsm){
        super(gsm);

        menuSkin = new Skin();
        menuSkin.createSkin(62);
        Label title = new Label(string.format("welcome"), menuSkin, "title");

        tableSkin = new Skin();
        tableSkin.createSkin(42);
        TextButton startButton = new TextButton(string.format("start"), tableSkin, "round");

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedStartButton = true;
            }
        });

        final TextField usernameField = new TextField(username, tableSkin);
        usernameField.setText(username);

        usernameField.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                usernameField.selectAll();
            }
        });
        usernameField.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                username = usernameField.getText();
            }
        });

        errorLabel = new Label(string.format("error"), tableSkin,"error");
        errorLabel.setVisible(false);

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();
        stage = new Stage(new ScreenViewport());

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;


        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2);
        tableContainer.top().fillX();

        table.add(title).top();
        table.row();

        table.add(usernameField).expandX().fillX().padTop(sh - ch);
        table.row();
        table.add(errorLabel).expandX().fillX().padTop(sh - ch);
        table.row();
        table.add(startButton).expandX().fillX().padTop(sh - ch);
        table.row();

        tableContainer.setActor(table);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if(isClickedStartButton){
            GravityRun.pref.putString("username", username);
            GravityRun.pref.putBoolean("firstTime", true);
            GravityRun.pref.flush();
            gsm.set(new MenuState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        if (isClickedStartButton && !username.equals(string.format("username")))
            handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        stage.act();
        stage.draw();
        if (isClickedStartButton && !username.equals(string.format("username"))){
            gsm.update(Gdx.graphics.getDeltaTime());
        }
        else if (isClickedStartButton){
            isClickedStartButton = false;
            errorLabel.setVisible(true);
        }
    }

    @Override
    public void dispose() {

    }
}
