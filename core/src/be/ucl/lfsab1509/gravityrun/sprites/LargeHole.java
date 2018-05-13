package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

public class LargeHole extends Hole {

    public LargeHole(float y, Marble marble, AbstractPlayScreen playScreen, Texture texture) {
        super(0, y, playScreen, texture);

        bounds.set(position.x, position.y + marble.getNormalDiameter() / 2,
                texture.getWidth(), texture.getHeight() - marble.getNormalDiameter());
    }

}
