package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class SlowDown extends Bonus {

    private static int activeSlowDowns = 0;

    private float collideTime;

    public SlowDown(float y, int offset, Marble marble, PlayScreen playScreen, Texture texture) {
        super(y, offset, marble, playScreen, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            activeSlowDowns++;
            collideTime = 0;
            marble.setSlowDown(.5f);
        }
        return super.collides(marble);
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 5;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (!playScreen.gameOver && collideTime >= 5 && --activeSlowDowns == 0)
            marble.setSlowDown(1f);
    }

    public static void resetBonus() {
        activeSlowDowns = 0;
    }

}
