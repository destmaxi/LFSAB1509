package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class FirstScreen extends AbstractMenuScreen {

    private boolean goingToMenuScreen = false;
    private String username = game.i18n.format("username");

    public FirstScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.format("welcome"), titleSkin, "title");

        final TextButton startButton = new TextButton(game.i18n.format("start"), tableSkin, "round");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                validateUsername();
            }
        });
        final TextField usernameField = new TextField(username, tableSkin);
        usernameField.setText(username);
        usernameField.setMessageText(game.i18n.format("username"));
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
                if (c == '\n' || c== '\r') {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    validateUsername();
                }
            }
        });

        Table table = new Table();
        table.add(title).top();
        table.row();
        table.add(usernameField).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(startButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (!goingToMenuScreen)
            disposeSkins();
    }

    @Override
    public void render(float dt) {
        if (clickedBack() && openDialogs == 0) {
            disposeSkins();
            game.exit();
            return;
        }

        super.render(dt);
    }

    private void initUser() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++)
            arrayList.add(0);

        user.setUsername(username);
        user.setFirstTimeTrue();
        user.setBeginner(new ArrayList<Integer>());
        user.setInter(new ArrayList<Integer>());
        user.setExpert(new ArrayList<Integer>());
        user.setIndexSelected(1);
        user.setHighScore(arrayList);

        user.write();
    }

    private void validateUsername() {
        if (!user.checkUsername(username)) {
            spawnErrorDialog(game.i18n.format("error_username_default"), user.getUsernameError(username));
        } else {
            initUser();
            goingToMenuScreen = true;
            screenManager.set(new HomeScreen(game));
        }
    }
}
