package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import be.ucl.lfsab1509.gravityrun.tools.User;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class OptionScreen extends AbstractMenuScreen {

    private String username; // Le dernier username valide, tel que retourné par le user.

    OptionScreen(GravityRun gravityRun) {
        super(gravityRun);

        username = game.user.getUsername();

        Label title = new Label(game.i18n.get("option"), game.titleSkin, "title");

        Label usernameLabel = new Label(game.i18n.format("username"), game.tableSkin);
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

        /*TextButton orientationProviderButton = new TextButton("Orientation Provider", game.tableSkin, "round");
        orientationProviderButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popOrientationProviderDialog();
            }
        });*/
        /*TextButton cheatButton = new TextButton("Cheat: " + GravityRun.cheat, game.tableSkin, "round");
        cheatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GravityRun.cheat = !GravityRun.cheat;
                cheatButton.setText("Cheat: " + GravityRun.cheat);
            }
        });*/

        Label musicLabel = new Label(game.i18n.format("music_level"), game.tableSkin);
        Slider musicSlider = new Slider(0f, 1f, .05f, false, game.tableSkin);
        musicSlider.setValue(game.user.getMusicLevel());
        musicSlider.addListener(new MusicListener(soundManager, musicSlider, game));

        Label soundLabel = new Label(game.i18n.format("sound_level"), game.tableSkin);
        Slider soundSlider = new Slider(0f, 1f, .05f, false, game.tableSkin);
        soundSlider.setValue(game.user.getSoundLevel());
        soundSlider.addListener(new SoundListener(soundManager, soundSlider, game));

        Table table = new Table();
        table.add(title).expandX();
        table.row();
        table.add(usernameLabel).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(usernameButton).expandX().fillX().maxWidth(containerWidth);
        table.row();
        table.add(lvlLabel).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(lvlButton).expandX().fillX().maxWidth(containerWidth);
        table.row();
        //table.add(orientationProviderButton);
        //table.row();
        //table.add(cheatButton);
        //table.row();
        table.add(musicLabel).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(musicSlider).expandX().fillX().maxWidth(containerWidth);
        table.row();
        table.add(soundLabel).expandX().fillX().padTop((height - containerHeight) / 2).maxWidth(containerWidth);
        table.row();
        table.add(soundSlider).expandX().fillX().maxWidth(containerWidth);

        initStage(table);
    }

    @Override
    public void hide() {
        game.user.write(); // don't trust it, it may not be called when the activity is actually destroyed
        super.hide();
    }

    private void popLevelSelectionDialog(TextButton levelButton) {
        List<String> levelList = new List<>(game.tableSkin);
        levelList.setItems(game.i18n.format("beginner"), game.i18n.format("inter"), game.i18n.format("expert"));
        levelList.setSelectedIndex(game.user.getIndexSelected());
        levelList.setAlignment(Align.center);
        // FIXME il n'y a pas de manière simple d'agrandir la taille des items dans une List... Peut-être passer à des boutons ? Merci libGDX.
        ListDialog levelDialog = new ListDialog(game.i18n.format("select_level"), levelList, new ListResultCallback() {
            @Override
            public void callback(String selected) {
                game.user.setIndexSelected(levelList.getSelectedIndex());
                levelButton.setText(game.user.getLevelDescription());
            }
        });
        levelDialog.show(stage);
    }

    /*private void popOrientationProviderDialog() {
        java.util.List<String> list = game.sensorHelper.getOrientationProviders();
        List<String> list1 = new List<>(game.tableSkin);
        list1.setItems(new Array<>(list.toArray(new String[0])));
        list1.setSelectedIndex(game.sensorHelper.getOrientationProvider());
        ListDialog dialog = new ListDialog("Orientation Provider select", list1, new ListResultCallback() {
            @Override
            public void callback(String selected) {
                game.sensorHelper.setOrientationProvider(list1.getSelectedIndex());
            }
        });
        dialog.show(stage);
    }*/

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
                else
                    return true;
            }
        });
        editUsernameDialog.show(stage);
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
            usernameButton.setText(username);
            usernameField.setText(username);
            return false;
        }
    }

    private class MusicListener extends DragListener {

        private GravityRun game;
        private Slider musicSlider;
        private SoundManager soundManager;

        MusicListener(SoundManager soundManager, Slider musicSlider, GravityRun game) {
            this.game = game;
            this.musicSlider = musicSlider;
            this.soundManager = soundManager;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            setVolume();
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            setVolume();
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            setVolume();
        }

        private void setVolume() {
            game.user.setMusicLevel(musicSlider.getValue());
            soundManager.setMusicLevel(musicSlider.getValue());
        }

    }

    private class SoundListener extends DragListener {

        private GravityRun game;
        private Slider soundSlider;
        private SoundManager soundManager;

        SoundListener(SoundManager soundManager, Slider soundSlider, GravityRun game) {
            this.soundManager = soundManager;
            this.soundSlider = soundSlider;
            this.game = game;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            game.user.setSoundLevel(soundSlider.getValue());
            soundManager.setSoundLevel(soundSlider.getValue());
            soundManager.gotBonus();
        }

    }

}
