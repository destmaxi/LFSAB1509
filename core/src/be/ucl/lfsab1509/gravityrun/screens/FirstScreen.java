package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

public class FirstScreen extends Screen {

    private Label errorLabel;
    private Stage stage;
    private String username = i18n.format("username");

    public FirstScreen(GravityRun gravityRun) {
        super(gravityRun);

        float cw = width * 0.9f;
        float ch = height * 0.9f;

        errorLabel = new Label(i18n.format("error"), aaronScoreSkin, "error");
        errorLabel.setWrap(true);
        errorLabel.setWidth(cw);
        errorLabel.setAlignment(Align.center);
        errorLabel.setVisible(false);

        Label title = new Label(i18n.format("welcome"), titleSkin, "title");

        TextButton startButton = new TextButton(i18n.format("start"), tableSkin, "round");
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if (username.equals(i18n.format("username"))) {
                    errorLabel.setVisible(true);
                } else {
                    initUser();
                    screenManager.set(new MenuScreen(game));
                }
            }
        });
        final TextField usernameField = new TextField(username, tableSkin);
        usernameField.setText(username);
        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                usernameField.selectAll();
            }
        });
        usernameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                username = usernameField.getText();
            }
        });

        Container<Table> tableContainer = new Container<Table>();
        Table table = new Table();

        tableContainer.setSize(cw, ch);
        tableContainer.setPosition((width - cw) / 2, (height - ch) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        table.add(title).top();
        table.row();
        table.add(usernameField).expandX().fillX().padTop(height - ch);
        table.row();
        table.add(startButton).expandX().fillX().padTop(height - ch);
        table.row();
        table.add(errorLabel).expandX().fillX().padTop(height - ch).width(cw);
        table.row();

        stage = new Stage(new ScreenViewport());
        stage.addActor(tableContainer);
    }

    @Override
    public void dispose() {
        // TODO dispose of skins
        stage.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        game.spriteBatch.setProjectionMatrix(camera.combined);
        stage.act();
        stage.draw();
    }

    @Override
    public void update(float dt) {
        if (clickedBack()) {
            disposeSkins();
            game.exit();
        }
    }

    private void disposeSkins() {
        aaronScoreSkin.dispose();
        tableSkin.dispose();
        titleSkin.dispose();
    }

    private void initUser() {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++)
            arrayList.add(0);

        game.user.setUsername(username);
        game.user.setFirstTimeTrue();
        game.user.setBeginner(new ArrayList<Integer>());
        game.user.setInter(new ArrayList<Integer>());
        game.user.setExpert(new ArrayList<Integer>());
        game.user.setIndexSelected(1);
        game.user.setHighScore(arrayList);

        game.pref.put(game.user.toMap());
        game.pref.flush();
    }

}
