package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;

public class SlowDown extends Bonus {

    private static int activeSlowDowns = 0;

    private float collideTime;

    public SlowDown(float y, int offset, Texture texture) {
        super(y, offset, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            marble2 = marble;
            activeSlowDowns++;
            collideTime = 0;
            marble2.setSlowDown(.5f);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 5;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (collideTime >= 5 && --activeSlowDowns == 0)
            marble2.setSlowDown(1f);
    }

    public static void resetBonus() {
        activeSlowDowns = 0;
    }

}
