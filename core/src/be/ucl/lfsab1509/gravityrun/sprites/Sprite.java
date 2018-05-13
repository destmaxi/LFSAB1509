package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Sprite {

    AbstractPlayScreen playScreen;
    Rectangle bounds;
    Texture texture;
    Vector2 position;

    Sprite(float x, float y, AbstractPlayScreen playScreen, Texture texture) {
        this.playScreen = playScreen;
        this.texture = texture;

        position = new Vector2(x, y);
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
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
