package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public abstract class Bonus extends Sprite {

    private int offset;
    Marble marble;
    PlayScreen playScreen;

    Bonus(float y, int offset, Marble marble, Texture texture) {
        super(0, y, texture);
        this.offset = offset;
        this.marble = marble;
        Random random = new Random();
        position.x = random.nextInt(GravityRun.WIDTH - texture.getWidth());
        bounds.x = position.x;
    }

    Bonus(float y, int offset, Marble marble, PlayScreen playScreen, Texture texture) {
        this(y, offset, marble, texture);
        this.playScreen = playScreen;
    }

    @Override
    public boolean collides(Marble marble) {
        return overlaps(marble);
    }

    public int getOffset() {
        return offset;
    }

    public abstract boolean isFinished();

    public void update(float dt) {

    }

}
