package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class RightWall extends Obstacle {

    public RightWall(float y, boolean first, int marbleWidth, int sw) {
        super();

        obstacleTexture = new Texture("drawable-" + sw + "/wall.png");
        position = first
                ? new Vector2(GravityRun.WIDTH - obstacleTexture.getWidth(), y)
                : new Vector2(rand.nextInt(GravityRun.WIDTH - 3 * marbleWidth) + 3 * marbleWidth, y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public void collides(Marble marble) {

        float marbleX0 = marble.getPosition().x;
        float marbleY0 = marble.getPosition().y;
        float marbleCx = marbleX0 + marble.getDiameter() / 2;
        float marbleCy = marbleY0 + (marble.getDiameter() / MarbleAnimation.FRAME_COUNT)/2;

        float rectX0 = position.x;
        float rectY0 = position.y;

        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            if (marbleCy < rectY0) {
                marble.setBlockedOnTop(true);
                PlayState.isCollideWall = true;
            }
            else {
                marble.setBlockedOnTop(false);
            }

            if (marbleCx < rectX0) {
                marble.setBlockedOnRight(true);
                PlayState.isCollideWall = true;
            }
            else {
                marble.setBlockedOnRight(false);
            }
        }
        else {
            PlayState.isCollideWall = false;
            marble.setBlockedOnTop(false);
            marble.setBlockedOnRight(false);
        }
    }

}
