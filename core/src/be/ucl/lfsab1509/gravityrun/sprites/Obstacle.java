package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
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

    public void collides(Marble marble, SoundManager soundManager) {
        if (Intersector.overlaps(marble.getBounds(), (Rectangle) bounds) && marble.getCenterPosition().z == 0) {
            soundManager.marbleBreak(PlayState.gameOver);
            PlayState.gameOver = true;
        }
    }

    public void dispose() {
        obstacleTexture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isOutOfScreen(float screenCenterY) {
        float screenBottom = screenCenterY - GravityRun.HEIGHT / 2;
        float obstacleTop = position.y + obstacleTexture.getHeight();

        return screenBottom >= obstacleTop;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(obstacleTexture, position.x, position.y);
    }

    void setX(int x) {
        ((Rectangle) bounds).x = x;
        position.x = x;
    }

}
