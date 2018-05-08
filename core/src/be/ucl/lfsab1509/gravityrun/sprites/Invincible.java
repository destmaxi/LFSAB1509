package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Invincible extends Bonus {

    private static int activeInvincibles = 0;

    private float collideTime;

    public Invincible(float y, int offset, Marble marble, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, marble, playScreen, random, texture);
    }

    @Override
    public boolean collidesMarble() {
        if (overlaps(marble)) {
            activeInvincibles++;
            collideTime = 0;
            marble.setInvincible(true);
        }

        return super.collidesMarble();
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 3;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (!playScreen.died && collideTime >= 3 && --activeInvincibles == 0) {
            marble.setInvincible(false);
            marble.setInWall(true);
        }
    }

    public static void resetBonus() {
        activeInvincibles = 0;
    }

}
