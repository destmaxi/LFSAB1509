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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class OptionState extends AbstractMenuState {

    private List<String> listBox;
    private TextButton saveButton;
    private TextField usernameField;
    private String username;
    private String newUsername;

    OptionState(GameStateManager gameStateManager, SoundManager soundManager) {
        super(gameStateManager, soundManager);

        Label title = new Label(GravityRun.i18n.get("option"), titleSkin, "title");

        TextButton lvlButton = new TextButton(GravityRun.i18n.format("chose_lvl"), tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listBox.setVisible(!listBox.isVisible());
            }
        });

        TextButton usernameButton = new TextButton(GravityRun.i18n.format("mod_username"), tableSkin, "round");
        usernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                saveButton.setVisible(!saveButton.isVisible());
                usernameField.setVisible(!usernameField.isVisible());
                // TODO mettre ce code à un meilleur endroit
                if (!usernameField.isVisible() && !User.checkUsername(newUsername))
                    // Le username n'est pas valide: on remet l'ancien.
                    usernameField.setText(username);
            }
        });

        saveButton = new TextButton(GravityRun.i18n.format("save"), tableSkin, "round");
        saveButton.setVisible(false);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                // isClickedSaveButton = true;

                if (User.checkUsername(newUsername)) {
                    GravityRun.user.setUsername(newUsername);
                    GravityRun.pref.put(GravityRun.user.toMap()); // TODO
                    GravityRun.pref.flush();
                    // TODO indiquer un message d'erreur (errorLabel.setText(User.getUsernameError(newUsername)))
                } else {
                    // Sinon, le textField retient la valeur qu'on a rentré, qui est donc incorrecte.
                    usernameField.setText(username);
                }

                saveButton.setVisible(false);
                usernameField.setVisible(false);
            }
        });

        newUsername = GravityRun.user.getUsername();
        username = GravityRun.user.getUsername();
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

        listBox = new List<String>(tableSkin);
        listBox.setItems(GravityRun.i18n.format("beginner"), GravityRun.i18n.format("inter"), GravityRun.i18n.format("expert"));
        listBox.setVisible(false);
        listBox.setSelectedIndex(GravityRun.user.getIndexSelected());
        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listBox.getSelected().equals(GravityRun.i18n.format("beginner")))
                    GravityRun.user.setIndexSelected(0);
                else if (listBox.getSelected().equals(GravityRun.i18n.format("inter")))
                    GravityRun.user.setIndexSelected(1);
                else if (listBox.getSelected().equals(GravityRun.i18n.format("expert")))
                    GravityRun.user.setIndexSelected(2);
                listBox.setVisible(false);
            }
        });

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(containerWidth, containerHeight);
        tableContainer.setPosition((width - containerWidth) / 2, (height - containerHeight) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

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

        stage.addActor(tableContainer);
    }

    @Override
    protected void handleInput() {
        // TODO faire en sorte que les préférences soient écrites sur le stockage dès que l'OptionState ou l'activité est quittée.
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();
            gameStateManager.pop();
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(stage);
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
