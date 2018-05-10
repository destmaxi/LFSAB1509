package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class MultiplayerOptionScreen extends AbstractMenuScreen {

    private Integer difficulty;
    private TextButton startGameButton;
    private AbstractMultiPlayScreen abstractMultiPlayScreen;

    MultiplayerOptionScreen(GravityRun gravityRun) {
        super(gravityRun);

        difficulty = game.user.getMulti_IndexSelected();
        Label title = new Label(game.i18n.get("option"), game.titleSkin, "title");

        Label multiplayerModeLabel = new Label(game.i18n.format("mode"), game.tableSkin);
        TextButton multiplayerModeButton = new TextButton(game.user.getMultiModeDescription(), game.tableSkin, "round");
        multiplayerModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popMultiplayerModeDialog(multiplayerModeButton);
            }
        });

        Label livesLabel = new Label(game.i18n.format("lives"), game.tableSkin);
        TextButton livesButton = new TextButton(game.user.getMultiLives().toString(), game.tableSkin, "round");
        livesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popLivesDiaglog(livesButton);
            }
        });

        Label lvlLabel = new Label(game.i18n.format("level_display"), game.tableSkin);
        TextButton lvlButton = new TextButton(game.user.getLevelDescription(), game.tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popLevelDialog(lvlButton);
            }
        });

        startGameButton = new TextButton(game.i18n.format("start"), game.tableSkin, "round");
        startGameButton.setVisible(false);
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                write("[4:" + game.user.getMultiLives() + ":" + difficulty + ":" + game.user.getMultiMode() + "]#");
                abstractMultiPlayScreen = (game.user.getMultiMode() == 0)
                        ? new MultiPlayFirstModeScreen(game)
                        : new MultiPlaySecondModeScreen(game);

                setMultiPlayScreen(abstractMultiPlayScreen);
                screenManager.push(abstractMultiPlayScreen);
            }
        });

        Table table = new Table();
        table.add(title).top().expandY();
        table.row();
        table.add(multiplayerModeLabel).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(multiplayerModeButton).expandX().fillX();
        table.row();
        table.add(livesLabel).expandX().fillX().padTop((height - containerHeight) / 2);
        table.row();
        table.add(livesButton).expandX().fillX();
        table.row();
        table.add(lvlLabel).expandX().fillX().padTop((height - containerHeight) / 2);
        table.row();
        table.add(lvlButton).expandX().fillX();
        table.row();
        table.add(startGameButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        handleInput();
    }

    private void handleInput() {
        if (isConnected())
            startGameButton.setVisible(true);
    }

    private void popLevelDialog(TextButton button) {
        List<String> levelList = new List<>(game.aaronScoreSkin);
        levelList.setAlignment(Align.center);
        levelList.setItems(game.i18n.format("beginner"), game.i18n.format("inter"), game.i18n.format("expert"));
        levelList.setSelectedIndex(game.user.getMulti_IndexSelected() - 1);

        ListDialog levelDialog = new ListDialog(game.i18n.format("select_level"), levelList, new ListResultCallback() {
            @Override
            public void callback(String selected) {
                game.user.setMulti_IndexSelected(levelList.getSelectedIndex() + 1);
                button.setText(game.user.getLevelDescription());
            }
        });
        levelDialog.show(stage);
    }

    private void popLivesDiaglog(TextButton button) {
        List<String> livesList = new List<>(game.aaronScoreSkin);
        livesList.setAlignment(Align.center);
        livesList.setItems("1", "2", "3", "4", "5");
        livesList.setSelectedIndex(game.user.getMultiLives() - 1);

        ListDialog livesDialog = new ListDialog(game.i18n.format("lives"), livesList, new ListResultCallback() {
            @Override
            public void callback(String selected) {
                game.user.setMultiLives(livesList.getSelectedIndex() + 1);
                button.setText(game.user.getMultiLives().toString());
            }
        });
        livesDialog.show(stage);
    }

    private void popMultiplayerModeDialog(TextButton button) {
        List<String> multiplayerModeList = new List<>(game.aaronScoreSkin);
        multiplayerModeList.setItems(game.i18n.format("mode1"), game.i18n.format("mode2"));
        multiplayerModeList.setSelectedIndex(game.user.getMultiMode());
        multiplayerModeList.setAlignment(Align.center);

        ListDialog multiplayerModeDialog = new ListDialog(game.i18n.format("mode"), multiplayerModeList, new ListResultCallback() {
            @Override
            public void callback(String selected) {
                game.user.setMultiMode(multiplayerModeList.getSelectedIndex());
                button.setText(game.user.getMultiModeDescription());
            }
        });
        multiplayerModeDialog.show(stage);
    }

}
