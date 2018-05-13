package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EmptyBonus extends Bonus {

    public EmptyBonus(float y, int offset, Texture texture) {
        super(y, offset, null, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        return false;
    }

    @Override
    public int getValue() {
        return EMPTY_BONUS;
    }

    @Override
    public boolean isFinished(Marble marble) {
        return true;
    }

    @Override
    void onCollide(Marble marble) {

    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

}