package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class Obstacle {

    public static final int HOLE_WIDTH = 52;

    Random rand;
    Texture obstacleTexture;
    Vector2 position;
    Shape2D bounds;

    Obstacle() {
        rand = new Random();
    }

    public Texture getObstacleTexture() {
        return obstacleTexture;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Shape2D getBounds() {
        return bounds;
    }
    public abstract void reposition(float y);
    public abstract boolean collides(Marble marble);
    public void dispose() {
        obstacleTexture.dispose();
    }
}
