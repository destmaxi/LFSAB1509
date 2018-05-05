package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class Hole extends Obstacle {

    public Hole(float y, int marbleWidth, Texture texture, PlayScreen playScreen) {
        super(0, y, texture, playScreen);
        position.x = random.nextInt(GravityRun.WIDTH - texture.getWidth());
        bounds.set(position.x + marbleWidth / 2, position.y + marbleWidth / 2,
                texture.getWidth() - marbleWidth, texture.getHeight() - marbleWidth);
    }

    public Hole(float x, float y, int marbleWidth, Texture texture, PlayScreen playScreen) {
        super(x, y, texture, playScreen);
        bounds.set(position.x + marbleWidth / 2, position.y + marbleWidth / 2,
                texture.getWidth() - marbleWidth, texture.getHeight() - marbleWidth);
    }

    @Override
    public boolean collides(Marble marble) {
        if (overlaps(marble) && marble.getCenterPosition().z == 0) {
            playScreen.getSoundManager().marbleBreak(PlayScreen.gameOver);
            PlayScreen.gameOver = true;
            return true;
        } else {
            return false;
        }
    }
}
