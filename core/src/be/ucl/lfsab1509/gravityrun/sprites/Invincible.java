package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class Invincible extends Bonus {

    private static int activeInvincibles = 0;

    private float collideTime;

    public Invincible(float y, int offset, Marble marble, PlayScreen playScreen, Texture texture) {
        super(y, offset, marble, playScreen, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            activeInvincibles++;
            collideTime = 0;
            marble.setInvincible(true);
        }
        return super.collides(marble);
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 3;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (!playScreen.gameOver && collideTime >= 3 && --activeInvincibles == 0) {
            marble.setInvincible(false);
            marble.setInWall(true);
        }
    }

    public static void resetBonus() {
        activeInvincibles = 0;
    }

}
