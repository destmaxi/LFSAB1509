package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class OptionScreen extends AbstractMenuScreen {

    private String username; // Le dernier username valide, tel que retourné par le user.

    OptionScreen(GravityRun gravityRun) {
        super(gravityRun);

        username = game.user.getUsername();

        Label title = new Label(game.i18n.get("option"), game.titleSkin, "title");

        Label usernameLabel = new Label(game.i18n.format("username_display"), game.tableSkin);
        TextButton usernameButton = new TextButton(game.user.getUsername(), game.tableSkin, "round");
        usernameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popUsernameDialog(usernameButton);
            }
        });

        Label lvlLabel = new Label(game.i18n.format("level_display"), game.tableSkin);
        TextButton lvlButton = new TextButton(game.user.getLevelDescription(), game.tableSkin, "round");
        lvlButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popLevelSelectionDialog(lvlButton);
            }
        });

        TextButton multiplayerButton = new TextButton(game.i18n.format("multiplayer"), game.tableSkin, "round");
        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                screenManager.push(new MultiplayerConnectionScreen(game));
            }
        });

        //Label musicLabel = new Label("Music", game.tableSkin, "round");//TODO add internationalisation

        Slider musicSlider = new Slider(0f, 1f, 0.05f, false, game.tableSkin);
        musicSlider.setValue(1f);
        musicSlider.addListener(new MusicListener(soundManager, musicSlider));

        //Label soundLabel = new Label("Sound effects", game.tableSkin, "round");//TODO add internationalisation

        Slider soundSlider = new Slider(0f, 1f, 0.05f, false, game.tableSkin);
        soundSlider.setValue(0.5f);
        soundSlider.addListener(new SoundListener(soundManager, soundSlider));

        Table table = new Table();
        table.add(title).expandX();
        table.row();
        table.add(usernameLabel).expandX().fillX().padTop(height - containerHeight).maxWidth(containerWidth);
        table.row();
        table.add(usernameButton).expandX().fillX().maxWidth(containerWidth);
        table.row();
        table.add(lvlLabel).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(lvlButton).expandX().fillX().maxWidth(containerWidth);
        table.row();
        table.add(multiplayerButton).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(musicSlider).expandX().fillX().maxWidth(containerWidth);
        table.row();
        table.add(soundSlider).expandX().fillX().maxWidth(containerWidth);

        initStage(table);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void hide() {
        game.user.write(); // don't trust it, it may not be called when the activity is actually destroyed
        super.hide();
    }

    private void popLevelSelectionDialog(TextButton levelButton) {
        List<String> levelSelectionList = new List<>(game.tableSkin);
        levelSelectionList.setItems(game.i18n.format("beginner"), game.i18n.format("inter"), game.i18n.format("expert"));
        levelSelectionList.setSelectedIndex(game.user.getIndexSelected());
        levelSelectionList.setAlignment(Align.center);
        // FIXME il n'y a pas de manière simple d'agrandir la taille des items dans une List... Peut-être passer à des boutons ? Merci libGDX.
        ListDialog editLevelSelectionDialog = new ListDialog(game.i18n.format("select_level"), levelSelectionList, new ListResultCallback() {
            @Override
            public void callback(String selected) {
                validateLevelSelection(selected);
                levelButton.setText(game.user.getLevelDescription());
            }
        });
        editLevelSelectionDialog.show(stage);
    }

    private void popUsernameDialog(TextButton usernameButton) {
        Gdx.input.setOnscreenKeyboardVisible(false);
        TextField usernameField = new TextField(username, game.tableSkin);
        usernameField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                usernameField.selectAll();
            }
        });
        Table content = new Table();
        content.add(usernameField).width(0.7f * width).pad(10);
        EditDialog editUsernameDialog = new EditDialog(game.i18n.format("enter_username"), content, new DialogResultMethod() {
            @Override
            public boolean result(Object object) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                if (object.equals(true))
                    return validateUserName(usernameField, usernameButton);
                else {
                    return true;
                }
            }
        });
        editUsernameDialog.show(stage);
    }

    private void validateLevelSelection(String selected) {
        if (selected.equals(game.i18n.format("beginner")))
            game.user.setIndexSelected(0);
        else if (selected.equals(game.i18n.format("inter")))
            game.user.setIndexSelected(1);
        else if (selected.equals(game.i18n.format("expert")))
            game.user.setIndexSelected(2);
        game.user.write();
    }

    private boolean validateUserName(TextField usernameField, TextButton usernameButton) {
        String newUsername = usernameField.getText();
        if (game.user.setUsername(newUsername)) {
            game.user.write();
            username = newUsername; // don't forget me too
            usernameButton.setText(username);
            return true;
        } else {
            spawnErrorDialog(game.i18n.format("error_username_default"), User.getUsernameError(newUsername));
            usernameField.setText(username);
            usernameButton.setText(username);
            return false;
        }
    }

    private class MusicListener extends DragListener{
        private SoundManager soundManager;
        private Slider musicSlider;

        public MusicListener(SoundManager soundManager, Slider musicSlider){
            this.soundManager = soundManager;
            this.musicSlider = musicSlider;
        }
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
            soundManager.setMusicLevel(musicSlider.getValue());
            return true;
        }
        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer){
            soundManager.setMusicLevel(musicSlider.getValue());
        }
        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button){
            soundManager.setMusicLevel(musicSlider.getValue());
        }

    }

    private class SoundListener extends DragListener{
        private SoundManager soundManager;
        private Slider soundSlider;

        public SoundListener(SoundManager soundManager, Slider soundSlider){
            this.soundManager = soundManager;
            this.soundSlider = soundSlider;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
            //soundManager.setSoundLevel(soundSlider.getValue());
            //soundManager.gotBonus();
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button){
            soundManager.setSoundLevel(soundSlider.getValue());
            soundManager.gotBonus();
        }
    }


}
