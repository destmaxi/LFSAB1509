package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    private static final int FRAME_COUNT = 5;
    private static final int MOVEMENT = 100;
    public static int LVL = GravityRun.user.getIndexSelected()+1;

    public boolean colliding = false;
    private Circle bounds;
    private MarbleAnimation marbleAnimation;
    private Texture marble;
    private Vector3 position;

    public Marble(int x, int y) {
        position = new Vector3(x, y, 0);
        marble = new Texture("marbles_360.png");
        marbleAnimation = new MarbleAnimation(marble, FRAME_COUNT, 1);
        bounds = new Circle(x + marble.getWidth() / 2, y + marble.getHeight() / FRAME_COUNT / 2, marble.getWidth() / 2);
    }

    public void update(float dt, boolean gameOver) {
        marbleAnimation.update(dt, gameOver);
        int speed;
        if (position.y < 1000)
            speed = 0;
        else if (position.y < 2000)
            speed = 20;
        else if (position.y < 3000)
            speed = 40;
        else if (position.y < 4000)
            speed = 60;
        else if (position.y < 5000)
            speed = 80;
        else
            speed = 100;

        if(Gdx.input.getGyroscopeX() > 2)
            position.z = 700;
        
        if(position.z > 0)
            position.add(0,0, -10);
        else
            position.z = 0;

        if (!colliding)
            position.add(2 * Gdx.input.getGyroscopeY(),LVL * (MOVEMENT + speed + SlowDown.SLOW_DOWN) * dt,0);

        if (position.x < 0)
            position.x = 0;

        if (position.x > GravityRun.WIDTH / 2 - marble.getWidth())
            position.x = GravityRun.WIDTH / 2 - marble.getWidth();

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

    Circle getBounds() {
        return bounds;
    }

    public void dispose() {
        marble.dispose();
        marbleAnimation.dispose();
    }

}