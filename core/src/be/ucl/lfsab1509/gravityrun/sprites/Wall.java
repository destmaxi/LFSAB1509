package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Wall extends Obstacle {

    public Wall(float y, int sw, int marbleWidth) {
        super(y, "drawable-" + sw + "/wall.png");
        setX(rand.nextBoolean()
                ? -rand.nextInt(2 * marbleWidth)
                : -rand.nextInt(2 * marbleWidth) + GravityRun.WIDTH / 2);
    }

    @Override
    public void collides(Marble marble) {
        float marbleCx = marble.getCenterPosition().x;
        float marbleCy = marble.getCenterPosition().y;

        float bottomBound = position.y;
        float leftBound = position.x;
        float rightBound = position.x + obstacleTexture.getWidth();

        if (!Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) || !marble.isInWall()) {
            marble.setInWall(false);

            if (!marble.isInvincible() && Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
                marble.setBlockedOnLeft(marbleCx > rightBound);
                marble.setBlockedOnRight(marbleCx < leftBound);
                marble.setBlockedOnTop(marbleCy < bottomBound);
                PlayState.isCollideWall = marbleCx > rightBound || marbleCx < leftBound || marbleCy < bottomBound;
            } else {
                marble.setBlockedOnLeft(false);
                marble.setBlockedOnRight(false);
                marble.setBlockedOnTop(false);
                PlayState.isCollideWall = false;
            }
        }
    }

}
