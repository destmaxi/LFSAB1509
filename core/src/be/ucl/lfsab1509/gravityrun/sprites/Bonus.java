package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public abstract class Bonus extends Sprite {

    static final int CAM_REPOSITION = 4;
    static final int EMPTY_BONUS = 5;
    static final int INVINCIBLE = 6;
    public static final int NEW_LIFE = 1;
    public static final int SCORE_BONUS = 3;
    public static final int SLOWDOWN = 2;
    public static final int SPEED_UP = 4;

    private boolean renderable = true;
    private int bonusId, offset;

    Bonus(float y, int offset, AbstractPlayScreen playScreen, Texture texture) {
        super(0, y, playScreen, texture);

        this.offset = offset;
    }

    Bonus(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        this(y, offset, playScreen, texture);

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
