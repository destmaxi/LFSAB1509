package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class SlowDown extends Bonus {

    private static int activeSlowDowns = 0;

    private float collideTime;

    public SlowDown(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            activeSlowDowns++;
            collideTime = 0;
            marble.setSlowDown(.5f);
        }
        return super.collides(marble);
    }

    @Override
    public boolean isFinished(Marble marble) {
        return collideTime >= 5;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        if (!marble.isDead() && collideTime >= 5 && --activeSlowDowns == 0)
            marble.setSlowDown(1f);
    }

    public static void resetBonus() {
        activeSlowDowns = 0;
    }

}
