package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Un sprite possède une position, une texture sous-jacente et une forme définissant ses limites.
 */
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

    /**
     * Retourne true si la collision a eu lieu, false sinon.
     * La méthode peut effectuer des actions supplémentaires, comme tuer la bille,
     * changer son nombre de points ou de bonus, changer les vitesses etc.
     *
     * @return voir ci-dessus
     * @param marble
     */
    public abstract boolean collides(Marble marble);

    public void dispose() {
//        texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    /**
     * Retourne true si l'objet est en dehors de l'écran défini par son centre.
     *
     * @param screenCenterY
     * @param height
     * @return voir ci-dessus
     */
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
