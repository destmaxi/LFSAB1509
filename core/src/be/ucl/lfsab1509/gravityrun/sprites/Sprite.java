package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class Sprite {

    Random random;
    Rectangle bounds;
    //Marble marble;
    Texture texture;
    Vector2 position;

    Sprite(float x, float y, Texture texture) {
        this.texture = texture;
        position = new Vector2(x, y);
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    Sprite(float x, float y, Random random, Texture texture) {
        this(x, y, texture);
        this.random = random;
    }

    public abstract boolean collides(Marble marble);

    public Vector2 getPosition() {
        return position;
    }

    public boolean isOutOfScreen(float screenCenterY, int height) {
        float screenBottom = screenCenterY - height / 2;
        float top = position.y + texture.getHeight();

        return screenBottom >= top;
    }

    boolean overlaps(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), bounds);
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x, position.y);
    }
}
