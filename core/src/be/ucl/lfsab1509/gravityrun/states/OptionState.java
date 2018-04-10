package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OptionState extends State {

    private List<String> listBox;
    private Stage stage;
    private String username;
    private TextButton saveButton;
    private TextField usernameField;

    OptionState(GravityRun gravityRun) {
        super(gravityRun);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        Label title = new Label(i18n.get("option"), titleSkin, "title");

        TextButton lvlButton = new TextButton(i18n.format("chose_lvl"), tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listBox.setVisible(!listBox.isVisible());
            }
        });

        saveButton = new TextButton(i18n.format("save"), tableSkin, "round");
        saveButton.setVisible(false);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);

                user.setUsername(username);
                pref.put(user.toMap());
                pref.flush();

                saveButton.setVisible(false);
                usernameField.setVisible(false);
            }
        });

        TextButton scoreButton = new TextButton(i18n.format("my_score"), tableSkin, "round");
        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new ScoreboardState(game));
            }
        });

        TextButton usernameButton = new TextButton(i18n.format("mod_username"), tableSkin, "round");
        usernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                saveButton.setVisible(!saveButton.isVisible());
                usernameField.setVisible(!usernameField.isVisible());
            }
        });

        username = user.getUsername();
        usernameField = new TextField(username, tableSkin);
        usernameField.setText(username);
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
                username = usernameField.getText();
            }
        });

        listBox = new List<String>(tableSkin);
        listBox.setItems(i18n.format("beginner"), i18n.format("inter"), i18n.format("expert"));
        listBox.setVisible(false);
        listBox.setSelectedIndex(user.getIndexSelected());
        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listBox.getSelected().equals(i18n.format("beginner")))
                    user.setIndexSelected(0);
                else if (listBox.getSelected().equals(i18n.format("inter")))
                    user.setIndexSelected(1);
                else if (listBox.getSelected().equals(i18n.format("expert")))
                    user.setIndexSelected(2);
                listBox.setVisible(false);
            }
        });

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();
        Table titleTable = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((width - cw) / 2, (height - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(titleTable);

        titleTable.row().expandY();
        titleTable.add(title).colspan(7).expandX();
        titleTable.row().colspan(7).fillX();
        titleTable.add(table);

        table.row().colspan(2);
        table.add(scoreButton).expandX().fillX().padTop(height - ch);
        table.row().colspan(2);
        table.add(usernameButton).expandX().fillX().padTop(height - ch).maxWidth(cw);
        table.row();
        table.add(usernameField).expandX().fillX();
        table.add(saveButton).expandX().fillX();
        table.row().colspan(2);
        table.add(lvlButton).expandX().fillX().padTop((height - ch) / 2).maxWidth(cw);
        table.row().colspan(2);
        table.add(listBox).fillX().top();

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void resume() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void update(float dt) {
        if (clickedBack()) {
            pref.put(user.toMap());
            pref.flush();

            screenManager.pop();
        }
    }

}
