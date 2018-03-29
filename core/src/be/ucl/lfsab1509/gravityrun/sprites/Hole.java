package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Hole extends Obstacle {

    public Hole(float y, int sw) {
        super(y, "drawable-" + sw + "/hole.png");
        setX(rand.nextInt(GravityRun.WIDTH - obstacleTexture.getWidth()));
    }

    @Override
    public void collides(Marble marble) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) && marble.getCenterPosition().z == 0)
            PlayState.gameOver = true;
    }

}
