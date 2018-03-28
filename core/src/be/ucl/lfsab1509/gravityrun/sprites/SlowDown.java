package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class SlowDown extends Bonus {

    public static float slowDown = 1f;
    public static int activeSlowDowns = 0;

    public SlowDown(float y, int offset, int sw) {
        super(y, offset, "drawable-" + sw + "/slowdown.png");
    }

    @Override
    public boolean collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            activeSlowDowns++;
            collideTime = 0;
            slowDown = .5f;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 5;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (collideTime >= 5) {
            activeSlowDowns--;
            if (activeSlowDowns == 0)
                slowDown = 1f;
        }
    }

}
