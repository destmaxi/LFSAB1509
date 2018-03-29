package be.ucl.lfsab1509.gravityrun.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class CamReposition extends Bonus {

    public static boolean isInReposition;

    private int offset;

    public CamReposition(float y, int sw, int offset) {
        super();

        this.offset = offset;
        bonusTexture = new Texture("drawable-" + sw + "/slowdown.png");
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());
    }


    @Override
    public boolean collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            isInReposition = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void update(float dt) {

    }
}
