package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;

public class Invincible extends Bonus {

    private static int activeInvincibles = 0;

    private float collideTime;

    public Invincible(float y, int offset, Texture texture) {
        super(y, offset, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            marble2 = marble;
            activeInvincibles++;
            collideTime = 0;
            marble2.setInvincible(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 3;
    }

    @Override
    public void update(float dt, boolean gameOver) {
        collideTime += dt;

        if (!gameOver && collideTime >= 3 && --activeInvincibles == 0) {
            marble2.setInvincible(false);
            marble2.setInWall(true);
        }
    }

    public static void resetBonus() {
        activeInvincibles = 0;
    }

}
