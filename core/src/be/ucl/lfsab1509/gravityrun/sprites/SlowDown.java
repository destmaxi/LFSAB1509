package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class SlowDown extends Bonus {

    private static int activeSlowDowns = 0;

    public SlowDown(float y, int offset, int standardWidth) {
        super(y, offset, "drawable-" + standardWidth + "/slowdown.png");
    }

    @Override
    public boolean collidesMarble() {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            activeSlowDowns++;
            collideTime = 0;
            marble.setSlowDown(.5f);
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

        if (collideTime >= 5 && --activeSlowDowns == 0)
            marble.setSlowDown(1f);
    }

    public static void resetBonus() {
        activeSlowDowns = 0;
    }

}
