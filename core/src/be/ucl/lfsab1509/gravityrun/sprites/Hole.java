package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Hole extends Obstacle {

    public Hole(float y, AbstractPlayScreen playScreen, Marble marble, Random random, Texture texture) {
        this(0, y, playScreen, texture);

        position.x = random.nextInt(playScreen.width - texture.getWidth());
        bounds.set(position.x + marble.getNormalDiameter() / 2, position.y + marble.getNormalDiameter() / 2,
                texture.getWidth() - marble.getNormalDiameter(), texture.getHeight() - marble.getNormalDiameter());
    }

    Hole(float x, float y, AbstractPlayScreen playScreen, Texture texture) {
        super(x, y, playScreen, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble) && marble.isHoleProtected() && !GravityRun.cheat) {
            return false;
        }
        marble.setHoleProtected(false);
        if (overlaps(marble) && !GravityRun.cheat) {
            playScreen.getSoundManager().marbleBreak(marble.isDead());
            marble.setInHole();
            marble.setLives(0);
            marble.setDead();
            playScreen.deadHole = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    boolean overlaps(Marble marble) {
        return super.overlaps(marble) && marble.getCenterPosition().z == 0;
    }

}
