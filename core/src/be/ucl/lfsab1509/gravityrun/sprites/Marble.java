package be.ucl.lfsab1509.gravityrun.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    private static final int MOVEMENT = 100;
    public static int LVL = 1;

    public boolean colliding;
    private Vector3 position, velocity;
    private Rectangle bounds;
    private Texture marble;

    public Marble(int x, int y){
        position = new Vector3(x,y,0);
        velocity = new Vector3(0,0,0);
        marble = new Texture("marble.jpg");
        bounds = new Rectangle(x, y, marble.getWidth(), marble.getHeight());
        colliding = false;
    }

    public void update(float dt){
        velocity.add(-2 * Gdx.input.getGyroscopeY(), 0,0);
        velocity.scl(dt);
        if (!colliding)
            position.add(2 * Gdx.input.getGyroscopeY(),LVL * MOVEMENT * dt,0);

        if (position.x < 0)
            position.x = 0;

        velocity.scl(1 / dt);
        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return position;
    }

    public float getWidth(){
        return marble.getWidth();
    }

    public Texture getMarble() {
        return marble;
    }

    public Rectangle getBounds(){
        return bounds;
    }

    public void dispose(){
        marble.dispose();
    }

}