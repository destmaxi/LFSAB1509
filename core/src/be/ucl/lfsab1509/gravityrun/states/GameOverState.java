package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.sprites.Marble;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
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
import java.util.Comparator;

public class GameOverState extends State {

    private boolean isClickedMenuButton = false, isClickedReplayButton = false;
    private Stage stage;
    private Skin buttonSkin, scoreSkin, titleSkin;

    public GameOverState(GameStateManager gsm) {
        super(gsm);

        titleSkin = new Skin();
        titleSkin.createSkin(62);
        Label title = new Label(string.format("game_over"), titleSkin, "title");

        scoreSkin = new Skin();
        scoreSkin.createSkin(28);
        Label score = new Label(string.format("final_score",PlayState.score), scoreSkin);

        buttonSkin = new Skin();
        buttonSkin.createSkin(42);
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

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        stage = new Stage(new ScreenViewport());

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2 );
        tableContainer.top().fillX();

        table.add(title).top();
        table.row();
        table.add(score).padTop(sh - ch);
        table.row();
        table.add(replayButton).expandX().fillX().padTop((sh - ch) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(sh - ch);

        stage = new Stage(new ScreenViewport());
        tableContainer.setActor(table);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedMenuButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            String col = null;

            switch (Marble.LVL) {
                case 1: col = GravityRun.DEB;
                    break;
                case 2: col = GravityRun.INTER;
                    break;
                case 3: col = GravityRun.EXPERT;
                    break;
            }

            add(col);
            GravityRun.scoreList = null;
            gsm.pop();
        }

        if (isClickedReplayButton)
            gsm.set(new PlayState(gsm));
    }

    public void add(String col){
       sortDESC(GravityRun.scoreList);
        int score1 = GravityRun.pref.getInteger(col+"_1");
        int score2 = GravityRun.pref.getInteger(col+"_2");
        int score3 = GravityRun.pref.getInteger(col+"_3");
       for (int i=0; i <GravityRun.scoreList.size(); i++){
           if(score1 < GravityRun.scoreList.get(i)){
               GravityRun.pref.putInteger(col+"_1",GravityRun.scoreList.get(i));
               GravityRun.pref.putInteger(col+"_2",score1);
               GravityRun.pref.putInteger(col+"_3",score2);
               GravityRun.pref.flush();
               score3 = score2;
               score2 = score1;
               score1 = GravityRun.scoreList.get(i);
           }
           else if(score1 > GravityRun.scoreList.get(i) && score2 < GravityRun.scoreList.get(i)){
               GravityRun.pref.putInteger(col+"_2",GravityRun.scoreList.get(i));
               GravityRun.pref.putInteger(col+"_3",score2);
               GravityRun.pref.flush();
               score3 = score2;
               score2 = GravityRun.scoreList.get(i);
           }
           else if(score2 > GravityRun.scoreList.get(i) && score3 < GravityRun.scoreList.get(i)){
               GravityRun.pref.putInteger(col+"_3",GravityRun.scoreList.get(i));
               GravityRun.pref.flush();
               score3 = GravityRun.scoreList.get(i);
           }

       }
    }

    private void sortDESC(ArrayList<Integer> arrayList){
        Collections.sort(arrayList, new Comparator<Integer>() {
            @Override
            public int compare(Integer integer, Integer t1) {
                return t1.compareTo(integer);
            }
        });
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

}
