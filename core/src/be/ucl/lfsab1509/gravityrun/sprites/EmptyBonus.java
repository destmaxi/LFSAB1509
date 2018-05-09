package be.ucl.lfsab1509.gravityrun.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class EmptyBonus extends Bonus{
    public EmptyBonus(float y, int offset, Texture texture) {
        super(y, offset, texture);
    }

    @Override
    public boolean collides(Marble marble) {
        return false;
    }

    @Override
    public boolean isFinished(Marble marble) {
        return true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void update(float dt, Marble marble) {

    }
}