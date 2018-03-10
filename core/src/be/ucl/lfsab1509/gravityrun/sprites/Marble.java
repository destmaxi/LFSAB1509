package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    private static final int FRAME_COUNT = 5;
    private static final int MOVEMENT = 100;
    public static int LVL = 1;

    public boolean colliding = false;
    private Circle bounds;
    private MarbleAnimation marbleAnimation;
    private Texture marble;
    private Vector3 position, velocity;

    public Marble(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        marble = new Texture("marbles.png");
        marbleAnimation = new MarbleAnimation(marble, FRAME_COUNT, 1);
        bounds = new Circle(x + marble.getWidth() / 2, y + marble.getHeight() / FRAME_COUNT / 2, marble.getWidth() / 2);
    }

    public void update(float dt, boolean gameOver) {
        marbleAnimation.update(dt, gameOver);
        velocity.add(-2 * Gdx.input.getGyroscopeY(), 0,0);
        if (!colliding)
            position.add(2 * Gdx.input.getGyroscopeY(),LVL * MOVEMENT * dt,0);

        if (position.x < 0)
            position.x = 0;
        bounds.setPosition(position.x + marble.getWidth() / 2, position.y + marble.getHeight() / FRAME_COUNT / 2);
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getWidth(){
        return marble.getWidth();
    }

    public TextureRegion getMarble() {
        return marbleAnimation.getFrame();
    }

    public Circle getBounds() {
        return bounds;
    }

    public void dispose() {
        marble.dispose();
        marbleAnimation.dispose();
    }

}