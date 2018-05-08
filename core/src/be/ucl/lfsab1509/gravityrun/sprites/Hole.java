package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Hole extends Obstacle {

    public Hole(float y, AbstractPlayScreen playScreen, Marble marble, Random random, Texture texture) {
        super(0, y, marble, playScreen, random, texture);

        position.x = this.random.nextInt(playScreen.width - texture.getWidth());
        bounds.set(position.x + marble.getNormalDiameter() / 2, position.y + marble.getNormalDiameter() / 2,
                texture.getWidth() - marble.getNormalDiameter(), texture.getHeight() - marble.getNormalDiameter());
    }

    Hole(float x, float y, Marble marble, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(x, y, marble, playScreen, random, texture);
    }

    @Override
    public boolean collidesMarble() {
        if (overlaps(marble) && marble.getCenterPosition().z == 0) {
            marble.setInHole(true);
            playScreen.getSoundManager().marbleBreak(playScreen.died);
            playScreen.died = true;
            return true;
        } else
            return false;
    }
}
