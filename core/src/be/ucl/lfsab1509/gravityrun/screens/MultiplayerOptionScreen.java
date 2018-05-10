package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class MultiplayerOptionScreen extends AbstractMenuScreen {

    private int difficulty;
    private TextButton startGameButton;
    private AbstractMultiPlayScreen abstractMultiPlayScreen;

    MultiplayerOptionScreen(GravityRun gravityRun) {
        super(gravityRun);

        difficulty = game.user.getMulti_IndexSelected();
        Label title = new Label(game.i18n.get("option"), game.titleSkin, "title");

        TextButton multiplayerModeButton = new TextButton(game.i18n.format("mode"), game.tableSkin, "round");
        multiplayerModeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popMultiplayerModeDialog();
            }
        });

        TextButton lvlButton = new TextButton(game.i18n.format("choose_lvl"), game.tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popLevelSelectionDialog();
            }
        });

        TextButton livesButton = new TextButton(game.i18n.format("lives"), game.tableSkin, "round");
        livesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popLivesDiaglog();
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
        table.add(livesButton).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(lvlButton).expandX().fillX().padTop((height - containerHeight) / 2);
        table.row();
        table.add(multiplayerModeButton).expandX().fillX().padTop((height - containerHeight) / 2);
        table.row();
        table.add(startGameButton).expandX().fillX().padTop((height - containerHeight) / 2);

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

    private void popLivesDiaglog() {
        List<Integer> livesList = new List<>(game.aaronScoreSkin);
        livesList.setItems(1, 2, 3, 4, 5);
        livesList.setSelectedIndex(game.user.getMultiLives() - 1);
        livesList.setAlignment(Align.center);
        Table table = new Table();
        table.add(livesList);
        EditDialog livesDialog = new EditDialog(game.i18n.format("lives"), table, new DialogResultMethod() {
            @Override
            public boolean result(Object object) {
                if (object.equals(true))
                    game.user.setMultiLives(livesList.getSelected());
                return true;
            }
        });

        livesList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.user.setMultiLives(livesList.getSelected());
                livesDialog.hide();
            }
        });

        livesDialog.show(stage);
    }

    private void popMultiplayerModeDialog() {
        List<String> multiplayerModeList = new List<>(game.aaronScoreSkin);
        multiplayerModeList.setItems(game.i18n.format("mode1"), game.i18n.format("mode2"));
        multiplayerModeList.setSelectedIndex(game.user.getMultiMode());
        multiplayerModeList.setAlignment(Align.center);
        Table table = new Table();
        table.add(multiplayerModeList);
        EditDialog multiplayerModeDialog = new EditDialog(game.i18n.format("mode"), table, new DialogResultMethod() {
            @Override
            public boolean result(Object object) {
                if (object.equals(true))
                    game.user.setMultiMode(multiplayerModeList.getSelectedIndex());
                return true;
            }
        });

        multiplayerModeList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.user.setMultiMode(multiplayerModeList.getSelectedIndex());
                multiplayerModeDialog.hide();
            }
        });

        multiplayerModeDialog.show(stage);
    }

    private void popLevelSelectionDialog() {
        List<String> levelSelectionList = new List<>(game.aaronScoreSkin);
        levelSelectionList.setItems(game.i18n.format("beginner"), game.i18n.format("inter"), game.i18n.format("expert"));
        levelSelectionList.setSelectedIndex(game.user.getMulti_IndexSelected());
        levelSelectionList.setAlignment(Align.center);
        Table content = new Table();
        content.add(levelSelectionList);
        EditDialog editLevelSelectionDialog = new EditDialog(game.i18n.format("select_level"), content, new DialogResultMethod() {
            @Override
            public boolean result(Object object) {
                if (object.equals(true))
                    validateLevelSelection(levelSelectionList);
                return true;
            }
        });
        levelSelectionList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                validateLevelSelection(levelSelectionList);
                editLevelSelectionDialog.hide();
            }
        });
        editLevelSelectionDialog.show(stage);
    }

    private void validateLevelSelection(List levelList) {
        if (levelList.getSelected().equals(game.i18n.format("beginner")))
            game.user.setMulti_IndexSelected(0);
        else if (levelList.getSelected().equals(game.i18n.format("inter")))
            game.user.setMulti_IndexSelected(1);
        else if (levelList.getSelected().equals(game.i18n.format("expert")))
            game.user.setMulti_IndexSelected(2);
    }

}
