package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class ScoreBonus extends Bonus {

    public ScoreBonus(float y, int offset, Marble marble, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, marble, playScreen, random, texture);
    }

    @Override
    public boolean isFinished() {
        playScreen.addScoreBonus(100);
        return true;
    }

}
