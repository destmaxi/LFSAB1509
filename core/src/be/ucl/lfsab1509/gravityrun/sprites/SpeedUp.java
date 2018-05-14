package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

public class SpeedUp extends Bonus {

    private float collideTime;

    public SpeedUp(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return SPEED_UP;
    }

    @Override
    public boolean isFinished(Marble marble) {
        return collideTime >= 3;
    }

    @Override
    void onCollide(Marble marble) {
        collideTime = 0;
        marble.setInvincible(true);
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(2f);
        playScreen.nbSpeedUp++;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        marble.getCenterPosition().z = Marble.JUMP_HEIGHT;

        if (!marble.isDead() && collideTime >= 3 && marble.decreaseActiveSpeedUps() == 0) {
            marble.setInvincible(false);
            marble.setSpeedUp(1f);
            marble.setInWall(true);
        }
    }

    public void activateSpeedUp(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(.5f);
    }
}
