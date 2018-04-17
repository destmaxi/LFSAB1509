package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionScreen extends AbstractMenuScreen {

    private List<String> listBox;
    private TextButton saveButton;
    private TextField usernameField;
    private String newUsername, username;

    OptionScreen(GravityRun gravityRun) {
        super(gravityRun);

        newUsername = user.getUsername();
        username = user.getUsername();

        Label title = new Label(game.i18n.get("option"), titleSkin, "title");

        TextButton lvlButton = new TextButton(game.i18n.format("chose_lvl"), tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listBox.setVisible(!listBox.isVisible());
            }
        });

        TextButton usernameButton = new TextButton(game.i18n.format("mod_username"), tableSkin, "round");
        usernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleUsernameFields();
            }
        });

        saveButton = new TextButton(game.i18n.format("save"), tableSkin, "round");
        saveButton.setVisible(false);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                validateUserName();
            }
        });

        usernameField = new TextField(newUsername, tableSkin);
        usernameField.setText(newUsername);
        usernameField.setVisible(false);
        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                usernameField.selectAll();
            }
        });
        usernameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                newUsername = usernameField.getText();
            }
        });
        usernameField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                if (c == '\n' || c == '\r') {
                    validateUserName();
                }
            }
        });

        listBox = new List<String>(tableSkin);
        listBox.setItems(game.i18n.format("beginner"), game.i18n.format("inter"), game.i18n.format("expert"));
        listBox.setVisible(false);
        listBox.setSelectedIndex(user.getIndexSelected());
        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                validateLevelSelection();
            }
        });

        Table table = new Table();
        table.add(title).colspan(2).expandX();
        table.row();
        table.add(usernameButton).colspan(2).expandX().fillX().padTop(height - containerHeight).maxWidth(containerWidth);
        table.row();
        table.add(usernameField).expandX().fillX();
        table.add(saveButton).fillX();
        table.row();
        table.add(lvlButton).colspan(2).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(listBox).colspan(2).fillX().top();

        initStage(table);
    }

    @Override
    public void render(float dt) {
        if (clickedBack() && !dialog) {
            user.write();
            screenManager.pop();
            return;
        }

        super.render(dt);
    }

    private void toggleUsernameFields() {
        Gdx.input.setOnscreenKeyboardVisible(false);
        saveButton.setVisible(!saveButton.isVisible());
        usernameField.setVisible(!usernameField.isVisible());
        // TODO mettre ce code à un meilleur endroit
        if (!usernameField.isVisible() && !user.checkUsername(newUsername))
            // Le username n'est pas valide: on remet l'ancien.
            usernameField.setText(username);
    }

    private void validateUserName() {
        Gdx.input.setOnscreenKeyboardVisible(false);

        if (user.setUsername(newUsername)) {
            user.write();
        } else {
            usernameField.setText(username); // Sinon, le textField retient la valeur qu'on a rentré, qui est donc incorrecte.
            newUsername = username; // Don't forget me
            spawnErrorDialog(game.i18n.format("error_username_default"), user.getUsernameError(newUsername));
        }

        saveButton.setVisible(false);
        usernameField.setVisible(false);
    }

    private void validateLevelSelection() {
        if (listBox.getSelected().equals(game.i18n.format("beginner")))
            user.setIndexSelected(0);
        else if (listBox.getSelected().equals(game.i18n.format("inter")))
            user.setIndexSelected(1);
        else if (listBox.getSelected().equals(game.i18n.format("expert")))
            user.setIndexSelected(2);
        listBox.setVisible(false);
    }

}
