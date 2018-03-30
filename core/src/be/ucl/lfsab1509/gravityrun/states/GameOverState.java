package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
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

    GameOverState(GameStateManager gameStateManager, SoundManager soundManager) {
        super(gameStateManager, soundManager);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        ArrayList<Integer> userList = GravityRun.user.getHighScore();
        for (int i = 0; i < GravityRun.scoreList.size(); i++)
            if (GravityRun.scoreList.get(i) > userList.get(GravityRun.user.getIndexSelected()))
                GravityRun.user.getHighScore().set(GravityRun.user.getIndexSelected(), GravityRun.scoreList.get(i));

        TextButton menuButton = new TextButton(i18n.format("menu"), tableSkin, "round");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedMenuButton = true;
            }
        });
        TextButton replayButton = new TextButton(i18n.format("replay"), tableSkin, "round");
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                isClickedReplayButton = true;
            }
        });

        Label score = new Label(i18n.format("final_score", PlayState.score), aaronScoreSkin);
        Label highScore = new Label(i18n.format("high_score", GravityRun.user.getHighScore().get(GravityRun.user.getIndexSelected())), aaronScoreSkin);

        Label title = new Label(i18n.format("game_over"), titleSkin, "title");

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((width - cw) / 2, (height - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(score).padTop(height - ch);
        table.row();
        table.add(highScore).padTop(height - ch);
        table.row();
        table.add(replayButton).expandX().fillX().padTop((height - ch) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(height - ch);

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedReplayButton) {
            soundManager.replayGame();
            gameStateManager.set(new PlayState(gameStateManager, soundManager));
        }

        if (isClickedMenuButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            switch (GravityRun.user.getIndexSelected() + 1) {
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
            
            gameStateManager.pop();
        }
    }

    @Override
    public void update(float dt) {
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
