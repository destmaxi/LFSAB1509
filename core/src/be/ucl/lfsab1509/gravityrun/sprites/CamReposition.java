package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class CamReposition extends Bonus {

    private PlayScreen playScreen;

    public CamReposition(float y, int offset, Texture texture, PlayScreen playScreen) {
        super(y, offset, texture);
        this.playScreen = playScreen;
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble)) {
            marble2 = marble;
            marble2.setRepositioning(.5f);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isFinished() {
        return playScreen.getCameraPosition().y <= marble2.getCenterPosition().y;
    }

    @Override
    public void update(float dt) {
        if (playScreen.getCameraPosition().y <= marble2.getCenterPosition().y)
            marble2.setRepositioning(1f);
    }

}
