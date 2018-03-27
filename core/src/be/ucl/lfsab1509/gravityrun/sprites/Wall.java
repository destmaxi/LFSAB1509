package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Wall extends Obstacle {

    private boolean wait;

    public Wall(float y, boolean first, int marbleWidth, int sw) {
        super();

        wait = false;
        obstacleTexture = new Texture("drawable-" + sw + "/wall.png");

        position = rand.nextBoolean()   // right wall ?
                ? first                 // first element ? (easier)
                   ? new Vector2(0, y)
                    : new Vector2(-rand.nextInt(2 * marbleWidth), y)
                : first
                    ? new Vector2(GravityRun.WIDTH - obstacleTexture.getWidth(), y)
                    : new Vector2(-rand.nextInt(2 * marbleWidth) + GravityRun.WIDTH / 2, y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public void collides(Marble marble) {
        float marbleCx = marble.getPosition().x + marble.getDiameter() / 2;
        float marbleCy = marble.getPosition().y + marble.getDiameter() / MarbleAnimation.FRAME_COUNT / 2;

        float rectY0 = position.y;
        float rectX0 = position.x;
        float rectX1 = position.x + obstacleTexture.getWidth();

        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) && Invincible.inWall) {
            wait = true;
        } else {
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
