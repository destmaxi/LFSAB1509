package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Invincible extends Bonus {

    public static boolean inWall = false, isInvincible = false;
    public static int activeInvincibles = 0;

    public Invincible(float y, int offset, int sw) {
        super(y, offset, "drawable-" + sw + "/invincible.png");
    }

    @Override
    public boolean collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            activeInvincibles++;
            collideTime = 0;
            isInvincible = true;
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

        if (collideTime >= 3) {
            activeInvincibles--;
            if (activeInvincibles == 0) {
                inWall = true;
                isInvincible = false;
            }
        }
    }

}
