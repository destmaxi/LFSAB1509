package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class LeftWall extends Obstacle {
    private boolean wait;

    public LeftWall(float y, boolean first, int marbleWidth, int sw) {
        super();

        wait = false;
        obstacleTexture = new Texture("drawable-" + sw + "/wall.png");
        position = first
                ? new Vector2(0, y)
                : new Vector2(rand.nextInt(GravityRun.WIDTH - 3 * marbleWidth) - obstacleTexture.getWidth(), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public void collides(Marble marble) {
        float marbleX0 = marble.getPosition().x;
        float marbleY0 = marble.getPosition().y;
        float marbleCx = marbleX0 + marble.getDiameter()/2;
        float marbleCy = marbleY0 + (marble.getDiameter() / MarbleAnimation.FRAME_COUNT)/2;

        float rectY0 = position.y;
        float rectX1 = position.x + obstacleTexture.getWidth();

        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) && Invincible.inWall) {
            System.out.println("after invicible timed out in a wall");

            wait = true;
          //  PlayState.isCollideWall = false;
          //  marble.setBlockedOnTop(false);
          //  marble.setBlockedOnLeft(false);
        }
        else if (wait) {
            System.out.println("went out wall, now no more invincible");

            wait = false;
            Invincible.inWall = false;
           // PlayState.isCollideWall = false;
           // marble.setBlockedOnTop(false);
           // marble.setBlockedOnLeft(false);
        }

        if (!wait && !Invincible.isInvicible && !Invincible.inWall && Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            System.out.println("no more in wall and invincible");

            if (marbleCy < rectY0) {
                marble.setBlockedOnTop(true);
                PlayState.isCollideWall = true;
            }
            else
                marble.setBlockedOnTop(false);

            if (marbleCx > rectX1) {
                marble.setBlockedOnLeft(true);
                PlayState.isCollideWall = true;
            }
            else
                marble.setBlockedOnLeft(false);
        }
        else if (!wait) {
            PlayState.isCollideWall = false;
            marble.setBlockedOnTop(false);
            marble.setBlockedOnLeft(false);
        }

    }

}