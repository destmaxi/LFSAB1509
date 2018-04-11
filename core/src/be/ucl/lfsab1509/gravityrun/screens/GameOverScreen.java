package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
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

public class GameOverScreen extends Screen {

    private Stage stage;

    GameOverScreen(GravityRun gravityRun) {
        super(gravityRun);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        ArrayList<Integer> userList = game.user.getHighScore();
        for (Integer score : game.scoreList)
            if (score > userList.get(game.user.getIndexSelected()))
                userList.set(game.user.getIndexSelected(), score);

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
                screenManager.set(new PlayScreen(game));
            }
        });

        Label score = new Label(i18n.format("final_score", PlayScreen.score), aaronScoreSkin);
        Label highScore = new Label(i18n.format("high_score", game.user.getHighScore().get(game.user.getIndexSelected())), aaronScoreSkin);

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
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float dt) {
        if (clickedBack()) {
            handleReturn();
            return;
        }

        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        soundManager.replayMenu();
    }

    public ArrayList<Integer> add(ArrayList<Integer> userList) {
        for (Integer score : game.scoreList) {
            if (userList != null)
                Collections.sort(userList);
            else
                userList = new ArrayList<Integer>();

            if (!userList.contains(score) && userList.size() < 3)
                userList.add(score);
            else if (!userList.contains(score) && userList.get(0) < score) {
                userList.remove(0);
                userList.add(score);
            }
        }

        return userList;
    }

    private void handleReturn() {
        switch (game.user.getIndexSelected() + 1) {
            case 1:
                game.user.setBeginner(add(game.user.getBeginner()));
                break;
            case 2:
                game.user.setInter(add(game.user.getInter()));
                break;
            case 3:
                game.user.setExpert(add(game.user.getExpert()));
                break;
        }

        game.pref.put(game.user.toMap());
        game.pref.flush();

        game.scoreList = null;

        screenManager.pop();
    }

}
