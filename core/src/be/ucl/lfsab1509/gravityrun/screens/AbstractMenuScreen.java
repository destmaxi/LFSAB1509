package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class AbstractMenuScreen extends Screen {

    float containerHeight, containerWidth;
    private int openDialogs = 0;
    Stage stage;

    AbstractMenuScreen(GravityRun gravityRun) {
        super(gravityRun);

        containerHeight = height * .9f;
        containerWidth = width * .9f;

        stage = new Stage(new ScreenViewport(), game.spriteBatch);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render(float dt) {
        if (clickedBack() && openDialogs == 0) {
            screenManager.pop();
            return;
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        soundManager.replayMenu();
    }

    void initStage(Table table) {
        Container<Table> tableContainer = new Container<>();
        tableContainer.setSize(containerWidth, containerHeight);
        tableContainer.setPosition((width - containerWidth) / 2, (height - containerHeight) / 2);
        tableContainer.top().fillX();
        tableContainer.setActor(table);

        stage.addActor(tableContainer);
    }

    public void spawnErrorDialog(String title, String message) {
        Label errorLabel = new Label(message, game.aaronScoreSkin);
        errorLabel.setAlignment(Align.center); // Pour que le texte soit centré à l'intérieur du label, sinon c'est moche
        errorLabel.setWrap(true); // Longs labels
        Table content = new Table();
        content.add(errorLabel).width(width * .7f).pad(10);
        Dialog errorDialog = new MessageDialog(title, content, new DialogResultMethod() {
            @Override
            public boolean result(Object object) {
                // do nothing
                return true;
            }
        });
        errorDialog.show(stage);
    }

    public interface DialogResultMethod {
        /**
         * Action à effectuer lors de la sortie de la boîte de dialogue (appui sur un bouton ou sur une touche).
         *
         * @param object la valeur de sortie associée à l'évènement. Peut être {@code null} si aucune valeur n'a été associée.
         * @return true si la boîte de dialogue peut se fermer, false sinon.
         */
        boolean result(Object object);
    }

    /**
     * Fenêtre de dialogue comportant uniquement un content et aucun bouton pour en sortir.
     * Les seuls moyens d'en sortir sont le bouton BACK et la touche ESCAPE.
     * Toute autre méthode doit être ajoutée explicitement.
     */
    class EmptyButtonsDialog extends Dialog {
        private DialogResultMethod resultMethod;

        EmptyButtonsDialog(String title, Table content, DialogResultMethod resultMethod) {
            super(title, game.tableSkin);
            this.resultMethod = resultMethod;
            this.getContentTable().add(content);
            this.key(Input.Keys.BACK, false).key(Input.Keys.ESCAPE, false);
            this.setModal(true);
            this.setMovable(false);
        }

        @Override
        public void hide(Action action) {
            openDialogs--;
            super.hide(action);
        }

        @Override
        protected void result(Object object) {
            if (!resultMethod.result(object)) {
                cancel();
            }
        }

        @Override
        public Dialog show(Stage stage, Action action) {
            openDialogs++;
            return super.show(stage, action);
        }
    }

    /**
     * Fenêtre de dialogue comportant, en plus de {@link EmptyButtonsDialog}, un bouton "Ok"
     * et la touche "ENTER" permettant de confirmer les changements au lieu de les ignorer ;
     * ils sont tous les deux associés à la valeur {@code true}.
     */
    class MessageDialog extends EmptyButtonsDialog {
        MessageDialog(String title, Table content, DialogResultMethod resultMethod) {
            super(title, content, resultMethod);
            TextButton okButton = new TextButton(game.i18n.format("ok"), game.aaronScoreSkin, "round");
            this.button(okButton, true).key(Input.Keys.ENTER, true);
        }
    }

    /**
     * Fenêtre de dialogue comportant, en plus de {@link EmptyButtonsDialog}, un bouton "Annuler"
     * permettant de quitter la fenêtre dans sauver les changements, et associé à la valeur {@code false}.
     */
    class EditDialog extends MessageDialog {
        EditDialog(String title, Table content, DialogResultMethod resultMethod) {
            super(title, content, resultMethod);
            TextButton cancelButton = new TextButton(game.i18n.format("cancel"), game.aaronScoreSkin, "round");
            this.button(cancelButton, false);
        }
    }

}
