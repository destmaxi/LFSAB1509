package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public abstract class Bonus extends Sprite {

    static final int NEW_LIFE = 1;
    static final int SLOWDOWN = 2;
    static final int SCORE_BONUS = 3;
    static final int CAM_REPOSITION = 4;
    static final int EMPTY_BONUS = 5;
    static final int INVINCIBLE = 6;

    private int bonusId, offset;
    AbstractPlayScreen playScreen;

    Bonus(float y, int offset, Texture texture) {
        super(0, y, texture);
        this.offset = offset;
    }

    Bonus(float y, int offset, AbstractPlayScreen playScreen, Random random, Texture texture) {
        this(y, offset, texture);
        this.playScreen = playScreen;
        position.x = random.nextInt(playScreen.width - texture.getWidth());
        bounds.x = position.x;
    }

    public abstract int getValue();

    public abstract boolean isFinished(Marble marble);

    @Override
    public boolean collides(Marble marble) {
        return overlaps(marble);
    }

    public int getBonusId() {
        return bonusId;
    }

    public int getOffset() {
        return offset;
    }

    public void setBonusId(int bonusId) {
        this.bonusId = bonusId;
    }

    public void update(float dt, Marble marble) {

    }

}
