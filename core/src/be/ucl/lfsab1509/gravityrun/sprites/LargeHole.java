package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.math.Rectangle;

public class LargeHole extends Obstacle {

    public LargeHole(float y, int standardWidth, int marbleWidth) {
        super(y, "drawable-" + standardWidth + "/largehole.png");

        bounds = new Rectangle(position.x, position.y + marbleWidth / 2,
                obstacleTexture.getWidth(), obstacleTexture.getHeight() - marbleWidth);
    }
}
