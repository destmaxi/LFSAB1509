package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreboardState extends State {

    private Skin labelSkin, menuSkin, tableSkin;
    private Stage stage;

    @SuppressWarnings("unchecked")
    ScoreboardState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);

        float ch = h * 0.9f;
        float cw = w * 0.9f;

        labelSkin = new Skin();
        labelSkin.createSkin((int) (0.9f * w / d / 10));
        Label beginnerLabel = new Label(string.format("beginner") + " :", labelSkin, "round");
        Label intermediateLabel = new Label(string.format("inter") + " :", labelSkin, "round");
        Label expertLabel = new Label(string.format("expert") + " :", labelSkin, "round");

        menuSkin = new Skin();
        menuSkin.createSkin((int) (1.5f * w / d / 10));
        Label title = new Label(string.format("my_score"), menuSkin, "title");

        tableSkin = new Skin();
        tableSkin.createSkin((int) (0.5f * w / d / 10));
        List beginnerScoreList = new List(tableSkin);
        List intermediateScoreList = new List(tableSkin);
        List expertScoreList = new List(tableSkin);

        java.util.List[] lists = {GravityRun.user.getBeginner(),GravityRun.user.getInter(),GravityRun.user.getExpert()};
        List[] list = {beginnerScoreList, intermediateScoreList, expertScoreList};
        ArrayList<Integer> myArrayList;

        for (int i = 0; i < 3; i++) {
            myArrayList = (ArrayList<Integer>) lists[i];
            int length;

            if (myArrayList != null) {
                sortDESC(myArrayList);
                length = myArrayList.size();
            } else
                length = 0;

            switch (length) {
                case 0:
                    list[i].setItems("1.  " + 0,"2.  " + 0, "3.  " + 0);
                    break;
                case 1:
                    list[i].setItems("1.  " + myArrayList.get(0), "2.  " + 0, "3.  " + 0);
                    break;
                case 2:
                    list[i].setItems("1.  " + myArrayList.get(0), "2.  " + myArrayList.get(1), "3.  " + 0);
                    break;
                case 3:
                    list[i].setItems("1.  " + myArrayList.get(0), "2.  " + myArrayList.get(1), "3.  " + myArrayList.get(2));
                    break;
                default:
                    list[i].setItems("1.  " + myArrayList.get(0), "2.  " + myArrayList.get(1), "3.  " + myArrayList.get(2));
                    break;
            }
        }

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((w - cw) / 2,(h - ch) / 2 );
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

        Gdx.input.setInputProcessor(stage);
    }

    private void sortDESC(ArrayList<Integer> arrayList) {
        Collections.sort(arrayList, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return t1.compareTo(integer);
            }
        });
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            gsm.pop();
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        labelSkin.dispose();
        menuSkin.dispose();
        stage.dispose();
        tableSkin.dispose();
    }

}
