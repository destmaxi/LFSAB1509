package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private Music gameMusic, menuMusic;
    private Sound bonus, marbleBreak;
    private float soundLevel;
    private float musicLevel;

    public SoundManager() {
        this.bonus = Gdx.audio.newSound(Gdx.files.internal("sounds/bonus.wav"));
        this.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Game.mp3"));
        this.marbleBreak = Gdx.audio.newSound(Gdx.files.internal("sounds/break.wav"));
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/Menu.mp3"));
        gameMusic.setLooping(true);
        menuMusic.setLooping(true);
        musicLevel = 0.8f;
        soundLevel = 0.5f;
	setMusicLevel(musicLevel);
    }

    // called when simply exiting the app with back button
    public void dispose() {
        gameMusic.dispose();
        menuMusic.dispose();

        bonus.dispose();
        marbleBreak.dispose();
    }

    public void gotBonus() {
        bonus.play(soundLevel);
    }

    public void marbleBreak(boolean gameOver) {
        if (!gameOver)
            marbleBreak.play(soundLevel);
    }

    public void setMusicLevel(float musicLevel){
        this.musicLevel = musicLevel;
        gameMusic.setVolume(musicLevel);
        menuMusic.setVolume(musicLevel);
    }

    public void setSoundLevel(float soundLevel){
        this.soundLevel = soundLevel;
    }

    public void playGame() {
        if (menuMusic.isPlaying())
            menuMusic.pause();
        if (!gameMusic.isPlaying())
            gameMusic.play();
    }

    public void playMenu() {
        if (gameMusic.isPlaying())
            gameMusic.pause();
        if (!menuMusic.isPlaying())
            menuMusic.play();
    }

    public void replayGame() {
        if (menuMusic.isPlaying())
            menuMusic.pause();
        if (!gameMusic.isPlaying()) {
            gameMusic.stop();
            gameMusic.play();
        }
    }

    public void replayMenu() {
        if (gameMusic.isPlaying())
            gameMusic.pause();
        if (!menuMusic.isPlaying()) {
            menuMusic.stop();
            menuMusic.play();
        }
    }
}
