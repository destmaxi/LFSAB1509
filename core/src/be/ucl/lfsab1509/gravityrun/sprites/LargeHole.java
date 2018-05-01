package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.math.Rectangle;

public class LargeHole extends Obstacle {

    public LargeHole(float y, int standardWidth, int marbleWidth) {
        super(y, "drawable-" + standardWidth + "/largehole.png");

        bounds = new Rectangle(position.x + marbleWidth / 2, position.y + marbleWidth / 2,
                obstacleTexture.getWidth() - marbleWidth, obstacleTexture.getHeight() - marbleWidth);
    }

}
