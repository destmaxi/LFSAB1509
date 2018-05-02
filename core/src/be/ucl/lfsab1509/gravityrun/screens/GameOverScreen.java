package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class GameOverScreen extends AbstractMenuScreen {

    GameOverScreen(GravityRun gravityRun, int finalScore) {
        super(gravityRun);

        int previousHighScore = game.user.getHighScore();
        boolean isNewHighScore = game.user.addScore(finalScore);
        game.user.write();

        TextButton menuButton = new TextButton(game.i18n.format("menu"), game.tableSkin, "round");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.pop();
            }
        });
        TextButton replayButton = new TextButton(game.i18n.format("replay"), game.tableSkin, "round");
        replayButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                screenManager.set(new PlayScreen(game));
            }
        });

        Label title = new Label(game.i18n.format("game_over"), game.titleSkin, "title");

        Table table = new Table();
        table.add(title).top();
        table.row();

        if (isNewHighScore) {
            Label newHighScoreLabel = new Label(game.i18n.format("new_high_score", finalScore), game.aaronScoreSkin);
            newHighScoreLabel.setAlignment(Align.center);
            newHighScoreLabel.setWrap(true);
            Label previousHighScoreLabel = new Label(game.i18n.format("previous_high_score", previousHighScore), game.aaronScoreSkin);
            previousHighScoreLabel.setAlignment(Align.center);
            previousHighScoreLabel.setWrap(true);
            table.add(newHighScoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
            table.add(previousHighScoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
        } else {
            Label highScoreLabel = new Label(game.i18n.format("high_score", previousHighScore), game.aaronScoreSkin);
            highScoreLabel.setAlignment(Align.center);
            highScoreLabel.setWrap(true);
            Label scoreLabel = new Label(game.i18n.format("final_score", finalScore), game.aaronScoreSkin);
            scoreLabel.setAlignment(Align.center);
            scoreLabel.setWrap(true);
            table.add(scoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
            table.add(highScoreLabel).padTop(height - containerHeight).width(containerWidth).expandX();
            table.row();
        }

        table.add(replayButton).expandX().fillX().padTop((height - containerHeight) * 2);
        table.row();
        table.add(menuButton).expandX().fillX().padTop(height - containerHeight);

        initStage(table);
    }

}
