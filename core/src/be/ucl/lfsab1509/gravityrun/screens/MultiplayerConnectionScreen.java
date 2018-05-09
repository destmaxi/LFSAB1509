package be.ucl.lfsab1509.gravityrun.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class MultiplayerConnectionScreen extends AbstractMenuScreen {
    static boolean isClient = false;
    static boolean ready = false;
    private final List<String> listDeviceName;

    MultiplayerConnectionScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.get("multiplayer"), game.titleSkin, "title");

        if (!supportDeviceBluetooth())
            screenManager.pop();

        enableBluetooth();

        listDeviceName = new List<>(game.tableSkin);

        TextButton hostButton = new TextButton("Create new Game", game.tableSkin, "round");
        TextButton clientButton = new TextButton("Join Game", game.tableSkin, "round");

        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startHost();
                screenManager.push(new MultiplayerOptionScreen(game));
            }
        });
        clientButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClient = true;
                discoverDevices();
                listDeviceName.setItems(devicesNames);
                listDeviceName.setVisible(true);
            }
        });

        listDeviceName.setVisible(false);
        listDeviceName.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connect(listDeviceName.getSelectedIndex());
                listDeviceName.setVisible(false);
            }
        });

        Table table = new Table();
        table.add(title).top().expandY();
        table.row();
        table.add(hostButton).expandX().fillX().padTop(height - containerHeight).maxWidth(containerWidth);
        table.row();
        table.add(clientButton).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(listDeviceName).expandX().fillX().maxWidth(containerWidth);

        initStage(table);
    }

    private void handleInput() {
        if (isClient && ready) {
            MultiPlayScreen multiPlayScreen = new MultiPlayScreen(game);
            setMultiPlayScreen(multiPlayScreen);
            screenManager.set(multiPlayScreen);
        }

    }

    @Override
    public void render(float dt) {
        super.render(dt);
        listDeviceName.setItems(devicesNames);

        handleInput();
    }
}