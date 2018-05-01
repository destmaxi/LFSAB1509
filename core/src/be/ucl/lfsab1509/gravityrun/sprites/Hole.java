package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.math.Rectangle;

public class Hole extends Obstacle {

    public Hole(float y, int standardWidth, int marbleWidth) {
        super(y, "drawable-" + standardWidth + "/hole.png");
        setX(random.nextInt(GravityRun.WIDTH - obstacleTexture.getWidth()));

        bounds = new Rectangle(position.x + marbleWidth / 2, position.y + marbleWidth / 2,
                obstacleTexture.getWidth() - marbleWidth, obstacleTexture.getHeight() - marbleWidth);
    }

}
