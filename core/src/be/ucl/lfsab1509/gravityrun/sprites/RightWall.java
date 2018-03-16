package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class RightWall extends Obstacle {

    public RightWall(float y) {
        super();

        obstacleTexture = new Texture("wall.png");
        position = new Vector2(GravityRun.WIDTH / 2 - obstacleTexture.getWidth(), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public void reposition(float y) {
        position = new Vector2(GravityRun.WIDTH / 2 - obstacleTexture.getWidth(), y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }
}
