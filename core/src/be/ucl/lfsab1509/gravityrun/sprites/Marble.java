package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.screens.PlayScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

public class Marble {

    public static final float GRAVITY_COMPENSATION = 1.4f;
    public static final float GYRO_COMPENSATION = 2;
    static final int JUMP_HEIGHT = 666;
    private static final int MOVEMENT = GravityRun.HEIGHT / 5;
    public static final float SQRT_2 = (float) Math.sqrt(2);

    private boolean blockedOnLeft = false, blockedOnRight = false, blockedOnTop = false, inHole = false, invincible = false, inWall = false, lifeLost = false;
    private Circle bounds;
    private float repositioning = 1f, slowDown = 1f, speed = 1f;
    private int difficulty, marbleLife = 5;
    private MarbleAnimation marbleAnimation;
    private PlayScreen playScreen;
    private Texture marble;
    private Vector3 position, velocity;

    public Marble(int x, int y, int standardWidth, int level, PlayScreen screen) {
        playScreen = screen;
        marble = new Texture("drawable-" + standardWidth + "/marbles.png");
        marbleAnimation = new MarbleAnimation(marble, standardWidth);
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, MOVEMENT, 0);
        bounds = new Circle(x, y, marbleAnimation.getDiameter(position.z) / 2);
        difficulty = level;
    }

    void addMarbleLife(int lives) {
        this.marbleLife += lives;
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

    private int getDiameter() {
        return marbleAnimation.getDiameter(position.z);
    }

    public TextureRegion getMarble() {
        return marbleAnimation.getFrame(position.z);
    }

    public int getMarbleLife() {
        return marbleLife;
    }

    public int getNormalDiameter() {
        return marbleAnimation.getDiameter(0);
    }

    public float getSpeedFactor() {
        return difficulty * MOVEMENT * repositioning * slowDown * speed;
    }

    public boolean isInvincible() {
        return invincible;
    }

    boolean isInWall() {
        return inWall;
    }

    boolean isLifeLost() {
        return lifeLost;
    }

    public boolean isOutOfScreen(float cameraCenterY) {
        return position.x <= getDiameter() / 2
                || position.x >= (GravityRun.WIDTH - getDiameter() / 2)
                || position.y <= cameraCenterY - GravityRun.HEIGHT / 2 + getDiameter() / 2;
    }

    public void render(SpriteBatch spriteBatch) {
        float marbleX = getCenterPosition().x - getDiameter() / 2;
        float marbleY = getCenterPosition().y - getDiameter() / 2;
        spriteBatch.draw(getMarble(), marbleX, marbleY);
    }

    private void repositionWithinScreen() {
        // TODO voir ce qu'on fait lorsque la bille touche le bord : est-ce qu'il y a une marge ?
        if (position.x < marbleAnimation.getDiameter(position.z) / 2)
            position.x = marbleAnimation.getDiameter(position.z) / 2;

        if (position.x > GravityRun.WIDTH - marbleAnimation.getDiameter(position.z) / 2)
            position.x = GravityRun.WIDTH - marbleAnimation.getDiameter(position.z) / 2;
    }

    void setBlockedOnLeft(boolean blockedOnLeft) {
        this.blockedOnLeft = blockedOnLeft;
    }

    void setBlockedOnRight(boolean blockedOnRight) {
        this.blockedOnRight = blockedOnRight;
    }

    void setBlockedOnTop(boolean blockedOnTop) {
        this.blockedOnTop = blockedOnTop;
    }

    void setInHole(boolean inHole) {
        this.inHole = inHole;
    }

    void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    void setInWall(boolean inWall) {
        this.inWall = inWall;
    }

    void setLifeLost(boolean lifeLost) {
        this.lifeLost = lifeLost;
    }

    void setRepositioning(float repositioning) {
        this.repositioning = repositioning;
    }

    void setSlowDown(float slowDown) {
        this.slowDown = slowDown;
    }

    public void update(float dt, boolean gameOver) {
        marbleAnimation.update(dt, gameOver);

        updateSpeed();

        updateJump(gameOver);

        updatePosition(dt, gameOver);

        repositionWithinScreen();

        bounds.setPosition(position.x, position.y);
    }

    private void updateJump(boolean gameOver) {
        if ((Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && position.z <= 0)
            position.z = JUMP_HEIGHT;

        if (position.z > 0 && !gameOver || position.z <= 0 && gameOver && inHole)
            position.add(0, 0, -10 * difficulty * speed * slowDown);
        else
            position.z = 0;
    }

    private void updatePosition(float dt, boolean gameOver) {
/*
POUR JOUER AVEC LES FLECHES QUAND T'ES SUR UN PUTAIN D'EMULATEUR !!!
C'EST PAS OUF MAIS CA FONCTIONNE +_
        int arrow = 0;
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
            arrow = -10;

        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            arrow = 10;

        if (!gameOver) {
            if ((blockedOnRight && Gdx.input.getGyroscopeY() > 0) || (blockedOnLeft && Gdx.input.getGyroscopeY() < 0))
                position.add(0, difficulty * MOVEMENT * speed * slowDown * dt, 0);
            else if ((blockedOnLeft && Gdx.input.getGyroscopeY() < 0) && blockedOnTop)
                position.add(0, 0, 0);
            else if ((blockedOnRight && Gdx.input.getGyroscopeY() > 0) && blockedOnTop)
                position.add(0, 0, 0);
            else if (blockedOnTop)
                position.add(arrow * GravityRun.WIDTH / 75, 0, 0);
            else
                position.add(arrow * GravityRun.WIDTH / 75, difficulty * MOVEMENT * speed * slowDown * dt, 0);
        }
*/
        if (gameOver)
            return;

        if ((blockedOnRight && Gdx.input.getGyroscopeY() > 0) || (blockedOnLeft && Gdx.input.getGyroscopeY() < 0))
            position.add(0, difficulty * MOVEMENT * speed * slowDown * dt, 0);
        else if ((blockedOnLeft && Gdx.input.getGyroscopeY() < 0) && blockedOnTop)
            position.add(0, 0, 0);
        else if ((blockedOnRight && Gdx.input.getGyroscopeY() > 0) && blockedOnTop)
            position.add(0, 0, 0);
        else if (blockedOnTop)
            position.add(Gdx.input.getGyroscopeY() * GravityRun.WIDTH / 75, 0, 0);
        else
            position.add(Gdx.input.getGyroscopeY() * GravityRun.WIDTH / 75, difficulty * MOVEMENT * speed * slowDown * dt, 0);
    }

    private void updateSpeed() {
        if (playScreen.getScore() < 1000)
            speed = 1f;
        else if (playScreen.getScore() < 2000)
            speed = 1.2f;
        else if (playScreen.getScore() < 3000)
            speed = 1.4f;
        else if (playScreen.getScore() < 4000)
            speed = 1.6f;
        else if (playScreen.getScore() < 5000)
            speed = 1.8f;
        else
            speed = 2f;
    }

}
