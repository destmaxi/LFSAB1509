package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class ScoreBonus extends Bonus {

    private PlayScreen playScreen;

    public ScoreBonus(float y, int offset, int standardWidth, PlayScreen playScreen) {
        super(y, offset, "drawable-" + standardWidth + "/scorebonus.png");
        this.playScreen = playScreen;
    }

    @Override
    public boolean collidesMarble() {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds)) {
            PlayScreen.nbScoreBonus++;
            return true;
        }
        return false;
    }

    @Override
    public boolean isFinished() {
        playScreen.setScoreBonus(playScreen.getScoreBonus() + 100);
        return true;
    }

    @Override
    public void update(float dt) {

    }

}
