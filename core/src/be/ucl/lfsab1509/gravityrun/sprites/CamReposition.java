package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class CamReposition extends Bonus {

    public CamReposition(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble))
            marble.setRepositioning(.5f);
        return super.collides(marble);
    }

    @Override
    public boolean isFinished(Marble marble) {
        return playScreen.getCameraPosition().y <= marble.getCenterPosition().y;
    }

    @Override
    public void update(float dt, Marble marble) {
        if (!marble.isDead() && playScreen.getCameraPosition().y <= marble.getCenterPosition().y)
            marble.setRepositioning(1f);
    }

}
