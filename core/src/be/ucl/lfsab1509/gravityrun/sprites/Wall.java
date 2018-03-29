package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Wall extends Obstacle {

    private boolean wait;

    public Wall(float y, int sw, int marbleWidth) {
        super(y, "drawable-" + sw + "/wall.png");
        wait = false;
        setX(rand.nextBoolean()
                ? -rand.nextInt(2 * marbleWidth)
                : -rand.nextInt(2 * marbleWidth) + GravityRun.WIDTH / 2);
    }

    @Override
    public void collides(Marble marble) {
        float marbleCx = marble.getCenterPosition().x;
        float marbleCy = marble.getCenterPosition().y;

        float rectY0 = position.y;
        float rectX0 = position.x;
        float rectX1 = position.x + obstacleTexture.getWidth();

        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) && Invincible.inWall)
            wait = true;
        else {
            wait = false;
            Invincible.inWall = false;
        }

        if (!wait && !Invincible.isInvincible && Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {

            if (marbleCy < rectY0) {
                marble.setBlockedOnTop(true);
                PlayState.isCollideWall = true;
            } else
                marble.setBlockedOnTop(false);

            if (marbleCx > rectX1) {
                marble.setBlockedOnLeft(true);
                PlayState.isCollideWall = true;
            } else
                marble.setBlockedOnLeft(false);

            if (marbleCx < rectX0) {
                marble.setBlockedOnRight(true);
                PlayState.isCollideWall = true;
            } else
                marble.setBlockedOnRight(false);

        } else if (!wait) {
            PlayState.isCollideWall = false;
            marble.setBlockedOnTop(false);
            marble.setBlockedOnLeft(false);
            marble.setBlockedOnRight(false);
        }

    }

}
