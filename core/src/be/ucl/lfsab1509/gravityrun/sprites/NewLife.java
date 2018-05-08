package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

public class NewLife extends Bonus {

    public NewLife(float y, int offset, AbstractPlayScreen playScreen, Marble marble, Random random, Texture texture) {
        super(y, offset, marble, playScreen, random, texture);
    }

    @Override
    public boolean isFinished() {
        marble.addMarbleLife(1);
        return true;
    }

}
