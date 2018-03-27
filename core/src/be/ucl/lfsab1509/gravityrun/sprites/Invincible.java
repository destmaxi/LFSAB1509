package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Invincible extends Bonus {

    public static boolean isInvincible = false, inWall = false;
    public static int n = 0;

    private float collideTime;
    private int offset;

    public Invincible(float y, int sw, int offset) {
        super();

        this.offset = offset;
        bonusTexture = new Texture("drawable-" + sw + "/invincible.png");
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            isInvincible = true;
            collideTime = 0;
            n++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return collideTime >= 3;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void update(float dt) {
        collideTime += dt;

        if (collideTime >= 3) {
            if (n <= 1) {
                isInvincible = false;
                inWall = true;
                n = 0;
            } else
                n--;
        }
    }
}
