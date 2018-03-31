package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class NewLife extends Bonus {
    public NewLife(float y, int offset, int standardWidth) {
        super(y, offset, "drawable-" + standardWidth + "/slowdown.png");
    }

    @Override
    public boolean collidesMarble() {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            marble.setMarbleLife(marble.getMarbleLife() + 1);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void update(float dt) {

    }
}
