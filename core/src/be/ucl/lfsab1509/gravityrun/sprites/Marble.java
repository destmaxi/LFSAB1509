package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;
import be.ucl.lfsab1509.gravityrun.tools.SensorHelper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    static final int FRAME_COUNT = 5;

    public static final float GRAVITY_COMPENSATION = 1.4f;
    public static final float GYRO_COMPENSATION = 2;
    static final int JUMP_HEIGHT = 666;
    public static final int MOVEMENT = GravityRun.HEIGHT / 5;
    public static final float SQRT2 = (float) Math.sqrt(2);

    public static int lvl;

    public boolean colliding = false;
    private boolean isBlockedOnLeft = false, isBlockedOnRight = false, isBlockedOnTop = false;
    private Circle bounds;
    public float speed;
    private MarbleAnimation marbleAnimation;
    private Texture marble;
    private Vector3 position, velocity;

    public Marble(int x, int y, int sw) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, MOVEMENT, 0);
        marble = new Texture("drawable-" + sw + "/marbles.png");
        marbleAnimation = new MarbleAnimation(marble, sw);
        bounds = new Circle(x, y, marbleAnimation.getDiameter(position.z) / 2);
        lvl = GravityRun.user.getIndexSelected() + 1;
    }

    public void update(float dt, boolean gameOver) {
        marbleAnimation.update(dt, gameOver);

        if (PlayState.score < 1000)
            speed = 1f;
        else if (PlayState.score < 2000)
            speed = 1.2f;
        else if (PlayState.score < 3000)
            speed = 1.4f;
        else if (PlayState.score < 4000)
            speed = 1.6f;
        else if (PlayState.score < 5000)
            speed = 1.8f;
        else
            speed = 2f;

        if (!gameOver) {
            if (SensorHelper.MAIN.hasJumped() && position.z <= 0)
                position.z = JUMP_HEIGHT;

            if (position.z > 0)
                position.add(0, 0, -10 * lvl * speed * SlowDown.slowDown);
            else
                position.z = 0;

            float[] gravity = SensorHelper.MAIN.getGravityDirectionVector();
            float positionX = (GravityRun.WIDTH / 2) * (gravity[0] * GRAVITY_COMPENSATION + 1);
            System.out.println(gravity[0] + " " + gravity[1] + " " + positionX + " " + speed + " " + SlowDown.slowDown + " " + lvl);
            if ((isBlockedOnRight && gravity[0] > 0) || (isBlockedOnLeft && gravity[0] < 0))
                position.add(0, lvl * (MOVEMENT + speed + SlowDown.slowDown) * dt, 0);
            else if ((isBlockedOnLeft && gravity[0] < 0) && isBlockedOnTop)
                position.add(0, 0, 0);
            else if ((isBlockedOnRight && gravity[0] > 0) && isBlockedOnTop)
                position.add(0, 0, 0);
            else if (isBlockedOnTop) {
                position.add(0, 0, 0);
                position.x = positionX;
            } else {
                position.add(0, lvl * (MOVEMENT + speed + SlowDown.slowDown) * dt, 0);
                position.x = positionX;
            }
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
