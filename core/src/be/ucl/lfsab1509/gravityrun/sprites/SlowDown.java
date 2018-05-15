package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class SlowDown extends Bonus {

    private static final float ACTIVE_SLOWDOWN = 0.5f;
    private static final float ACTIVE_ADVERSE_SLOWNDOWN = 1.5f;
    private static final float ACTIVE_SLOWDOWN_TIME = 3f;
    private static final float INACTIVE_SLOWDOWN = 1f;

    private float collideTime;

    public SlowDown(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return SLOW_DOWN;
    }

    @Override
    public boolean isFinished(Marble marble) {
        return collideTime >= ACTIVE_SLOWDOWN_TIME;
    }

    @Override
    void onCollide(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSlowdowns();
        marble.setSlowDown(ACTIVE_SLOWDOWN);
        playScreen.nbSlowDown++;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        if (!marble.isDead() && collideTime >= ACTIVE_SLOWDOWN_TIME && marble.decreaseActiveSlowdowns() == 0)
            marble.setSlowDown(INACTIVE_SLOWDOWN);
    }

    public void activateSlowdown(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSlowdowns();
        marble.setSlowDown(ACTIVE_ADVERSE_SLOWNDOWN);
    }

}
