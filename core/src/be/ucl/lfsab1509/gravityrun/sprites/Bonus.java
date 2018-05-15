package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public abstract class Bonus extends Sprite {

    static final int CAM_REPOSITION = 1;
    public static final int INVINCIBLE = 2;
    public static final int NEW_LIFE = 3;
    public static final int SCORE_BONUS = 4;
    public static final int SLOW_DOWN = 5;
    public static final int SPEED_UP = 6;

    private boolean renderable = true;
    private int bonusId, offset;

    Bonus(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(0, y, playScreen, texture);

        this.offset = offset;
        position.x = random.nextInt(playScreen.width - texture.getWidth());
        bounds.x = position.x;
    }

    public abstract int getValue();

    abstract void onCollide(Marble marble);

    @Override
    public boolean collides(Marble marble) {
        boolean colliding = overlaps(marble);
        if (colliding)
            onCollide(marble);
        return colliding;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        if (renderable)
            super.render(spriteBatch);
    }

    public Integer getBonusId() {
        return bonusId;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isFinished(Marble marble) {
        return true;
    }

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
    }

    public void stopRender() {
        renderable = false;
    }

    public void update(float dt, Marble marble) {

    }

}
