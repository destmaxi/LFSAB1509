package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class LargeHole extends Obstacle {

    public LargeHole(float y, int sw) {
        super(y, "drawable-" + sw + "/largehole.png");
    }

    @Override
    public void collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) && marble.getCenterPosition().z == 0)
            PlayState.gameOver = true;
    }

}
