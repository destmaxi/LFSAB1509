package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MultiplayerConnectionScreen extends AbstractMenuScreen {

    static boolean isClient = false, ready = false;

    private final List<String> listDeviceName;

    //FIXME le son s'arrete lorsqu'un popup s'ouvre
    MultiplayerConnectionScreen(GravityRun gravityRun) {
        super(gravityRun);

        if (!supportDeviceBluetooth()) {
            spawnErrorDialog(game.i18n.format("error"), game.i18n.format("error_bluetooth_unsupported"));
        }

        enableBluetooth();

        Label title = new Label(game.i18n.get("multiplayer"), game.titleSkin, "title");

        listDeviceName = new List<>(game.tableSkin);
        listDeviceName.setVisible(false);
        listDeviceName.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                connect(listDeviceName.getSelectedIndex());
                listDeviceName.setVisible(false);
            }
        });

        TextButton hostButton = new TextButton(game.i18n.format("create_game"), game.tableSkin, "round");
        hostButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new MultiplayerOptionScreen(game));
            }
        });

        TextButton clientButton = new TextButton(game.i18n.format("join_game"), game.tableSkin, "round");
        clientButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isClient = true;
                discoverDevices();
                listDeviceName.setItems(devicesNames);
                listDeviceName.setVisible(true);
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

    @Override
    public void render(float dt) {
        super.render(dt);
        listDeviceName.setItems(devicesNames);

        handleInput();
    }

    private void handleInput() {
        if (isClient && ready) {
            AbstractMultiPlayScreen abstractMultiPlayScreen = (game.user.getMultiMode() == 0)
                    ? new MultiPlayFirstModeScreen(game)
                    : new MultiPlaySecondModeScreen(game);

            setMultiPlayScreen(abstractMultiPlayScreen);
            screenManager.push(abstractMultiPlayScreen);
        }
    }

    static void setReady(boolean ready) {
        MultiplayerConnectionScreen.ready = ready;
    }

}