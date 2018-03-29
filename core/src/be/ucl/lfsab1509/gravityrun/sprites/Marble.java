package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.states.PlayState;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    public static final float GRAVITY_COMPENSATION = 1.4f;
    public static final float GYRO_COMPENSATION = 2;
    static final int JUMP_HEIGHT = 666;
    public static final int MOVEMENT = GravityRun.HEIGHT / 5;
    public static final float SQRT_2 = (float) Math.sqrt(2);

    public static int lvl;

    public boolean colliding = false;
    private boolean blockedOnLeft = false, blockedOnRight = false, blockedOnTop = false, invincible = false, inWall = false;
    private Circle bounds;
    public float speed = 1f;
    private float slowDown = 1f;
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

        if ((Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && position.z <= 0)
            position.z = JUMP_HEIGHT;

        if (position.z > 0 && !gameOver)
            position.add(0, 0, -10 * lvl * speed * slowDown);
        else
            position.z = 0;

        if (!gameOver) {
            if ((blockedOnRight && Gdx.input.getGyroscopeY() > 0) || (blockedOnLeft && Gdx.input.getGyroscopeY() < 0))
                position.add(0, lvl * MOVEMENT * speed * slowDown * dt, 0);
            else if ((blockedOnLeft && Gdx.input.getGyroscopeY() < 0) && blockedOnTop)
                position.add(0, 0, 0);
            else if ((blockedOnRight && Gdx.input.getGyroscopeY() > 0) && blockedOnTop)
                position.add(0, 0, 0);
            else if (blockedOnTop)
                position.add(Gdx.input.getGyroscopeY() * GravityRun.WIDTH / 75, 0, 0);
            else
                position.add(Gdx.input.getGyroscopeY() * GravityRun.WIDTH / 75, lvl * MOVEMENT * speed * slowDown * dt, 0);
        }

        if (position.x < marbleAnimation.getDiameter(position.z) / 2)
            position.x = marbleAnimation.getDiameter(position.z) / 2;

        if (position.x > GravityRun.WIDTH - marbleAnimation.getDiameter(position.z) / 2)
            position.x = GravityRun.WIDTH - marbleAnimation.getDiameter(position.z) / 2;

        bounds.setPosition(position.x, position.y);
    }

    public void dispose() {
        marble.dispose();
        marbleAnimation.dispose();
    }

    Circle getBounds() {
        return bounds;
    }

    public Vector3 getCenterPosition() {
        return position;
    }

    public int getDiameter() {
        return marbleAnimation.getDiameter(position.z);
    }

    public TextureRegion getMarble() {
        return marbleAnimation.getFrame(position.z);
    }

    public int getNormalDiameter() {
        return marbleAnimation.getDiameter(0);
    }

    public float getSlowDown() {
        return slowDown;
    }

    public boolean isBlockedOnLeft() {
        return blockedOnLeft;
    }

    public boolean isBlockedOnRight() {
        return blockedOnRight;
    }

    public boolean isBlockedOnTop() {
        return blockedOnTop;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public boolean isInWall() {
        return inWall;
    }

    public void setBlockedOnLeft(boolean blockedOnLeft) {
        this.blockedOnLeft = blockedOnLeft;
    }

    public void setBlockedOnRight(boolean blockedOnRight) {
        this.blockedOnRight = blockedOnRight;
    }

    public void setBlockedOnTop(boolean blockedOnTop) {
        this.blockedOnTop = blockedOnTop;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public void setInWall(boolean inWall) {
        this.inWall = inWall;
    }

    public void setSlowDown(float slowDown) {
        this.slowDown = slowDown;
    }

}
