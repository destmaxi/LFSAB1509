package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;

/**
 * Created by maxime on 05/03/2018.
 */

public class ScoreboardState extends State {
    public static ArrayList<Integer> myScores;
    private Stage stage;

    private Database dbHandler;
    private ArrayList<Integer> arrayList;


    private boolean isClickedReturnImButton;

    private static final String TABLE_COMMENTS = "comments";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_COMMENT = "comment";

    private static final String DATABASE_NAME = "scoreboard.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table if not exists " + TABLE_COMMENTS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_COMMENT + " integer not null);";

    private TextButton textButton;
    private Label statusLabel;
    private be.ucl.lfsab1509.gravityrun.tools.Skin skin;

    public ScoreboardState(GameStateManager gsm){
        super(gsm);
        cam.setToOrtho(false, GravityRun.WIDTH/2, GravityRun.HEIGHT/2);

        if(PlayState.scoreList ==null)
            PlayState.scoreList = new ArrayList<Integer>();

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

        isClickedReturnImButton = false;

        Label title = new Label(string.get("my_score"),menuSkin, "title");
        ImageButton returnImageButton= new ImageButton(new TextureRegionDrawable(new TextureRegion(new Texture("back.png"))));

        returnImageButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedReturnImButton = true;
            }
        });


        dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);

        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }


       add();

        Collections.sort(arrayList);

        table.add(returnImageButton).expandX().top().width(50).padTop(40);
        table.add(title).expandX().top().left();
        table.add().expandX().top().left().width(100);
        table.row();


        for(int i=arrayList.size()-1; i>=0 && i < 10;i--){
            TextButton label;
            if(arrayList.size()-i <= 3)
                label = new TextButton(arrayList.size()-i + ".     " +arrayList.get(i).toString(), tableSkin, "subtitle-red");
            else
                label = new TextButton(arrayList.size()-i + ".     " +arrayList.get(i).toString(), tableSkin, "round");

            table.add().top().left().width(90);
            table.add(label).padTop(25*Gdx.graphics.getHeight()/800);
            table.row();
        }

        stage.addActor(table);

        Gdx.input.setInputProcessor(stage);
    }

    public ScoreboardState(){
        super();

        dbHandler = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, DATABASE_CREATE, null);

        dbHandler.setupDatabase();
        try {
            dbHandler.openOrCreateDatabase();
            dbHandler.execSQL(DATABASE_CREATE);
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void handleInput() {
        if(isClickedReturnImButton)
            gsm.set(new OptionState(gsm));
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

    }

    public void add(){
        DatabaseCursor cursor = null;
        int length;

        if(arrayList == null)
            arrayList = new ArrayList<Integer>();

        try {
            cursor = dbHandler.rawQuery("SELECT "+COLUMN_COMMENT+" FROM comments");
            while(cursor.next()){
                arrayList.add(cursor.getInt(0));
            }
        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }

        try {
            Collections.sort(arrayList);

            for(int i = 0; i< PlayState.scoreList.size(); i++){
                length = arrayList.size();
                if(length < 10 && !(arrayList.contains(PlayState.scoreList.get(i)))){
                    dbHandler.execSQL("INSERT INTO comments ('comment') VALUES (" + PlayState.scoreList.get(i)+ ")");
                    arrayList.add(PlayState.scoreList.get(i));
                }
                else if(PlayState.scoreList.get(i) > arrayList.get(0) && !(arrayList.contains(PlayState.scoreList.get(i)))){
                    Integer remove = arrayList.get(0);
                    arrayList.remove(0);
                    dbHandler.execSQL("DELETE FROM "+ TABLE_COMMENTS +" WHERE "+ COLUMN_COMMENT + "="+ remove);
                    dbHandler.execSQL("INSERT INTO comments ('comment') VALUES (" + PlayState.scoreList.get(i)+ ")");
                    arrayList.add(PlayState.scoreList.get(i));

                    Collections.sort(arrayList);
                }
            }


        } catch (SQLiteGdxException e) {
            e.printStackTrace();
        }
    }
}
