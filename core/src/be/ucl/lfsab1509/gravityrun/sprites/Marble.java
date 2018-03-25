package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    public static boolean colliding = false;
    public static final float GRAVITY_COMPENSATION = 1.4f;
    public static final float GYRO_COMPENSATION = 2;
    public static final float SQRT2 = (float) Math.sqrt(2);
    static final int JUMP_HEIGHT = 700;
    public static final int MOVEMENT = (int) (GravityRun.HEIGHT / 5);
    public static int LVL = GravityRun.user.getIndexSelected() + 1;
    public static int speed;

    private boolean isBlockedOnLeft = false, isBlockedOnRight = false, isBlockedOnTop = false;
    private Circle bounds;
    private MarbleAnimation marbleAnimation;
    private Texture marble;
    private Vector3 position, velocity;

    public Marble(int x, int y, int sw) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, MOVEMENT, 0);
        marble = new Texture("drawable-" + sw + "/marbles.png");
        marbleAnimation = new MarbleAnimation(marble, sw);
        bounds = new Circle(x, y, marbleAnimation.getDiameter(position.z) / 2);
    }

    public void update(float dt, boolean gameOver) {
        marbleAnimation.update(dt, gameOver);
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

        if (Gdx.input.getGyroscopeX() > 2)
            position.z = JUMP_HEIGHT;

        if (position.z > 0 && !gameOver)
            position.add(0,0, -10);
        else
            position.z = 0;

        if (!colliding && !gameOver) {
            if ((isBlockedOnRight && Gdx.input.getGyroscopeY() >= 0) || (isBlockedOnLeft && Gdx.input.getGyroscopeY() >= 0))
                position.add(0,LVL * (MOVEMENT + speed + SlowDown.SLOW_DOWN) * dt,0);
            else if (isBlockedOnTop)
                position.add(Gdx.input.getGyroscopeY() * GravityRun.WIDTH / 75,0,0);
            else
                position.add(Gdx.input.getGyroscopeY() * GravityRun.WIDTH / 75, LVL * (MOVEMENT + speed + SlowDown.SLOW_DOWN) * dt,0);
        }

        if (position.x < marbleAnimation.getDiameter(position.z) / 2)
            position.x = marbleAnimation.getDiameter(position.z) / 2;

        if (position.x > GravityRun.WIDTH - marbleAnimation.getDiameter(position.z) / 2)
            position.x = GravityRun.WIDTH - marbleAnimation.getDiameter(position.z) / 2;

        bounds.setPosition(position.x, position.y);
    }

    public Vector3 getPosition() {
        return new Vector3(position.x - marbleAnimation.getDiameter(position.z) / 2, position.y - marbleAnimation.getDiameter(position.z) / 2, position.z);
    }

    public int getDiameter() {
        return marbleAnimation.getDiameter(position.z);
    }

    public TextureRegion getMarble() {
        return marbleAnimation.getFrame(position.z);
    }

    Circle getBounds() {
        return bounds;
    }

    public void dispose() {
        marble.dispose();
        marbleAnimation.dispose();
    }

    public boolean isBlockedOnRight() {
        return isBlockedOnRight;
    }

    public void setBlockedOnRight(boolean blockedOnRight) {
        isBlockedOnRight = blockedOnRight;
    }

    public boolean isBlockedOnLeft() {
        return isBlockedOnLeft;
    }

    public void setBlockedOnLeft(boolean blockedOnLeft) {
        isBlockedOnLeft = blockedOnLeft;
    }

    public boolean isBlockedOnTop() {
        return isBlockedOnTop;
    }

    public void setBlockedOnTop(boolean blockedOnTop) {
        isBlockedOnTop = blockedOnTop;
    }
}
