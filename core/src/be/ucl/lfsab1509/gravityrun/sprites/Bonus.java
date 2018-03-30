package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public abstract class Bonus {

    static Marble marble;

    float collideTime;
    private int offset;
    Shape2D bounds;
    private Texture bonusTexture;
    private Vector2 position;

    Bonus(float y, int offset, String path) {
        this.offset = offset;
        Random rand = new Random();

        bonusTexture = new Texture(path);
        position = new Vector2(rand.nextInt(GravityRun.WIDTH - bonusTexture.getWidth()), y);
        bounds = new Rectangle(position.x, position.y, bonusTexture.getWidth(), bonusTexture.getHeight());    }

    public void dispose() {
        bonusTexture.dispose();
    }

    public int getOffset() {
        return offset;
    }

    public Vector2 getPosition() {
        return position;
    }

    public static void initMarble(Marble marble1) {
        marble = marble1;
    }

    public boolean isOutOfScreen(float screenCenterY) {
        float screenBottom = screenCenterY - GravityRun.HEIGHT / 2;
        float bonusTop = position.y + bonusTexture.getHeight();

        return screenBottom >= bonusTop;
    }

    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(bonusTexture, position.x, position.y);
    }

    public abstract boolean collidesMarble();

    public abstract boolean isFinished();

    public abstract void update(float dt);

}
