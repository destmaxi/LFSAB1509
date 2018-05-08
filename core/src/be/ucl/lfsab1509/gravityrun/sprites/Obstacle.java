package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public abstract class Obstacle extends Sprite {

    AbstractPlayScreen playScreen;
    Random random;

    Obstacle(float y, Marble marble, AbstractPlayScreen playScreen, Random random, Texture texture) {
        this(0, y, marble, playScreen, random, texture);
    }

    Obstacle(float x, float y, Marble marble, AbstractPlayScreen playScreen, Random random, Texture texture) {
        super(x, y, marble, random, texture);
        this.playScreen = playScreen;
        this.random = new Random();
    }

}
