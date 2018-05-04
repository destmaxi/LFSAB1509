package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public abstract class Obstacle extends Sprite {

    Random random;
    PlayScreen playScreen;

    Obstacle(float y, Texture texture, PlayScreen playScreen) {
        this(0, y, texture, playScreen);
    }

    Obstacle(float x, float y, Texture texture, PlayScreen playScreen) {
        super(x, y, texture);
        random = new Random();
        this.playScreen = playScreen;
    }

    void setX(int x) {
        bounds.x = x;
        position.x = x;
    }

}
