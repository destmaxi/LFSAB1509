package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Invincible extends Bonus {

    static boolean isInvicible = true, inWall = false;
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
    public void reposition(float y) {
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }

    @Override
    public boolean collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            isInvicible = true;
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
        if (isInvicible) {
            collideTime += dt;
        }

        if (collideTime >= 3.4) {
            System.out.println("invincible timed over");
            isInvicible = false;
            inWall = true;
            collideTime = 0;
        }
    }
}
