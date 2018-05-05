package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;

public class NewLife extends Bonus {
    public NewLife(float y, int offset, Texture texture) {
        super(y, offset, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            marble2 = marble;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFinished() {
        marble2.addMarbleLife(1);
        return true;
    }
}
