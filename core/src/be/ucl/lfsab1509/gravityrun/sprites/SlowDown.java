package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class SlowDown extends Bonus {

    public static float slowDown = 1f;

    private float collideTime;
    private int offset;

    public SlowDown(float y, int sw, int offset) {
        super();

        this.offset = offset;
        bonusTexture = new Texture("drawable-" + sw + "/slowdown.png");
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    @Override
    public void reposition(float y) {
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            slowDown = .5f;
            collideTime = 0;
            return true;
        }

        return false;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void update(float dt) {
        if (slowDown == .5f)
            collideTime += dt;

        if (collideTime >= 5) {
            collideTime = 0;
            slowDown = 1f;
        }

    }
}
