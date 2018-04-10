package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
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

    private Stage stage;

    GameOverState(GravityRun gravityRun) {
        super(gravityRun);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        ArrayList<Integer> userList = user.getHighScore();
        for (int i = 0; i < scoreList.size(); i++)
            if (scoreList.get(i) > userList.get(user.getIndexSelected()))
                user.getHighScore().set(user.getIndexSelected(), scoreList.get(i));

        TextButton menuButton = new TextButton(i18n.format("menu"), tableSkin, "round");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleReturn();
            }
        });
        TextButton replayButton = new TextButton(i18n.format("replay"), tableSkin, "round");
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundManager.replayGame();
                screenManager.set(new PlayState(game));
            }
        });

        Label score = new Label(i18n.format("final_score", PlayState.score), aaronScoreSkin);
        Label highScore = new Label(i18n.format("high_score", user.getHighScore().get(user.getIndexSelected())), aaronScoreSkin);

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
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        stage.draw();
    }

    @Override
    public void update(float dt) {
        if (clickedBack())
            handleReturn();
    }

    public ArrayList<Integer> add(ArrayList<Integer> userList) {
        for (int i = 0; i < scoreList.size(); i++) {
            if (userList != null)
                Collections.sort(userList);
            else
                userList = new ArrayList<Integer>();

            if (!userList.contains(scoreList.get(i)) && userList.size() < 3)
                userList.add(scoreList.get(i));
            else if (!userList.contains(scoreList.get(i)) && userList.get(0) < scoreList.get(i)) {
                userList.remove(0);
                userList.add(scoreList.get(i));
            }
        }

        return userList;
    }

    private void handleReturn() {
        switch (user.getIndexSelected() + 1) {
            case 1:
                user.setBeginner(add(user.getBeginner()));
                break;
            case 2:
                user.setInter(add(user.getInter()));
                break;
            case 3:
                user.setExpert(add(user.getExpert()));
                break;
        }

        pref.put(user.toMap());
        pref.flush();

        scoreList = null;

        screenManager.pop();
    }

}
