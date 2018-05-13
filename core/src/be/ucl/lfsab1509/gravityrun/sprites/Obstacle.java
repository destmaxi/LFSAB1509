package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public abstract class Obstacle extends Sprite {

    AbstractPlayScreen playScreen;
    Random random;

    Obstacle(float y, AbstractPlayScreen playScreen, Random random, Texture texture) {
        this(0, y, playScreen, random, texture);
    }

    Obstacle(float x, float y, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(x, y, random, texture);

        this.playScreen = playScreen;
        this.random = random;
        this.random = new Random();
    }

}
