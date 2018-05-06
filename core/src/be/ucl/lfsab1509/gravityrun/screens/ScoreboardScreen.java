package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

class ScoreboardScreen extends AbstractMenuScreen {

    private static final int HIGH_SCORE_COUNT = 3;

    ScoreboardScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.format("my_score"), game.titleSkin, "title");

        Label beginnerLabel = new Label(game.i18n.format("beginner") + " :", game.labelScoreBoardSkin, "round");
        Label intermediateLabel = new Label(game.i18n.format("inter") + " :", game.labelScoreBoardSkin, "round");
        Label expertLabel = new Label(game.i18n.format("expert") + " :", game.labelScoreBoardSkin, "round");

        List[] listWidget = new List[3];

        for (int level = 0; level < 3; level++) {
            listWidget[level] = new List(game.tableScoreBoardSkin);
            Array<String> array = new Array<>(HIGH_SCORE_COUNT);
            ArrayList<Integer> list = game.user.getHighScores(level, HIGH_SCORE_COUNT);
            for (int i = 0; i < HIGH_SCORE_COUNT; i++)
                array.add(i + ".  " + list.get(i));
            listWidget[level].setItems(array);
        }

        Table table = new Table();
        table.add(title).colspan(5).expandX();
        table.row();//.expandX().expandY();
        table.add(beginnerLabel).expandY().colspan(3).fillX().left();
        table.add(listWidget[0]).expandY().colspan(2).center();
        table.row();//.colspan(5);
        table.add(intermediateLabel).expandY().colspan(3).fillX().left();
        table.add(listWidget[1]).expandY().colspan(2).center();
        table.row();//.colspan(5);
        table.add(expertLabel).expandY().colspan(3).fillX().left();
        table.add(listWidget[2]).expandY().colspan(2).center();

        initStage(table);
    }

}
