package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;

public class NewLife extends Bonus {

    public NewLife(float y, int offset, Marble marble, Texture texture) {
        super(y, offset, marble, texture);
    }

    @Override
    public boolean isFinished() {
        marble.addMarbleLife(1);
        return true;
    }

}
