package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class LeftWall extends Obstacle {

    public LeftWall(float y, boolean first, int marbleWidth, int sw) {
        super();

        obstacleTexture = new Texture("drawable-" + sw + "/wall.png");
        position = first
                ? new Vector2(0, y)
                : new Vector2(rand.nextInt(GravityRun.WIDTH - 3 * marbleWidth) - obstacleTexture.getWidth(), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        float marbleX0 = marble.getPosition().x;
        float marbleY0 = marble.getPosition().y;
        float marbleX1 = marble.getPosition().x + marble.getDiameter();
        float marbleY1 = marble.getPosition().y + marble.getDiameter() / MarbleAnimation.FRAME_COUNT;

        float rectX0 = position.x;
        float rectY0 = position.y;
        float rectX1 = position.x + obstacleTexture.getWidth();
        float rectY1 = position.y + obstacleTexture.getHeight();

        //marble blocked on left side by wall
        if (marbleX0 <= rectX1 && marbleY1 > rectY0 && marbleY0 <= rectY1)
            marble.setBlockedOnLeft(true);
        else
            marble.setBlockedOnLeft(false);

        //marble blocked on right side by wall
        if (marbleX1 >= rectX0 && marbleY1 > rectY0 && marbleY0 <= rectY1)
            marble.setBlockedOnRight(true);
        else
            marble.setBlockedOnRight(false);

        //marble blocked on top with wall
        if (marbleY1 >= rectY0 && marbleX0 <= rectX1 && marbleX0 >= rectX0) {
            marble.setBlockedOnTop(true);
            PlayState.isCollideWall = true;
            return true;
        }

        marble.setBlockedOnRight(false);
        PlayState.isCollideWall = false;
        marble.colliding = false;
        return false;
    }

}
