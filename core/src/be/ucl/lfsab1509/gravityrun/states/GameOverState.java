package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;

public class GameOverState extends State {

    private boolean isClickedMenuButton = false, isClickedReplayButton = false;
    private Stage stage;
    private Skin buttonSkin, scoreSkin, titleSkin;

    GameOverState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);

        float ch = h * 0.9f;
        float cw = w * 0.9f;

        ArrayList<Integer> userList = GravityRun.user.getHighScore();
        for (int i = 0; i < GravityRun.scoreList.size(); i++)
            if (GravityRun.scoreList.get(i) > userList.get(Marble.lvl - 1))
                GravityRun.user.getHighScore().set(Marble.lvl - 1, GravityRun.scoreList.get(i));

        buttonSkin = new Skin();
        buttonSkin.createSkin((int) (w / d / 10));
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

        scoreSkin = new Skin();
        scoreSkin.createSkin((int) (0.75f * w / d / 10));
        Label score = new Label(string.format("final_score", PlayState.score), scoreSkin);
        Label highScore = new Label(string.format("high_score", GravityRun.user.getHighScore().get(Marble.lvl - 1)), scoreSkin);

        titleSkin = new Skin();
        titleSkin.createSkin((int) (1.5f * w / d / 10));
        Label title = new Label(string.format("game_over"), titleSkin, "title");

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((w - cw) / 2, (h - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(score).padTop(h - ch);
        table.row();
        table.add(highScore).padTop(h - ch);
        table.row();
        table.add(replayButton).expandX().fillX().padTop((h - ch) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(h - ch);

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedReplayButton) {
            PlayState.gameOver = false;
            PlayState.isCollideWall = false;
            soundManager.replayGame();
            gsm.set(new PlayState(gsm, soundManager));
        }

        if (isClickedMenuButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switch (Marble.lvl) {
                case 1:
                    GravityRun.user.setBeginner(add(GravityRun.user.getBeginner()));
                    break;
                case 2:
                    GravityRun.user.setInter(add(GravityRun.user.getInter()));
                    break;
                case 3:
                    GravityRun.user.setExpert(add(GravityRun.user.getExpert()));
                    break;
            }

            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();

            GravityRun.scoreList = null;

            gsm.pop();
        }
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

    public ArrayList<Integer> add(ArrayList<Integer> userList) {
        for (int i = 0; i < GravityRun.scoreList.size(); i++) {
            if (userList != null)
                Collections.sort(userList);
            else
                userList = new ArrayList<Integer>();

            if (!userList.contains(GravityRun.scoreList.get(i)) && userList.size() < 3)
                userList.add(GravityRun.scoreList.get(i));
            else if (!userList.contains(GravityRun.scoreList.get(i)) && userList.get(0) < GravityRun.scoreList.get(i)) {
                userList.remove(0);
                userList.add(GravityRun.scoreList.get(i));
            }
        }

        return userList;
    }

}
