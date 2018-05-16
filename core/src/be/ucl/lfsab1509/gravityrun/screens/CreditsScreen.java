package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

class CreditsScreen extends AbstractMenuScreen {

    CreditsScreen(GravityRun gravityRun) {
        super(gravityRun);

        containerWidth = width;

        Label titre = new Label(game.i18n.format("credits"), game.titleSkin, "title");

        Label gravityRunText = new Label("Gravity Run", game.tableSkin);
        gravityRunText.setAlignment(Align.center);
        Label mainText = new Label(
                        "Design\n-------\nCédric De Bontridder\nMaxime de Streel\n\n" +
                        "Programming\n-------\nCédric De Bontridder\nMaxime de Streel\nJean-Martin Vlaeminck\n\n" +
                        "Hardware support\n-------\nJean-Martin Vlaeminck\n\n" +
                        "Music and sound design\n-------\nArthur van Stratum\n\n" +
                        "Music and Sound Credits\n-----\n" +
                        "See repo for details. All musics and sounds are licensed under Creative Commons License\n"
                , game.aaronScoreSkin);
        mainText.setWrap(true);
        mainText.setAlignment(Align.center);
        Table table = new Table();
        table.add(titre).top();
        table.row();
        Table subTable = new Table();
        subTable.add(gravityRunText).expandX().width(width * 0.9f);
        subTable.row();
        subTable.add(mainText).expandX().width(width * 0.9f).padTop((height - containerHeight) / 2);
        ScrollPane.ScrollPaneStyle scrollPaneStyle = game.tableSkin.get(ScrollPane.ScrollPaneStyle.class);
        scrollPaneStyle.background = null;
        scrollPaneStyle.corner = null;
        ScrollPane scrollPane = new ScrollPane(subTable, scrollPaneStyle);
        table.add(scrollPane).expandX().width(width).padTop(height - containerHeight);
        initStage(table);
    }
}
