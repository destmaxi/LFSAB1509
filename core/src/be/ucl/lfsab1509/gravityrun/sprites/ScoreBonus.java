package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class ScoreBonus extends Bonus {

    private PlayScreen playScreen;

    public ScoreBonus(float y, int offset, Texture texture, PlayScreen playScreen) {
        super(y, offset, texture);
        this.playScreen = playScreen;
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
        playScreen.addScoreBonus(100);
        return true;
    }

}
