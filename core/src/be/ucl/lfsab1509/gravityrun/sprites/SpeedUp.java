package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

public class SpeedUp extends SlowDown {

    public SpeedUp(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return SPEED_UP;
    }

    @Override
    void onCollide(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(1.25f);
        playScreen.nbSpeedUp++;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        if (!marble.isDead() && collideTime >= 5 && marble.decreaseActiveSpeedUps() == 0)
            marble.setSpeedUp(1f);
    }

    public void activateSpeedUp(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(.5f);
    }
}
