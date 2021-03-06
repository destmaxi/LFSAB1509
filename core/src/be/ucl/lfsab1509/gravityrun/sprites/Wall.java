package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Wall extends Obstacle {

    public Wall(float y, Random random, Marble marble, AbstractPlayScreen playScreen, Texture texture) {
        super(y, playScreen, texture);
        int x = random.nextBoolean()
                ? -random.nextInt(2 * marble.getNormalDiameter())
                : -random.nextInt(2 * marble.getNormalDiameter()) + playScreen.width / 2;

        bounds.x = x;
        position.x = x;
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble) && marble.isInWall()) {
            return false;
        }
        marble.setInWall(false);

        if (!marble.isInvincible() && overlaps(marble) && !GravityRun.cheat) {
            block(marble);
        } else {
            unblock(marble);
        }
        return false;
    }

    private void block(Marble marble) {
        float marbleCenterX = marble.getCenterPosition().x;
        float marbleCenterY = marble.getCenterPosition().y;

        float bottomBound = position.y;
        float leftBound = position.x;
        float rightBound = position.x + texture.getWidth();

        marble.setBlockedOnLeft(marbleCenterX > rightBound);
        marble.setBlockedOnRight(marbleCenterX < leftBound);
        marble.setBlockedOnTop(marbleCenterY < bottomBound);
        marble.setCollidingWall(true);
        if (!marble.isLifeLost()) {
            marble.addMarbleLife(-1);
            marble.setLifeLost(true);
        }
    }

    private void unblock(Marble marble) {
        marble.setBlockedOnLeft(false);
        marble.setBlockedOnRight(false);
        marble.setBlockedOnTop(false);
        marble.setCollidingWall(false);
        marble.setLifeLost(false);
    }

}
