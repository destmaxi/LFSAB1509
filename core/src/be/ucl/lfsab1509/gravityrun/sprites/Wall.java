package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Wall extends Obstacle {

    public Wall(float y, int standardWidth, int marbleWidth) {
        super(y, "drawable-" + standardWidth + "/wall.png");
        setX(random.nextBoolean()
                ? -random.nextInt(2 * marbleWidth)
                : -random.nextInt(2 * marbleWidth) + GravityRun.WIDTH / 2);
    }

    @Override
    public void collides(Marble marble) {
        float marbleCenterX = marble.getCenterPosition().x;
        float marbleCenterY = marble.getCenterPosition().y;

        float bottomBound = position.y;
        float leftBound = position.x;
        float rightBound = position.x + obstacleTexture.getWidth();

        if (!Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) || !marble.isInWall()) {
            marble.setInWall(false);

            if (!marble.isInvincible() && Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
                marble.setBlockedOnLeft(marbleCenterX > rightBound);
                marble.setBlockedOnRight(marbleCenterX < leftBound);
                marble.setBlockedOnTop(marbleCenterY < bottomBound);
                PlayState.isCollideWall = marbleCenterX > rightBound || marbleCenterX < leftBound || marbleCenterY < bottomBound;
            } else {
                marble.setBlockedOnLeft(false);
                marble.setBlockedOnRight(false);
                marble.setBlockedOnTop(false);
                PlayState.isCollideWall = false;
            }
        }
    }

}
