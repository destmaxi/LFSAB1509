package be.ucl.lfsab1509.gravityrun.sprites;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

public class EmptyBonus extends Bonus{
    public EmptyBonus(float y, int offset, Texture texture) {
        super(y, offset, texture);
    }

    @Override
    public boolean collidesMarble() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void update(float dt) {

    }
}