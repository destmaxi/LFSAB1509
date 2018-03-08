package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.tools.DataBase;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;

public class ScoreboardState extends State {

    private boolean isClickedReturnImButton = false;
    private Skin labelSkin, menuSkin, tableSkin;
    private Stage stage;
    private Texture returnTexture;

    public ScoreboardState(GameStateManager gsm){
        super(gsm);

        stage = new Stage(new ScreenViewport());
        Table table = new Table();
        Table titleTable = new Table();

        menuSkin = new Skin();
        tableSkin = new Skin();
        labelSkin = new Skin();

        tableSkin.createSkin(24);
        labelSkin.createSkin(38);
        menuSkin.createSkin(62);

        returnTexture = new Texture("back.png");

        Label title = new Label(string.format("my_score"),menuSkin, "title");
        Label beginnerLabel = new Label(string.format("beginner")+" :",labelSkin,"round");
        Label intermediateLabel = new Label(string.format("inter")+" :",labelSkin,"round");
        Label expertLabel = new Label(string.format("expert")+" :",labelSkin,"round");
        ImageButton returnImageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(returnTexture)));
        List beginnerScoreList = new List(tableSkin);
        List intermediateScoreList = new List(tableSkin);
        List expertScoreList = new List(tableSkin);

        returnImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedReturnImButton = true;
            }
        });

        // Create date base
        DataBase dataBase = new DataBase();

        String[] stringTab = {DataBase.COLUMN_BEGINNER, DataBase.COLUMN_INTERMEDIATE, DataBase.COLUMN_EXPERT};
        List[] list = {beginnerScoreList, intermediateScoreList, expertScoreList};
        ArrayList<Integer> myArrayList;
        for (int i = 0; i < 3; i++) {
            myArrayList = dataBase.getColumn(stringTab[i]);
            dataBase.sortDESC(myArrayList);
            switch (myArrayList.size()) {
                case 0: break;
                case 1: list[i].setItems("1.     "+myArrayList.get(0),"2.     "+0,"3.     "+0);
                break;
                case 2: list[i].setItems("1.     "+myArrayList.get(0),"2.     "+ myArrayList.get(1),"3.     "+0);
                break;
                case 3: list[i].setItems("1.     "+myArrayList.get(0),"2.     "+ myArrayList.get(1),"3.     "+ myArrayList.get(2));
                break;
                default : list[i].setItems("1.     "+myArrayList.get(0),"2.     "+ myArrayList.get(1),"3.     "+ myArrayList.get(2));
                break;
            }
        }

        dataBase.dispose();

        Container<Table> tableContainer = new Container<Table>();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2 );
        tableContainer.top().fillX().fillY();

        titleTable.row();
        titleTable.add(returnImageButton).expandX().left().size(cw / 6);
        titleTable.add(title).colspan(6).expandX().left();
        titleTable.row().expandX().expandY().colspan(7).fillX();

        titleTable.add(table).colspan(7).top().fillX().fillY().expandY().expandX();
        table.row().colspan(7).expandX().expandY();

        table.add(beginnerLabel).expandY().colspan(3).fillX().left();
        table.add(beginnerScoreList).expandY().colspan(2).center();
        table.row().colspan(7);

        table.add(intermediateLabel).expandY().colspan(3).fillX().left();
        table.add(intermediateScoreList).expandY().colspan(2).center();
        table.row().colspan(7);

        table.add(expertLabel).expandY().colspan(3).fillX().left();
        table.add(expertScoreList).expandY().colspan(2).center();

        tableContainer.setActor(titleTable);
        stage.addActor(tableContainer);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (isClickedReturnImButton || Gdx.input.isKeyJustPressed(Input.Keys.BACK))
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
        returnTexture.dispose();
        stage.dispose();
        tableSkin.dispose();
    }

}
