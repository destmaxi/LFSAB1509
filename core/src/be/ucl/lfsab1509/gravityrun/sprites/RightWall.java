package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class RightWall extends Obstacle {

    public RightWall(float y, int sw) {
        super();

        obstacleTexture = new Texture("drawable-" + sw + "/wall.png");
        position = new Vector2(GravityRun.WIDTH - obstacleTexture.getWidth(), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public void reposition(float y) {
        position = new Vector2(GravityRun.WIDTH - obstacleTexture.getWidth(), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }

}
