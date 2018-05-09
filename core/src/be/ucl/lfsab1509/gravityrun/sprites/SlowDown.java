package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class SlowDown extends Bonus {

    private static int activeSlowDowns = 0;

    private float collideTime;

    public SlowDown(float y, int offset, Marble marble, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, marble, playScreen, random, texture);
    }

    @Override
    public boolean collidesMarble() {
        if (overlaps(marble)) {
            activeSlowDowns++;
            collideTime = 0;
            marble.setSlowDown(.5f);
        }
        return super.collidesMarble();
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 5;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (!playScreen.died && collideTime >= 5 && --activeSlowDowns == 0)
            marble.setSlowDown(1f);
    }

    public static void resetBonus() {
        activeSlowDowns = 0;
    }

    public void activateSlowDown() {
        activeSlowDowns++;
        collideTime = 0;
        marble.setSlowDown(1.5f);
    }

}
