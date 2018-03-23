package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
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

    private boolean isCheckedLvlButton = false, isCheckedUsernameButton = false, isClickedLvlButton = false, isClickedSaveButton = false, isClickedScoreButton = false, isClickedUsernameButton = false;
    private final List<String> listBox;
    private Skin menuSkin, tableSkin;
    private Stage stage;
    private String username;
    private TextField usernameField;
    private TextButton saveButton;

    OptionState(GameStateManager gsm) {
        super(gsm);

        float ch = h * 0.9f;
        float cw = w * 0.9f;

        menuSkin = new Skin();
        menuSkin.createSkin((int) (1.5f * w / d / 10));
        Label title = new Label(string.get("option"), menuSkin, "title");

        tableSkin = new Skin();
        tableSkin.createSkin((int) (w / d / 10));

        TextButton lvlButton = new TextButton(string.format("chose_lvl"), tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isCheckedLvlButton) {
                    isClickedLvlButton = true;
                    isCheckedLvlButton = true;
                } else {
                    isClickedLvlButton = false;
                    isCheckedLvlButton = false;
                }
            }
        });

        saveButton = new TextButton(string.format("save"), tableSkin, "round");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedSaveButton = true;
            }
        });
        saveButton.setVisible(false);

        TextButton scoreButton = new TextButton(string.format("my_score"), tableSkin, "round");
        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedScoreButton = true;
            }
        });

        TextButton usernameButton = new TextButton(string.format("mod_username"), tableSkin, "round");
        usernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isCheckedUsernameButton) {
                    isClickedUsernameButton = true;
                    isCheckedUsernameButton = true;
                } else {
                    isClickedUsernameButton = false;
                    isCheckedUsernameButton = false;
                }
            }
        });

        username = GravityRun.user.getUsername();
        usernameField = new TextField(username, tableSkin);
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
        usernameField.setVisible(false);

        listBox = new List<String>(tableSkin);
        listBox.setItems(string.format("beginner"), string.format("inter"), string.format("expert"));
        listBox.setVisible(false);
        listBox.setSelectedIndex(GravityRun.user.getIndexSelected());
        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listBox.getSelected().equals(string.format("beginner"))) {
                    Marble.LVL = 1;
                    GravityRun.user.setIndexSelected(0);
                    isCheckedLvlButton = false;
                    isClickedLvlButton = false;
                    listBox.setVisible(false);
                } else if (listBox.getSelected().equals(string.format("inter"))) {
                    Marble.LVL = 2;
                    GravityRun.user.setIndexSelected(1);
                    isCheckedLvlButton = false;
                    isClickedLvlButton = false;
                    listBox.setVisible(false);
                } else if (listBox.getSelected().equals(string.format("expert"))) {
                    Marble.LVL = 3;
                    GravityRun.user.setIndexSelected(2);
                    isCheckedLvlButton = false;
                    isClickedLvlButton = false;
                    listBox.setVisible(false);
                }
            }
        });

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();
        Table titleTable = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((w - cw) / 2,(h - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(titleTable);

        titleTable.row().expandY();
        titleTable.add(title).colspan(7).expandX();
        titleTable.row().colspan(7).fillX();
        titleTable.add(table);

        table.row().colspan(2);
        table.add(scoreButton).expandX().fillX().padTop(h - ch);
        table.row().colspan(2);
        table.add(usernameButton).expandX().fillX().padTop(h - ch).maxWidth(cw);
        table.row();
        table.add(usernameField).expandX().fillX();
        table.add(saveButton).expandX().fillX();
        table.row().colspan(2);
        table.add(lvlButton).expandX().fillX().padTop((h - ch) / 2).maxWidth(cw);
        table.row().colspan(2);
        table.add(listBox).fillX().top();

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            gsm.pop();
        if (isClickedScoreButton) {
            isClickedScoreButton = false;
            gsm.push(new ScoreboardState(gsm));
        }
        if (isClickedSaveButton) {
            GravityRun.user.setUsername(username);
            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();
            usernameField.setVisible(false);
            saveButton.setVisible(false);
            isCheckedUsernameButton = false;
            isClickedUsernameButton = false;
            isClickedSaveButton = false;
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(stage);
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);

        stage.act();
        stage.draw();

        if (isClickedLvlButton)
            listBox.setVisible(true);
        else
            listBox.setVisible(false);

        if (isClickedUsernameButton){
            usernameField.setVisible(true);
            saveButton.setVisible(true);
        } else {
            usernameField.setVisible(false);
            saveButton.setVisible(false);
        }
    }

    @Override
    public void dispose() {
        menuSkin.dispose();
        tableSkin.dispose();
        stage.dispose();
    }

}
