package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class LargeHole extends Obstacle {

    public LargeHole(float y, int sw) {
        super();

        obstacleTexture = new Texture("drawable-" + sw + "/largehole.png");
        position = new Vector2(0, y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public void reposition(float y) {
        position = new Vector2(0, y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        return false;
       // return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
