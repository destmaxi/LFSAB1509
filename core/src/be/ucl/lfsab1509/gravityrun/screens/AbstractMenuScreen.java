package be.ucl.lfsab1509.gravityrun.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public abstract class AbstractMenuScreen extends Screen {

    boolean dialog = false;
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

    void initStage(Table table) {
        Container<Table> tableContainer = new Container<Table>();
        tableContainer.setSize(containerWidth, containerHeight);
        tableContainer.setPosition((width - containerWidth) / 2, (height - containerHeight) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        stage.addActor(tableContainer);
    }

    public void spawnErrorDialog(String title, String message) {
        Dialog errorDialog;
        Label errorLabel;
        TextButton okButton;

        okButton = new TextButton(game.i18n.format("ok"), tableSkin, "round");

        errorLabel = new Label(message, aaronScoreSkin);
        errorLabel.setAlignment(Align.center); // Pour que le texte soit centré à l'intérieur du label, sinon c'est moche
        errorLabel.setWrap(true); // Longs labels

        errorDialog = new Dialog(title, tableSkin) {
            @Override
            public void hide(Action action) {
                dialog = false;
                super.hide(action);
            }

            @Override
            public Dialog show(Stage stage, Action action) {
                dialog = true;
                return super.show(stage, action);
            }
        };
        errorDialog.button(okButton, true).key(Input.Keys.ENTER, true);
        errorDialog.getContentTable().add(errorLabel).width(width * .7f).pad(10); // Sinon la taille est pourrie
        errorDialog.key(Input.Keys.BACK, null);
        errorDialog.key(Input.Keys.ESCAPE, null);
        errorDialog.setModal(true); // Sinon on peut cliquer à travers
        errorDialog.setMovable(false); // Ou la déplacer
        errorDialog.show(stage);
    }

}
