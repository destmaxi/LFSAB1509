package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class Obstacle {

    Random random;
    Shape2D bounds;
    Texture obstacleTexture;
    Vector2 position;

    Obstacle(float y, String path) {
        random = new Random();
        obstacleTexture = new Texture(path);
        position = new Vector2(0, y);
        bounds = new Rectangle(position.x, position.y, obstacleTexture.getWidth(), obstacleTexture.getHeight());
    }

    public void dispose() {
        obstacleTexture.dispose();
    }

    public Texture getObstacleTexture() {
        return obstacleTexture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(obstacleTexture, position.x, position.y);
    }

    void setX(int x) {
        ((Rectangle) bounds).x = x;
        position.x = x;
    }

    public abstract void collides(Marble marble);

}
