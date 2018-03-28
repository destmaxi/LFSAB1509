package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class ScoreBonus extends Bonus {

    public ScoreBonus(float y, int offset, int sw) {
        super(y, offset, "drawable-" + sw + "/scorebonus.png");
    }

    @Override
    public boolean collides(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), (Rectangle) bounds);
    }

    @Override
    public boolean isFinished() {
        PlayState.scoreBonus += 100;
        return true;
    }

    @Override
    public void update(float dt) {

    }

}
