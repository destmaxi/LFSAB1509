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
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OptionState extends State {

    private boolean isClickedSaveButton = false, isClickedScoreButton = false;
    private List<String> listBox;
    private Stage stage;
    private String username;
    private TextButton saveButton;
    private TextField usernameField;

    public OptionState(GravityRun game, SoundManager soundManager) {
        super(game, soundManager);

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
                isClickedSaveButton = true;
            }
        });

        TextButton scoreButton = new TextButton(i18n.format("my_score"), tableSkin, "round");
        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedScoreButton = true;
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

        username = GravityRun.user.getUsername();
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
        listBox.setSelectedIndex(GravityRun.user.getIndexSelected());
        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listBox.getSelected().equals(i18n.format("beginner")))
                    GravityRun.user.setIndexSelected(0);
                else if (listBox.getSelected().equals(i18n.format("inter")))
                    GravityRun.user.setIndexSelected(1);
                else if (listBox.getSelected().equals(i18n.format("expert")))
                    GravityRun.user.setIndexSelected(2);
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
    protected void handleInput() {
        if (isClickedSaveButton) {
            GravityRun.user.setUsername(username);
            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();
            saveButton.setVisible(false);
            usernameField.setVisible(false);
            isClickedSaveButton = false;
        }

        if (isClickedScoreButton) {
            isClickedScoreButton = false;
//            gameStateManager.push(new ScoreboardState(gameStateManager, soundManager));
            game.setScreen(new ScoreboardState(game, soundManager));
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();
//            gameStateManager.pop();
            game.setScreen(new MenuState(game, soundManager));
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(stage);
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
