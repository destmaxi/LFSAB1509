package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreboardScreen extends AbstractMenuScreen {

    @SuppressWarnings("unchecked")
    ScoreboardScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.format("my_score"), titleSkin, "title");

        Label beginnerLabel = new Label(game.i18n.format("beginner") + " :", labelScoreBoardSkin, "round");
        Label intermediateLabel = new Label(game.i18n.format("inter") + " :", labelScoreBoardSkin, "round");
        Label expertLabel = new Label(game.i18n.format("expert") + " :", labelScoreBoardSkin, "round");

        List beginnerScoreList = new List(tableScoreBoardSkin);
        List intermediateScoreList = new List(tableScoreBoardSkin);
        List expertScoreList = new List(tableScoreBoardSkin);

        java.util.List[] lists = {user.getBeginner(), user.getInter(), user.getExpert()};
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

        Table table = new Table();
        table.add(title).colspan(5).expandX();
        table.row();//.expandX().expandY();
        table.add(beginnerLabel).expandY().colspan(3).fillX().left();
        table.add(beginnerScoreList).expandY().colspan(2).center();
        table.row();//.colspan(5);
        table.add(intermediateLabel).expandY().colspan(3).fillX().left();
        table.add(intermediateScoreList).expandY().colspan(2).center();
        table.row();//.colspan(5);
        table.add(expertLabel).expandY().colspan(3).fillX().left();
        table.add(expertScoreList).expandY().colspan(2).center();

        initStage(table);
    }

    @Override
    public void render(float dt) {
        if (clickedBack()) {
            screenManager.pop();
            return;
        }

        super.render(dt);
    }

}
