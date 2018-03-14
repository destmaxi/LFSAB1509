package be.ucl.lfsab1509.gravityrun.states;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.Skin;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MenuState extends State {

    private boolean isClickedStartGameButton = false, isClickedOptionButton = false;
    private Stage stage;
    private Skin menuSkin, tableSkin;
    private Label hyLabel;


    public MenuState(GameStateManager gsm) {
        super(gsm);

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        float cw = sw * 0.9f;
        float ch = sh * 0.9f;

        menuSkin = new Skin();
        menuSkin.createSkin(62);
        Label title = new Label(string.format("menu"), menuSkin, "title");

        tableSkin = new Skin();
        tableSkin.createSkin(42);
        TextButton optionButton = new TextButton(string.format("option"), tableSkin, "round");
        TextButton startGameButton = new TextButton(string.format("new_game"), tableSkin, "round");
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedStartGameButton = true;
            }
        });
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClickedOptionButton = true;
            }
        });

        hyLabel = new Label(string.format("hello")+GravityRun.pref.getString("username"),tableSkin);
        hyLabel.setWrap(true);
        hyLabel.setWidth(cw);
        hyLabel.setAlignment(Align.center);

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();
        stage = new Stage(new ScreenViewport());


        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((sw - cw) / 2,(sh - ch) / 2);
        tableContainer.top().fillX();

        table.add(title).top();
        table.row();

        table.add(hyLabel).expandX().width(cw).padTop(sh - ch);
        table.row();

        table.add(optionButton).expandX().fillX().padTop(sh - ch);
        table.row();

        table.add(startGameButton).expandX().fillX().padTop(sh - ch);
        table.row();

        tableContainer.setActor(table);
        stage.addActor(tableContainer);

    }

    @Override
    public void handleInput() {
        if (isClickedOptionButton) {
            isClickedOptionButton = false;
            gsm.push(new OptionState(gsm));
        }
        if (isClickedStartGameButton) {
            isClickedStartGameButton = false;
            gsm.push(new PlayState(gsm));
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            GravityRun.pref.put(GravityRun.user.toMap());
            GravityRun.pref.flush();
            Gdx.app.exit();
        }
    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(stage);
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        hyLabel.setText(string.format("hello")+ GravityRun.user.getUsername());
        stage.act();
        stage.draw();
        if (isClickedOptionButton || isClickedStartGameButton)
            gsm.update(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        menuSkin.dispose();
        stage.dispose();
        tableSkin.dispose();
    }

}