package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Hole extends Obstacle {

    public Hole(float y, AbstractPlayScreen playScreen, Marble marble, Random random, Texture texture) {
        super(0, y, playScreen, random, texture);

        position.x = random.nextInt(playScreen.width - texture.getWidth());
        bounds.set(position.x + marble.getNormalDiameter() / 2, position.y + marble.getNormalDiameter() / 2,
                texture.getWidth() - marble.getNormalDiameter(), texture.getHeight() - marble.getNormalDiameter());
    }

    Hole(float x, float y, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(x, y, playScreen, random, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble) && marble.getCenterPosition().z == 0 && !GravityRun.cheat) {
            marble.setInHole();
            playScreen.getSoundManager().marbleBreak(marble.isDead());
            marble.setLives(0);
            marble.setDead();
            return true;
        } else
            return false;
    }
}
