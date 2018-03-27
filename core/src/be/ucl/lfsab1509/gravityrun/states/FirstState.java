package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class FirstState extends State {

    private boolean isClickedStartButton = false;
    private Label errorLabel;
    private Stage stage;
    private String username = i18n.format("username");

    public FirstState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);

        float cw = w * 0.9f;
        float ch = h * 0.9f;

        errorLabel = new Label(i18n.format("error"), aaronScoreSkin, "error");
        errorLabel.setWrap(true);
        errorLabel.setWidth(cw);
        errorLabel.setAlignment(Align.center);
        errorLabel.setVisible(false);

        Label title = new Label(i18n.format("welcome"), titleSkin, "title");

        TextButton startButton = new TextButton(i18n.format("start"), tableSkin, "round");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedStartButton = true;
            }
        });
        final TextField usernameField = new TextField(username, tableSkin);
        usernameField.setText(username);
        usernameField.addListener(new ClickListener() {
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

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((w - cw) / 2, (h - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(usernameField).expandX().fillX().padTop(h - ch);
        table.row();
        table.add(startButton).expandX().fillX().padTop(h - ch);
        table.row();
        table.add(errorLabel).expandX().fillX().padTop(h - ch).width(cw);
        table.row();

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedStartButton) {
            Gdx.input.setOnscreenKeyboardVisible(false);
            if (username.equals(i18n.format("username"))) {
                isClickedStartButton = false;
                errorLabel.setVisible(true);
            } else {
                initUser();
                gsm.set(new MenuState(gsm, soundManager));
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
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
    }

    @Override
    public void dispose() {
        // TODO dispose of skins
        aaronScoreSkin.dispose();
        titleSkin.dispose();
        stage.dispose();
        tableSkin.dispose();
    }

    private void initUser() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++)
            arrayList.add(0);

        GravityRun.user.setUsername(username);
        GravityRun.user.setFirstTimeTrue();
        GravityRun.user.setBeginner(new ArrayList<Integer>());
        GravityRun.user.setInter(new ArrayList<Integer>());
        GravityRun.user.setExpert(new ArrayList<Integer>());
        GravityRun.user.setIndexSelected(1);
        GravityRun.user.setHighScore(arrayList);

        GravityRun.pref.put(GravityRun.user.toMap());
        GravityRun.pref.flush();
    }

}
