package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import be.ucl.lfsab1509.gravityrun.GravityRun;

abstract class AbstractMenuScreen extends Screen {

    float containerHeight, containerWidth;
    Stage stage;

    AbstractMenuScreen(GravityRun gravityRun) {
        super(gravityRun);

        containerHeight = height * .9f;
        containerWidth = width * .9f;

        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float dt) {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        soundManager.replayMenu();
    }

    void spawnErrorDialog(String title, String message) {
        Dialog errorDialog; // Override methods public void hide() and public void show() here.
        Label errorLabel;
        TextButton okButton;

        okButton = new TextButton(game.i18n.format("ok"), tableSkin, "round");

        errorLabel = new Label(message, aaronScoreSkin);
        errorLabel.setAlignment(Align.center); // Pour que le texte soit centré à l'intérieur du label, sinon c'est moche
        errorLabel.setWrap(true); // Longs labels

        errorDialog = new Dialog(title, tableSkin) {
            // Override methods public void hide() and public void show() here.
        };
        errorDialog.button(okButton, true).key(Input.Keys.ENTER, true);
        errorDialog.getContentTable().add(errorLabel).width(width * .7f).pad(10); // Sinon la taille est pourrie
        errorDialog.setModal(true); // Sinon on peut cliquer à travers
        errorDialog.setMovable(false); // Ou la déplacer
        errorDialog.show(stage);
    }

}
