package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class ScoreBonus extends Bonus {

    public ScoreBonus(float y, int offset, Marble marble, PlayScreen playScreen, Texture texture) {
        super(y, offset, marble, playScreen, texture);
    }

    @Override
    public boolean isFinished() {
        playScreen.addScoreBonus(100);
        return true;
    }

}
