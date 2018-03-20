package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    private static final int FRAME_COUNT = 5;
    private static final int MOVEMENT = (int) (GravityRun.HEIGHT / 5);//(int)(GravityRun.HEIGHT/GravityRun.DENSITY);//(int)(100*GravityRun.DENSITY/(GravityRun.HEIGHT/1000f));
    public static int LVL = 1;

    public boolean colliding = false;
    private Circle bounds;
    private MarbleAnimation marbleAnimation;
    private Texture marble;
    private Vector3 position, velocity;

    public Marble(int x, int y, int sw) {
        marble = new Texture("drawable-" + sw + "/marbles.png");
        marbleAnimation = new MarbleAnimation(marble, FRAME_COUNT, 1);
        bounds = new Circle(x + marble.getWidth() / 2, y + marble.getHeight() / FRAME_COUNT / 2, marble.getWidth() / 2);
        position = new Vector3(x - marble.getWidth() / 2, y, 0);
        velocity = new Vector3(0, 0, 0);
        System.out.println("MOVEMENT = " + MOVEMENT);
    }

    public void update(float dt, boolean gameOver) {
        marbleAnimation.update(dt, gameOver);
        // velocity.add(-Gdx.input.getGyroscopeY() * GravityRun.WIDTH / GravityRun.DENSITY / 50, 0,0);
        velocity.add(-8 * Gdx.input.getGyroscopeY(), 0, 0);
        // System.out.println("gyroY = " + Gdx.input.getGyroscopeY());
        if (!colliding)
            position.add(Gdx.input.getGyroscopeY() * GravityRun.WIDTH / 75,LVL * MOVEMENT * dt,0);

        if (position.x < 0)
            position.x = 0;
        bounds.setPosition(position.x + marble.getWidth() / 2, position.y + marble.getHeight() / FRAME_COUNT / 2);
    }

    public Vector3 getPosition() {
        return position;
    }

    public int getWidth() {
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