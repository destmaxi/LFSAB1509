package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class NewLife extends Bonus {

    public NewLife(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return NEW_LIFE;
    }

    @Override
    public boolean isFinished(Marble marble) {
        marble.addMarbleLife(1);
        return true;
    }

    @Override
    void onCollide(Marble marble) {
        playScreen.nbNewLife++;
    }
}
