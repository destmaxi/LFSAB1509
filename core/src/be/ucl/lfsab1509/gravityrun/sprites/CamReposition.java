package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class CamReposition extends Bonus {

    public CamReposition(float y, int offset, Marble marble, PlayScreen playScreen, Texture texture) {
        super(y, offset, marble, playScreen, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble))
            marble.setRepositioning(.5f);
        return super.collides(marble);
    }

    @Override
    public boolean isFinished() {
        return playScreen.getCameraPosition().y <= marble.getCenterPosition().y;
    }

    @Override
    public void update(float dt) {
        if (!playScreen.gameOver && playScreen.getCameraPosition().y <= marble.getCenterPosition().y)
            marble.setRepositioning(1f);
    }

}
