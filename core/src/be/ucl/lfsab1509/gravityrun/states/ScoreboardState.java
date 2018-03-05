package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Locale;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;

/**
 * Created by maxime on 05/03/2018.
 */

public class ScoreboardState extends State {
    public static ArrayList<Integer> myScores;
    private Stage stage;

    public ScoreboardState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        FileHandle baseFileHandle = Gdx.files.internal("strings/string");
        Locale locale = new Locale("fr", "CA", "VAR1");
        final I18NBundle string = I18NBundle.createBundle(baseFileHandle, locale);

        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.top();
        table.setFillParent(true);

        Skin menuSkin = new Skin();
        Skin tableSkin = new Skin();

        tableSkin.createSkin(18);
        menuSkin.createSkin(62);

        Label title = new Label(string.get("my_score"),menuSkin, "title");
        List listBox = new List(tableSkin);

        table.add(title);


    }

    public void add(int score){
        for(int i = 0; i<myScores.size();i++){
            if(myScores.get(i) <= score)
                myScores.add(i,score);

            i++;
        }
    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
