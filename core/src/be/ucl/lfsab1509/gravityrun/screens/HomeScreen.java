package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class HomeScreen extends AbstractMenuScreen {

//    private boolean canStartMultiplayerGame = false;
    private final ImageButton achievementsButton, gpgsButton, leaderboardsButton;
    private Label hyLabel;
    private Texture achievementsImage, gpgsImage, leaderboardsImage;

    public HomeScreen(GravityRun gravityRun) {
        super(gravityRun);

        Label title = new Label(game.i18n.format("menu"), game.titleSkin, "title");

        int standardWidth = calculateStandardWidth(width);
        achievementsImage = new Texture("drawable-" + standardWidth + "/achievements.png");
        gpgsImage = new Texture("drawable-" + standardWidth + "/gpgs.png");
        leaderboardsImage = new Texture("drawable-" + standardWidth + "/leaderboards.png");

        int achievementsHeight = achievementsImage.getHeight();
        int achievementsWidth = achievementsImage.getWidth();
        TextureRegionDrawable achievementsConnected = new TextureRegionDrawable(new TextureRegion(achievementsImage, 0, 0, achievementsWidth, achievementsHeight / 2));
        TextureRegionDrawable achievementsDisconnected = new TextureRegionDrawable(new TextureRegion(achievementsImage, 0, achievementsHeight / 2, achievementsWidth, achievementsHeight / 2));
        achievementsButton = new ImageButton(achievementsDisconnected, achievementsConnected, achievementsConnected);
        achievementsButton.setPosition(0, 0);
        achievementsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gpgs.showAchievements();
            }
        });
        stage.addActor(achievementsButton);

        int gpgsHeight = gpgsImage.getHeight();
        int gpgsWidth = gpgsImage.getWidth();
        TextureRegionDrawable gpgsConnected = new TextureRegionDrawable(new TextureRegion(gpgsImage, 0, 0, gpgsWidth, gpgsHeight / 2));
        TextureRegionDrawable gpgsDisconnected = new TextureRegionDrawable(new TextureRegion(gpgsImage, 0, gpgsHeight / 2, gpgsWidth, gpgsHeight / 2));
        gpgsButton = new ImageButton(gpgsDisconnected, gpgsConnected, gpgsConnected);
        gpgsButton.setPosition(width - gpgsImage.getWidth(), 0);
        gpgsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.connect();
            }
        });
        stage.addActor(gpgsButton);

        int leaderboardsHeight = leaderboardsImage.getHeight();
        int leaderboardsWidth = leaderboardsImage.getWidth();
        TextureRegionDrawable leaderboardsConnected = new TextureRegionDrawable(new TextureRegion(leaderboardsImage, 0, 0, leaderboardsWidth, leaderboardsHeight / 2));
        TextureRegionDrawable leaderboardsDisconnected = new TextureRegionDrawable(new TextureRegion(leaderboardsImage, 0, leaderboardsHeight / 2, leaderboardsWidth, leaderboardsHeight / 2));
        leaderboardsButton = new ImageButton(leaderboardsDisconnected, leaderboardsConnected, leaderboardsConnected);
        leaderboardsButton.setPosition(achievementsButton.getWidth(), 0);
        leaderboardsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.gpgs.showLeaderboards();
            }
        });
        stage.addActor(leaderboardsButton);

        refreshButtons();

        TextButton startGameButton = new TextButton(game.i18n.format("new_game"), game.tableSkin, "round");
        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundManager.replayGame();  // FIXME le placement de cette instruction laisse à désirer. 20 msec de délai
                screenManager.push(new SoloPlayScreen(game));
            }
        });

        TextButton multiplayerButton = new TextButton(game.i18n.format("play_two"), game.tableSkin, "round");
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new MultiplayerConnectionScreen(game));
            }
        });

        TextButton scoreBoardButton = new TextButton(game.i18n.format("my_score"), game.tableSkin, "round");
        scoreBoardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new ScoreboardScreen(game));
                /*game.gpgs.setStartGameCallbask(new IGpgs.StartGameCallback() {
                    @Override
                    public void startGame() {
                        canStartMultiplayerGame = true;
                    }
                });
//                game.gpgs.invitePlayers();
                canStartMultiplayerGame = true;*/
            }
        });


        TextButton optionButton = new TextButton(game.i18n.format("option"), game.tableSkin, "round");
        optionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.push(new OptionScreen(game));
            }
        });

        hyLabel = new Label("", game.tableSkin);
        hyLabel.setAlignment(Align.center);
        hyLabel.setWidth(containerWidth);
        hyLabel.setWrap(true);

        Table table = new Table();
        table.add(title).top();
        table.row();
        table.add(hyLabel).expandX().width(containerWidth).padTop(height - containerHeight);
        table.row();
        table.add(startGameButton).expandX().fillX().padTop(height - containerHeight);
        table.row();
        table.add(multiplayerButton).expandX().fillX().padTop((height - containerHeight) / 2);
        table.row();
        table.add(scoreBoardButton).expandX().fillX().padTop((height - containerHeight) / 2);
        table.row();
        table.add(optionButton).expandX().fillX().padTop((height - containerHeight) / 2);
        table.row();

        initStage(table);

        game.gpgs.signInSilently();
    }

    @Override
    public void dispose() {
        super.dispose();

        achievementsImage.dispose();
        gpgsImage.dispose();
        leaderboardsImage.dispose();
    }

    @Override
    public void hide() {
        game.user.write();
        super.hide();
    }

    @Override
    public void render(float dt) {
        /*if (canStartMultiplayerGame) {
            canStartMultiplayerGame = false;
            startMultiplayerGame();
        }*/

        super.render(dt);

        refreshButtons();
    }

    @Override
    public void show() {
        super.show();
        hyLabel.setText(game.i18n.format("hello", game.user.getUsername()));
    }

    private void refreshButtons() {
        achievementsButton.setChecked(game.gpgs.isSignedIn());
        gpgsButton.setChecked(game.gpgs.isSignedIn());
        leaderboardsButton.setChecked(game.gpgs.isSignedIn());
    }

    /*private void startMultiplayerGame() {
        screenManager.push(new PlayScreen(game));
    }*/

}