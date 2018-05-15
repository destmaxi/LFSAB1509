package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class SpeedUp extends Bonus {

    private static final float ACTIVE_ADVERSE_SPEEDUP = 0.5f;
    private static final float ACTIVE_SPEEDUP = 2f;
    private static final float ACTIVE_INVINCIBLE_TIME = 4f;
    private static final float ACTIVE_SPEEDUP_TIME = 3f;
    private static final float INACTIVE_SPEEDUP = 1f;

    private float collideTime;
    private boolean activeSpeedUp = true;

    public SpeedUp(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(y, offset, playScreen, random, texture);
    }

    @Override
    public int getValue() {
        return SPEED_UP;
    }

    @Override
    public boolean isFinished(Marble marble) {
        return collideTime >= ACTIVE_INVINCIBLE_TIME;
    }

    @Override
    void onCollide(Marble marble) {
        collideTime = 0;
        marble.setInvincible(true);
        marble.increaseActiveInvincibles();
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(ACTIVE_SPEEDUP);
        playScreen.nbSpeedUp++;
    }

    @Override
    public void update(float dt, Marble marble) {
        collideTime += dt;

        if (marble.getSpeedUp() == ACTIVE_SPEEDUP)
            marble.getCenterPosition().z = Marble.JUMP_HEIGHT;

        if (activeSpeedUp && !marble.isDead() && collideTime >= ACTIVE_SPEEDUP_TIME) {
            activeSpeedUp = false;
            //System.out.println("SpeedUp.update: deactivating speed up " + marble.getActiveSpeedUps() + " " + marble.getActiveInvincibles() + " " + marble.isHoleProtected() + " " + marble.isInWall() + " " + marble.isInvincible() + " " + marble.getSpeedUp());
            if (marble.decreaseActiveSpeedUps() == 0) {
                //System.out.println("SpeedUp.update: deactivating speed up, first case " + marble.getActiveSpeedUps() + " " + marble.getActiveInvincibles() + " " + marble.isHoleProtected() + " " + marble.isInWall() + " " + marble.isInvincible() + " " + marble.getSpeedUp());
                marble.setSpeedUp(INACTIVE_SPEEDUP);
                marble.setHoleProtected(true);
            }
        }

        if (!marble.isDead() && collideTime >= ACTIVE_INVINCIBLE_TIME && marble.decreaseActiveInvicibles() == 0) {
            //System.out.println("SpeedUp.update: deactivating invincible " + marble.getActiveSpeedUps() + " " + marble.getActiveInvincibles() + " " + marble.isHoleProtected() + " " + marble.isInWall() + " " + marble.isInvincible() + " " + marble.getSpeedUp());
            marble.setInvincible(false);
            marble.setInWall(true);
            marble.setHoleProtected(false);
        }/* else if (!marble.isDead() && collideTime >= ACTIVE_INVINCIBLE_TIME) {
            System.out.println("SpeedUp.update: deactivating, second case " + marble.getActiveSpeedUps() + " " + marble.getActiveInvincibles() + " " + marble.isHoleProtected() + " " + marble.isInWall() + " " + marble.isInvincible() + " " + marble.getSpeedUp());
        }*/
    }

    public void activateSpeedUp(Marble marble) {
        collideTime = 0;
        marble.increaseActiveSpeedUps();
        marble.setSpeedUp(ACTIVE_ADVERSE_SPEEDUP);
        marble.setInvincible(false);
        marble.setInWall(true);
    }

}
