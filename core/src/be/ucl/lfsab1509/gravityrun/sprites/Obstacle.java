package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

public abstract class Obstacle extends Sprite {

    Obstacle(float y, AbstractPlayScreen playScreen, Texture texture) {
        this(0, y, playScreen, texture);
    }

    Obstacle(float x, float y, AbstractPlayScreen playScreen, Texture texture) {
        super(x, y, playScreen, texture);
    }

}
