package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Invincible extends Bonus {

    private static int activeInvincibles = 0;

    public Invincible(float y, int offset, int standardWidth) {
        super(y, offset, "drawable-" + standardWidth + "/invincible.png");
    }

    @Override
    public boolean collidesMarble() {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            activeInvincibles++;
            collideTime = 0;
            marble.setInvincible(true);
            PlayScreen.nbInvincible++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 3;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (collideTime >= 3 && --activeInvincibles == 0) {
            marble.setInvincible(false);
            marble.setInWall(true);
        }
    }

    public static void resetBonus() {
        activeInvincibles = 0;
    }

}
