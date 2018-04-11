package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreboardScreen extends Screen {

    private Stage stage;

    @SuppressWarnings("unchecked")
    ScoreboardScreen(GravityRun gravityRun) {
        super(gravityRun);

        float ch = height * 0.9f;
        float cw = width * 0.9f;

        Label beginnerLabel = new Label(i18n.format("beginner") + " :", labelScoreBoardSkin, "round");
        Label intermediateLabel = new Label(i18n.format("inter") + " :", labelScoreBoardSkin, "round");
        Label expertLabel = new Label(i18n.format("expert") + " :", labelScoreBoardSkin, "round");

        Label title = new Label(i18n.format("my_score"), titleSkin, "title");

        List beginnerScoreList = new List(tableScoreBoardSkin);
        List intermediateScoreList = new List(tableScoreBoardSkin);
        List expertScoreList = new List(tableScoreBoardSkin);

        java.util.List[] lists = {game.user.getBeginner(), game.user.getInter(), game.user.getExpert()};
        List[] list = {beginnerScoreList, intermediateScoreList, expertScoreList};
        ArrayList<Integer> myArrayList;

        for (int i = 0; i < 3; i++) {
            myArrayList = (ArrayList<Integer>) lists[i];
            int length;

            if (myArrayList != null) {
                Collections.sort(myArrayList, Collections.<Integer>reverseOrder());
                length = myArrayList.size();
            } else
                length = 0;

            switch (length) {
                case 0:
                    list[i].setItems("1.  " + 0, "2.  " + 0, "3.  " + 0);
                    break;
                case 1:
                    list[i].setItems("1.  " + myArrayList.get(0), "2.  " + 0, "3.  " + 0);
                    break;
                case 2:
                    list[i].setItems("1.  " + myArrayList.get(0), "2.  " + myArrayList.get(1), "3.  " + 0);
                    break;
                default:    // case 3:
                    list[i].setItems("1.  " + myArrayList.get(0), "2.  " + myArrayList.get(1), "3.  " + myArrayList.get(2));
                    break;
            }
        }

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((width - cw) / 2, (height - ch) / 2);
        tableContainer.top().fillX().fillY();
        tableContainer.setActor(table);

        table.add(title).colspan(5).expandX();
        table.row().expandX().expandY();
        table.add(beginnerLabel).expandY().colspan(3).fillX().left();
        table.add(beginnerScoreList).expandY().colspan(2).center();
        table.row().colspan(5);
        table.add(intermediateLabel).expandY().colspan(3).fillX().left();
        table.add(intermediateScoreList).expandY().colspan(2).center();
        table.row().colspan(5);
        table.add(expertLabel).expandY().colspan(3).fillX().left();
        table.add(expertScoreList).expandY().colspan(2).center();

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
            screenManager.pop();
            return;
        }

        game.spriteBatch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

}
