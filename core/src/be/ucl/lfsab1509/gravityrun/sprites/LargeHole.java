package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class LargeHole extends Hole {

    public LargeHole(float y, Marble marble, Random random, AbstractPlayScreen playScreen, Texture texture) {
        super(0, y, playScreen, random, texture);
        bounds.set(position.x, position.y + marble.getNormalDiameter() / 2,
                texture.getWidth(), texture.getHeight() - marble.getNormalDiameter());
    }

}
