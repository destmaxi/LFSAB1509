package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Manage the creation, deletion and playing of sounds and music.
 */

public class SoundManager {

    private final float soundLevel = 0.5f;
    private Music gameMusic, menuMusic;
    private Sound bonus, marbleBreak;

    public SoundManager() {
        this.bonus = Gdx.audio.newSound(Gdx.files.internal("sound/bonus.wav"));
        this.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/6_symphony.mp3"));
        this.marbleBreak = Gdx.audio.newSound(Gdx.files.internal("sound/break.wav"));
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/2_suite_holst.mp3"));
        gameMusic.setLooping(true);
        menuMusic.setLooping(true);
    }

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
