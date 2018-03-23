package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class Obstacle {

    public static int OBSTACLE_HEIGHT;

    Random rand;
    Shape2D bounds;
    Texture obstacleTexture;
    Vector2 position;

    Obstacle() {
        rand = new Random();
    }

    public Texture getObstacleTexture() {
        return obstacleTexture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public abstract boolean collides(Marble marble);

    public void dispose() {
        obstacleTexture.dispose();
    }
}
