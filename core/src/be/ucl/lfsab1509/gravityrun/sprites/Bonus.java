package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public abstract class Bonus extends Sprite {

    private int offset;
    Marble marble2;

    Bonus(float y, int offset, Texture texture) {
        super(0, y, texture);
        this.offset = offset;
        Random random = new Random();
        position.x = random.nextInt(GravityRun.WIDTH - texture.getWidth());
        bounds.x = position.x;
    }

    public int getOffset() {
        return offset;
    }

    public abstract boolean isFinished();

    public void update(float dt) {

    }

}
