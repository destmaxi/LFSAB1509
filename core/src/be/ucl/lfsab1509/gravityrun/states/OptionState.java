package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Locale;

public class OptionState extends State {

    private boolean isCheckedLangButton = false, isClickedLangButton = false, isClickedReturnImButton = false, isClickedScoreButton = false;
    private final List<String> listBox;
    private Skin menuSkin, tableSkin;
    private Stage stage;
    private Texture returnImage;

    public OptionState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "BE", "VAR1");
        final I18NBundle string = I18NBundle.createBundle(baseFileHandle, locale);

        stage = new Stage(new ScreenViewport());

        menuSkin = new Skin();
        tableSkin = new Skin();

        tableSkin.createSkin(42);
        menuSkin.createSkin(62);

        returnImage = new Texture("back.png");

        Label title = new Label(string.get("option"),menuSkin, "title");
        TextButton lvlButton = new TextButton(string.format("chose_lvl"),tableSkin,"round");
        TextButton scoreButton = new TextButton(string.format("my_score"),tableSkin,"round");
        ImageButton returnImageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnImage)));
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

        scoreButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedScoreButton = true;
            }
        });

        returnImageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedReturnImButton = true;
            }
        });

        listBox.setSelectedIndex(GravityRun.indexSelected);
        listBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (listBox.getSelected().equals(string.format("beginner"))){
                    Marble.LVL=1;
                    GravityRun.indexSelected = 0;
                }
                else if(listBox.getSelected().equals(string.format("inter"))){
                    Marble.LVL=2;
                    GravityRun.indexSelected = 1;
                }
                else if(listBox.getSelected().equals(string.format("expert"))){
                    Marble.LVL=3;
                    GravityRun.indexSelected = 2;
                }
            }
        });

        Container<Table> tableContainer = new Container<Table>();

        Table table = new Table();
        Table titleTable = new Table();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2 );
        tableContainer.top().fillX();

        titleTable.row().expandY();
        titleTable.add(returnImageButton).expandX().left().size(cw / 6);
        titleTable.add(title).colspan(6).expandX().left();
        titleTable.row().colspan(7).fillX();

        titleTable.add(table);
        table.row();

        table.add(scoreButton).expandX().fillX().padTop(sh - ch);
        table.row();

        table.add(lvlButton).expandX().fillX().padTop(sh - ch);
        table.row();

        table.add(listBox).fillX().top();

        tableContainer.setActor(titleTable);
        stage.addActor(tableContainer);
    }

    @Override
    protected void handleInput() {
        if (isClickedReturnImButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
            gsm.pop();
        if (isClickedScoreButton) {
            isClickedScoreButton = false;
            gsm.push(new ScoreboardState(gsm));
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

        if (isClickedLangButton) {
            listBox.setVisible(true);
        } else {
            listBox.setVisible(false);
        }
    }

    @Override
    public void dispose() {
        menuSkin.dispose();
        returnImage.dispose();
        tableSkin.dispose();
        stage.dispose();
    }

}
