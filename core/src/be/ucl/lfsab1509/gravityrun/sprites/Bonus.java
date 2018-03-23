package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class Bonus {

    Texture bonusTexture;
    Vector2 position;
    Random rand;
    Shape2D bounds;

    Bonus(){
        rand = new Random();
    }

    public Texture getObstacleTexture() {
        return bonusTexture;
    }
    public Vector2 getPosition() {
        return position;
    }
    public abstract void reposition(float y);
    public abstract boolean collides(Marble marble);
    public void dispose() {
        bonusTexture.dispose();
    }
}
