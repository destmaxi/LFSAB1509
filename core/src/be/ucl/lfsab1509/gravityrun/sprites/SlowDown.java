package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class SlowDown extends Bonus {

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
        return collideTime >= 5;
    }

    @Override
    void onCollide(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSlowdowns();
        marble.setSlowDown(.5f);
        playScreen.nbSlowDown++;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        if (!marble.isDead() && collideTime >= 5 && marble.decreaseActiveSlowdowns() == 0)
            marble.setSlowDown(1f);
    }

    public void activateSlowdown(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSlowdowns();
        marble.setSlowDown(1.5f);
    }

}
