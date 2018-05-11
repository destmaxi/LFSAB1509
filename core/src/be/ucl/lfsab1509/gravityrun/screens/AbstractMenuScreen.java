package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

abstract class AbstractMenuScreen extends Screen {

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
            System.out.println("clickedBack");
            screenManager.pop();
            return;
        }

        stage.act(dt);
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

    void spawnErrorDialog(String title, String message) {
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
        boolean result(Object object);

    }

    class EmptyButtonsDialog extends Dialog {

        private DialogResultMethod resultMethod;

        EmptyButtonsDialog(String title, Table content, DialogResultMethod resultMethod) {
            super(title, game.tableSkin);
            this.resultMethod = resultMethod;
            this.getContentTable().add(content).width(0.8f * width).pad(width / 40f); // FIXME rendre le padding dépendante de la résolution de l'écran
            this.getButtonTable().add(new Actor()).expandX(); // parce qu'il en faut au moins un.
            this.key(Input.Keys.BACK, false).key(Input.Keys.ESCAPE, false);
            this.setModal(true);
            this.setMovable(false);
        }

        @Override
        public Dialog button(Button button, Object object) {
            getButtonTable().add(button);
            // gros hack : on ajoute des acteurs vides entre les boutons pour que seuls les acteurs vides expand,
            // tous de la même manière, de sorte à avoir des espacements égaux entre boutons.
            getButtonTable().add(new Actor()).expandX();
            setObject(button, object);
            return this;
        }

        @Override
        public void hide(Action action) {
            openDialogs--;
            super.hide(action);
        }

        void requestHide() {
            resultMethod.result(false); // discard return value as we won't use it
            hide();
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
            this.addListener(new EventListener() {
                private final float MARGIN = width / 20; // FIXME la rendre dépendante de la résolution de l'écran

                @Override
                public boolean handle(Event event) {
                    if (!(event instanceof InputEvent))
                        return false;

                    InputEvent inputEvent = (InputEvent) event;
                    if (inputEvent.getType() != InputEvent.Type.touchDown)
                        return false;

                    Vector2 position = inputEvent.toCoordinates(event.getListenerActor(), new Vector2());
                    if (isOutsideOfDialog(position.x, position.y)) {
                        EmptyButtonsDialog.this.requestHide();
                        return true;
                    } else
                        return false;
                }

                private boolean isOutsideOfDialog(float x, float y) {
                    return (x < -MARGIN) || (x > EmptyButtonsDialog.this.getWidth() + MARGIN)
                            || (y < -MARGIN) || (y > EmptyButtonsDialog.this.getHeight() + MARGIN);
                }
            });
            return super.show(stage, action);
        }

    }

    class MessageDialog extends EmptyButtonsDialog {

        MessageDialog(String title, Table content, DialogResultMethod resultMethod) {
            super(title, content, resultMethod);
            TextButton okButton = new TextButton(game.i18n.format("ok"), game.tableSkin, "round");
            this.button(okButton, true).key(Input.Keys.ENTER, true);
        }

    }

    class EditDialog extends MessageDialog {

        EditDialog(String title, Table content, DialogResultMethod resultMethod) {
            super(title, content, resultMethod);
            TextButton cancelButton = new TextButton(game.i18n.format("cancel"), game.tableSkin, "round");
            this.button(cancelButton, false);
        }

    }

    class NoOkEditDialog extends EmptyButtonsDialog {

        NoOkEditDialog(String title, Table content, DialogResultMethod resultMethod) {
            super(title, content, resultMethod);
            TextButton cancelButton = new TextButton(game.i18n.format("cancel"), game.tableSkin, "round");
            this.button(cancelButton, false); // ESCAPE and BACK are also set to false
        }

    }

    public interface ListResultCallback {

        void callback(String selected);

    }

    class ListDialog extends NoOkEditDialog {

        ListDialog(String title, List<String> list, ListResultCallback listResultCallback) {
            super(title, embedTable(list), new DialogResultMethod() {
                @Override
                public boolean result(Object object) {
                    return true;
                }
            });
            list.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    listResultCallback.callback(list.getSelected());
                    ListDialog.this.requestHide();
                }
            });
        }

    }

    private static Table embedTable(List<String> list) {
        Table table = new Table();
        table.add(list);
        return table;
    }

}
