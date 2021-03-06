package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class ScoreBonus extends Bonus {

    public ScoreBonus(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return SCORE_BONUS;
    }

    @Override
    void onCollide(Marble marble) {
        marble.addScoreBonus();
        playScreen.nbScoreBonus++;
    }

}
