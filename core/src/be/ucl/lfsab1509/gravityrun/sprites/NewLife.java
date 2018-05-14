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
    void onCollide(Marble marble) {
        marble.addMarbleLife(1);
        playScreen.nbNewLife++;
    }
}
