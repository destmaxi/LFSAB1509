package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
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
    public void reposition(float y) {
        position = new Vector2(0, y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }

}
