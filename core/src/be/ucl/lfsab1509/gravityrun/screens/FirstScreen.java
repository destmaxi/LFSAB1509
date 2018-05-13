package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FirstScreen extends AbstractMenuScreen {

    public FirstScreen(GravityRun gravityRun) {
        super(gravityRun);

        String username = game.i18n.format("username");

        Label title = new Label(game.i18n.format("welcome"), game.titleSkin, "title");

        TextField usernameField = new TextField(username, game.tableSkin);
        usernameField.setMessageText(game.i18n.format("username"));
        usernameField.setText(username);
        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                usernameField.selectAll();
            }
        });
        usernameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    Gdx.input.setOnscreenKeyboardVisible(false);
                    String username = usernameField.getText();
                    validateUsername(username);
                }
            }
        });

        TextButton startButton = new TextButton(game.i18n.format("start"), game.tableSkin, "round");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                String username = usernameField.getText();
                validateUsername(username);
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

    private void validateUsername(String username) {
        if (!User.checkUsername(username))
            spawnErrorDialog(game.i18n.format("error_username_default"), User.getUsernameError(username));
        else {
            game.user = new User(game, username);
            screenManager.set(new HomeScreen(game));
        }
    }

}
