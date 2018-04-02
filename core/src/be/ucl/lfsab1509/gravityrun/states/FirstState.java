package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.User;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class FirstState extends AbstractMenuState {

    private boolean goingToMenuState = false;
    private String username = GravityRun.i18n.format("username");

    public FirstState(GameStateManager pGameStateManager, SoundManager pSoundManager) {
        super(pGameStateManager, pSoundManager);


        Label title = new Label(GravityRun.i18n.format("welcome"), titleSkin, "title");

        final TextButton startButton = new TextButton(GravityRun.i18n.format("start"), tableSkin, "round");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                validateUsername();
            }
        });
        final TextField usernameField = new TextField(username, tableSkin);
        usernameField.setText(username);
        usernameField.setMessageText(GravityRun.i18n.format("username"));
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
        usernameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n') {
                    validateUsername();
                }
            }
        });

        Container<Table> tableContainer = new Container<>();
        Table table = new Table();

        tableContainer.setSize(containerWidth, containerHeight);
        tableContainer.setPosition((width - containerWidth) / 2, (height - containerHeight) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(usernameField).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(startButton).expandX().fillX().padTop(height - containerHeight);

        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        if (! goingToMenuState)
            State.disposeSkins();
        stage.dispose();
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

    private void validateUsername() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        if (!User.checkUsername(username)) {
            spawnErrorDialog(stage, GravityRun.i18n.format("error_username_default"), User.getUsernameError(username));
        } else {
            initUser();
            goingToMenuState = true;
            gameStateManager.set(new HomeState(gameStateManager, soundManager)); // lolilol
        }
    }
}
