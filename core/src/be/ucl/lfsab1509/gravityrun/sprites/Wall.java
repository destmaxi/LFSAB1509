package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class Wall extends Obstacle {

    public Wall(float y, Random random, Marble marble, AbstractPlayScreen playScreen, Texture texture) {
        super(y, playScreen, random, texture);
        int x = this.random.nextBoolean()
                ? -this.random.nextInt(2 * marble.getNormalDiameter())
                : -this.random.nextInt(2 * marble.getNormalDiameter()) + playScreen.width / 2;

        bounds.x = x;
        position.x = x;
    }

    @Override
    public boolean collides(Marble marble) {
        float marbleCenterX = marble.getCenterPosition().x;
        float marbleCenterY = marble.getCenterPosition().y;

        float bottomBound = position.y;
        float leftBound = position.x;
        float rightBound = position.x + texture.getWidth();

        if (!(overlaps(marble) && marble.isInWall())) {
            marble.setInWall(false);

            if (!marble.isInvincible() && overlaps(marble)) {
                marble.setBlockedOnLeft(marbleCenterX > rightBound);
                marble.setBlockedOnRight(marbleCenterX < leftBound);
                marble.setBlockedOnTop(marbleCenterY < bottomBound);
                marble.setCollideWall(true);
                if (!marble.isLifeLost()) {
                    marble.addMarbleLife(-1);
                    marble.setLifeLost(true);
                }
            } else {
                marble.setBlockedOnLeft(false);
                marble.setBlockedOnRight(false);
                marble.setBlockedOnTop(false);
                marble.setCollideWall(false);
                marble.setLifeLost(false);
            }
        }
        return false;
    }

}
