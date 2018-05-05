package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Un sprite possède une position, une texture sous-jacente et une forme définissant ses limites.
 */
public abstract class Sprite {
    Rectangle bounds;
    Texture texture;
    Vector2 position;

    Sprite(float x, float y, Texture texture) {
        this.texture = texture;
        position = new Vector2(x, y);
        bounds = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    /**
     * Retourne true si la collision a eu lieu, false sinon.
     * La méthode peut effectuer des actions supplémentaires, comme tuer la bille,
     * changer son nombre de points ou de bonus, changer les vitesses etc.
     *
     * @param marble la bille qui collisionne
     * @return voir ci-dessus
     */
    public abstract boolean collides(Marble marble);

    public void dispose() {
        //texture.dispose();
    }

    /**
     * Retourne true si l'objet est en dehors de l'écran défini par son centre.
     *
     * @param screenCenterY
     * @return voir ci-dessus
     */
    public boolean isOutOfScreen(float screenCenterY) {
        float screenBottom = screenCenterY - GravityRun.HEIGHT / 2;
        float top = position.y + texture.getHeight();

        return screenBottom >= top;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(texture, position.x, position.y);
    }

    boolean overlaps(Marble marble) {
        return Intersector.overlaps(marble.getBounds(), bounds);
    }
}
