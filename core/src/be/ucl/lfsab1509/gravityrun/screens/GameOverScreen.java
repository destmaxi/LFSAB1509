package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.Collections;

public class GameOverScreen extends AbstractMenuScreen {

    GameOverScreen(GravityRun gravityRun) {
        super(gravityRun);

        ArrayList<Integer> userList = user.getHighScore();
        for (Integer score : game.scoreList)
            if (score > userList.get(user.getIndexSelected()))
                userList.set(user.getIndexSelected(), score);

        TextButton menuButton = new TextButton(game.i18n.format("menu"), tableSkin, "round");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                handleReturn();
            }
        });
        TextButton replayButton = new TextButton(game.i18n.format("replay"), tableSkin, "round");
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.set(new PlayScreen(game));
            }
        });

        Label highScore = new Label(game.i18n.format("high_score", user.getHighScore().get(user.getIndexSelected())), aaronScoreSkin);
        Label score = new Label(game.i18n.format("final_score", PlayScreen.score), aaronScoreSkin);
        Label title = new Label(game.i18n.format("game_over"), titleSkin, "title");

        Table table = new Table();
        table.add(title).top();
        table.row();
        table.add(score).padTop(height - containerHeight);
        table.row();
        table.add(highScore).padTop(height - containerHeight);
        table.row();
        table.add(replayButton).expandX().fillX().padTop((height - containerHeight) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

    @Override
    public void render(float dt) {
        if (clickedBack()) {
            handleReturn();
            return;
        }

        super.render(dt);
    }

    public ArrayList<Integer> add(ArrayList<Integer> userList) {
        for (Integer score : game.scoreList) {
            if (userList != null)
                Collections.sort(userList);
            else
                userList = new ArrayList<>();

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

        user.write();
        game.scoreList = null;
        screenManager.pop();
    }

}
