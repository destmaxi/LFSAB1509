package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;

public class Hole extends Obstacle {

    public Hole(float y, int standardWidth) {
        super(y, "drawable-" + standardWidth + "/hole.png");
        setX(random.nextInt(GravityRun.WIDTH - obstacleTexture.getWidth()));
    }

}
