package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Manage the creation, deletion and playing of sounds and music.
 */

public class SoundManager {
    private Music menuMusic;
    private Music gameMusic;
    private Sound marbleBreak;
    private final float breakSoundLevel = 0.5f;

    public SoundManager(){
        this.gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/6_symphony.mp3"));
        this.menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/2_suite_holst.mp3"));
        gameMusic.setLooping(true);
        menuMusic.setLooping(true);
        this.marbleBreak = Gdx.audio.newSound(Gdx.files.internal("sound/break.wav"));
    }

    public void playMenu(){
        if (gameMusic.isPlaying())
            gameMusic.pause();
        if (!menuMusic.isPlaying())
            menuMusic.play();
    }

    public void playGame(){
        if (menuMusic.isPlaying())
            menuMusic.pause();
        if (!gameMusic.isPlaying())
            gameMusic.play();
    }

    public void replayMenu(){
        if (gameMusic.isPlaying())
            gameMusic.pause();
        if (!menuMusic.isPlaying()) {
            menuMusic.stop();
            menuMusic.play();
        }
    }

    public void replayGame(){
        if (menuMusic.isPlaying())
            menuMusic.pause();
        if (!gameMusic.isPlaying()) {
            gameMusic.stop();
            gameMusic.play();
        }
    }
    public void marbleBreak(){
        marbleBreak.play(breakSoundLevel);
    }

    public void dispose(){
        menuMusic.dispose();
        gameMusic.dispose();
        marbleBreak.dispose();
    }
}
