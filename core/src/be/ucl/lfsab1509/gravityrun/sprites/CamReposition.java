package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class CamReposition extends Bonus {

    public CamReposition(float y, int offset, Marble marble, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, marble, playScreen, random, texture);
    }

    @Override
    public boolean collidesMarble() {
        if (overlaps(marble))
            marble.setRepositioning(.5f);
        return super.collidesMarble();
    }

    @Override
    public boolean isFinished() {
        return playScreen.getCameraPosition().y <= marble.getCenterPosition().y;
    }

    @Override
    public void update(float dt) {
        if (!playScreen.died && playScreen.getCameraPosition().y <= marble.getCenterPosition().y)
            marble.setRepositioning(1f);
    }

}
