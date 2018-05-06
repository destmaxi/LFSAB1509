package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

public class Hole extends Obstacle {

    public Hole(float y, int marbleWidth, PlayScreen playScreen, Texture texture) {
        super(0, y, playScreen, texture);
        position.x = random.nextInt(GravityRun.WIDTH - texture.getWidth());
        bounds.set(position.x + marbleWidth / 2, position.y + marbleWidth / 2,
                texture.getWidth() - marbleWidth, texture.getHeight() - marbleWidth);
    }

    Hole(float x, float y, PlayScreen playScreen, Texture texture) {
        super(x, y, playScreen, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble) && marble.getCenterPosition().z == 0) {
            marble.setInHole(true);
            playScreen.getSoundManager().marbleBreak(playScreen.gameOver);
            playScreen.gameOver = true;
            return true;
        } else
            return false;
    }

}
