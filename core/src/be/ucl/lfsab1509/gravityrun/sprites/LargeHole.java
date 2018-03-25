package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class LargeHole extends Obstacle {

    public LargeHole(float y, boolean first, int marbleWidth, int sw) {
        super();

        obstacleTexture = new Texture("drawable-" + sw + "/largehole.png");
        position = new Vector2(0, y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) && marble.getPosition().z == 0) {
            PlayState.gameOver = true;
            return true;
        }
        return false;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
