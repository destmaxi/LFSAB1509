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

    private boolean isCheckedLangButton = false, isClickedLangButton = false, isClickedScoreButton = false, isClickedUsernameButton = false, isClickedSaveButton = false, isCheckedUsernameButton = false, isClickedLVLButton = false;
    private final List<String> listBox;
    private Skin menuSkin, tableSkin;
    private Stage stage;
    private String username;
    private TextField usernameField;
    private TextButton saveButton;

    public OptionState(GameStateManager gsm) {
        super(gsm);

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;

        stage = new Stage(new ScreenViewport());

        menuSkin = new Skin();
        tableSkin = new Skin();

        tableSkin.createSkin(42);
        menuSkin.createSkin(62);

        Label title = new Label(string.get("option"), menuSkin, "title");
        TextButton lvlButton = new TextButton(string.format("chose_lvl"), tableSkin, "round");
        TextButton scoreButton = new TextButton(string.format("my_score"), tableSkin, "round");
        saveButton = new TextButton(string.format("save"), tableSkin, "round");
        TextButton usernameButton = new TextButton(string.format("mod_username"),tableSkin,"round");
        listBox = new List<String>(tableSkin);

        listBox.setItems(string.format("beginner"), string.format("inter"), string.format("expert"));
        listBox.setVisible(false);

        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isCheckedLangButton) {
                    isClickedLangButton = true;
                    isCheckedLangButton = true;
                } else {
                    isClickedLangButton = false;
                    isCheckedLangButton = false;
                }
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedSaveButton = true;
            }
        });

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

        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedScoreButton = true;
            }
        });

        saveButton.setVisible(false);

        listBox.setSelectedIndex(GravityRun.user.getIndexSelected());
        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listBox.getSelected().equals(string.format("beginner"))) {
                    Marble.LVL = 1;
                    GravityRun.user.setIndexSelected(0);
                    isClickedLVLButton = true;
                }
                else if(listBox.getSelected().equals(string.format("inter"))) {
                    Marble.LVL = 2;
                    GravityRun.user.setIndexSelected(1);
                    isClickedLVLButton = true;
                }
                else if(listBox.getSelected().equals(string.format("expert"))) {
                    Marble.LVL = 3;
                    GravityRun.user.setIndexSelected(2);
                    isClickedLVLButton = true;
                }
            }
        });

        username = GravityRun.user.getUsername();
        usernameField = new TextField(username, tableSkin);
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

        usernameField.setVisible(false);

        Container<Table> tableContainer = new Container<Table>();

        Table table = new Table();
        Table titleTable = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2);
        tableContainer.top().fillX();

        /*titleTable.row().expandY();
        titleTable.add(title).colspan(7).expandX();
        titleTable.row().colspan(7).fillX();
        titleTable.add(table);

        table.row().colspan(2);
        table.add(scoreButton).expandX().fillX().padTop(sh - ch);
        table.row().colspan(2);
        table.add(usernameButton).expandX().fillX().padTop(sh - ch);
        table.row();
        table.add(usernameField).expandX().fillX();
        table.add(saveButton).expandX().fillX();
        table.row().colspan(2);
        table.add(lvlButton).expandX().fillX().padTop((sh - ch)/2);
        table.row().colspan(2);
        table.add(listBox).fillX().top();*/

        titleTable.row().expandY();
        titleTable.add(title).colspan(7).expandX();
        titleTable.row().colspan(7).fillX();
        titleTable.add(table);

        table.row().colspan(2);
        table.add(scoreButton).expandX().fillX().padTop(sh - ch);
        table.row().colspan(2);
        table.add(usernameButton).expandX().fillX().padTop(sh - ch).maxWidth(cw);
        table.row();
        table.add(usernameField).expandX().fillX();
        table.add(saveButton).expandX().fillX();
        table.row().colspan(2);
        table.add(lvlButton).expandX().fillX().padTop((sh - ch)/2).maxWidth(cw);
        table.row().colspan(2);
        table.add(listBox).fillX().top();


        tableContainer.setActor(titleTable);
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
        if(isClickedSaveButton){
            GravityRun.user.setUsername(username);
            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();
            usernameField.setVisible(false);
            saveButton.setVisible(false);
            isCheckedUsernameButton = false;
            isClickedUsernameButton = false;
            isClickedSaveButton = false;
        }

        if(isClickedLVLButton){
            listBox.setVisible(false);
            isCheckedLangButton = false;
            isClickedLangButton = false;
            isClickedLVLButton = false;
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

        if (isClickedLangButton)
            listBox.setVisible(true);
        else
            listBox.setVisible(false);

        if (isClickedUsernameButton){
            usernameField.setVisible(true);
            saveButton.setVisible(true);
        }
        else{
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
