package be.ucl.lfsab1509.gravityrun.states;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

abstract class AbstractMenuState extends State {
    /**
     * Container width
     */
    float containerWidth;
    /**
     * Container height
     */
    float containerHeight;
    Stage stage;

    AbstractMenuState(GameStateManager gsm, SoundManager soundManager) {
        super(gsm, soundManager);
        containerWidth = width * 0.9f;
        containerHeight = height * 0.9f;

        stage = new Stage(new ScreenViewport());
    }

    void spawnErrorDialog(Stage stage, String title, String message) {
        Dialog errorDialog = new Dialog(title, tableSkin) {
            // Override methods public void hide() and public void show() here.
        };
        Label errorLabel = new Label(message, aaronScoreSkin);
        errorLabel.setAlignment(Align.center); // Pour que le texte soit centré à l'intérieur du label, sinon c'est moche
        errorLabel.setWrap(true); // Longs labels
        errorDialog.getContentTable().add(errorLabel).width(width * 0.7f).pad(10); // Sinon la taille est pourrie
        TextButton okButton = new TextButton(GravityRun.i18n.format("ok"), tableSkin, "round");
        errorDialog.button(okButton, true).key(Input.Keys.ENTER, true);
        errorDialog.setModal(true); // Sinon on peut cliquer à travers
        errorDialog.setMovable(false); // Ou la déplacer
        errorDialog.show(stage);
    }
}
