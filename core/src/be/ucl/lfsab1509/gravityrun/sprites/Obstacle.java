package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public abstract class Obstacle extends Sprite {

    PlayScreen playScreen;
    Random random;

    Obstacle(float y, PlayScreen playScreen, Texture texture) {
        this(0, y, playScreen, texture);
    }

    Obstacle(float x, float y, PlayScreen playScreen, Texture texture) {
        super(x, y, texture);
        this.playScreen = playScreen;
        random = new Random();
    }

}
