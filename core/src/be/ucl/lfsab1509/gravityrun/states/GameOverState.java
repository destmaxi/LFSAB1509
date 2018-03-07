package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.tools.DataBase;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Locale;

public class GameOverState extends State {

    private boolean isClickedMenuButton = false, isClickedReplayButton = false;
    private Stage stage;
    private Skin buttonSkin, scoreSkin, titleSkin;

    public GameOverState(GameStateManager gsm) {
        super(gsm);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "BE", "VAR1");
        final I18NBundle string = I18NBundle.createBundle(baseFileHandle, locale);

        titleSkin = new Skin();
        titleSkin.createSkin(62);
        Label title = new Label(string.format("game_over"), titleSkin, "title");

        scoreSkin = new Skin();
        scoreSkin.createSkin(28);
        Label score = new Label(string.format("final_score",PlayState.score), scoreSkin);

        buttonSkin = new Skin();
        buttonSkin.createSkin(42);
        TextButton menuButton = new TextButton(string.format("menu"), buttonSkin, "round");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedMenuButton = true;
            }
        });
        TextButton replayButton = new TextButton(string.format("replay"), buttonSkin, "round");
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedReplayButton = true;
            }
        });

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        stage = new Stage(new ScreenViewport());

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2 );
        tableContainer.top().fillX();

        table.add(title).top();
        table.row();
        table.add(score).padTop(sh - ch);
        table.row();
        table.add(replayButton).expandX().fillX().padTop((sh - ch) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(sh - ch);

        stage = new Stage(new ScreenViewport());
        tableContainer.setActor(table);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedMenuButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            /*DataBase dataBase = new DataBase();
            String col = null;

            for (int i = 0; i < GravityRun.scoreList.size(); i++) {
                switch (Marble.LVL){
                    case 1: col = DataBase.COLUMN_BEGINNER;
                        break;
                    case 2: col = DataBase.COLUMN_INTERMEDIATE;
                        break;
                    case 3: col = DataBase.COLUMN_EXPERT;
                        break;
                }
                dataBase.add(col);
            }
            dataBase.dispose();
            DataBase.scoreList.clear();
            DataBase.scoreList = null;
            GravityRun.scoreList.clear();
            GravityRun.scoreList = null;*/
            gsm.pop();
        }

        if (isClickedReplayButton)
            gsm.set(new PlayState(gsm));
    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        stage.draw();
    }

    @Override
    public void dispose() {
        buttonSkin.dispose();
        scoreSkin.dispose();
        stage.dispose();
        titleSkin.dispose();
    }

}
