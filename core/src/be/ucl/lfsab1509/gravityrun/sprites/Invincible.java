package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Invincible extends Bonus {

    private static int activeInvincibles = 0;

    private float collideTime;

    public Invincible(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            activeInvincibles++;
            collideTime = 0;
            marble.setInvincible(true);
        }

        return super.collides(marble);
    }

    @Override
    public boolean isFinished(Marble marble) {
        return collideTime >= 3;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        if (!marble.isDead() && collideTime >= 3 && --activeInvincibles == 0) {
            marble.setInvincible(false);
            marble.setInWall(true);
        }
    }

    public static void resetBonus() {
        activeInvincibles = 0;
    }

}
