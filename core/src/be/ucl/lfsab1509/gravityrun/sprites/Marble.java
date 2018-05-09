package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.screens.AbstractPlayScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Marble {

    static final int JUMP_HEIGHT = 666;
    private static int MOVEMENT;

    private ArrayList<Bonus> caughtBonuses;
    private int scoreBonus, collidedWall, score;
    private boolean blockedOnLeft = false, blockedOnRight = false, blockedOnTop = false, inHole = false, invincible = false, inWall = false, lifeLost = false, myMarble, dead = false, isCollideWall = false;
    private Circle bounds;
    private float repositioning = 1f, slowDown = 1f, speed = 1f, gyroY;
    private int difficulty, height, marbleLife = 5, width;
    private MarbleAnimation marbleAnimation, marbleAnimationInvincible;
    private AbstractPlayScreen playScreen;
    private Texture marble, marbleInvincible;
    private Vector3 position;

    public Marble(boolean myMarble, boolean multiplayer, int x, int y, int standardWidth, int level, AbstractPlayScreen screen) {
        this.myMarble = myMarble;
        playScreen = screen;
        caughtBonuses = new ArrayList<>();
        marble = new Texture("drawable-" + standardWidth + "/marbles.png");
        marbleInvincible = new Texture("drawable-" + standardWidth + "/marbles_invincible.png");
        marbleAnimation = new MarbleAnimation(marble, standardWidth);
        marbleAnimationInvincible = new MarbleAnimation(marbleInvincible, standardWidth);

        height = multiplayer ? GravityRun.MULTI_HEIGHT : GravityRun.HEIGHT;
        width = multiplayer ? GravityRun.MULTI_WIDTH : GravityRun.WIDTH;

        MOVEMENT = height / 5;

        position = new Vector3(x, y, 0);
        bounds = new Circle(x, y, getRadius());
        difficulty = level;
    }

    public void addCatchedBonuses(Bonus bonus) {
        caughtBonuses.add(bonus);
    }

    void addMarbleLife(int lives) {
        this.marbleLife += lives;
    }

    public void addPosition(float gyroY, float slowDown, boolean blockedOnLeft, boolean blockedOnRight, boolean blockedOnTop, float positionZ, float speed, boolean invincible) {
        this.gyroY = gyroY;
        this.position.z = positionZ;
        this.slowDown = slowDown;
        this.blockedOnTop = blockedOnTop;
        this.blockedOnRight = blockedOnRight;
        this.blockedOnLeft = blockedOnLeft;
        this.speed = speed;
        this.invincible = invincible;
    }

    public void addScoreBonus () {
        scoreBonus += 100;
    }

    public void dispose() {
        marble.dispose();
        marbleInvincible.dispose();
    }

    Circle getBounds() {
        return bounds;
    }

    public Vector3 getCenterPosition() {
        return position;
    }

    public int getCollidedWall() {
        return collidedWall;
    }

    public TextureRegion getMarble() {
        return getMarbleAnimation().getFrame(position.z);
    }

    private MarbleAnimation getMarbleAnimation() {
        return invincible ? marbleAnimationInvincible : marbleAnimation;
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

    public ArrayList<Bonus> getCaughtBonuses() {
        return caughtBonuses;
    }

    public int getScore() {
        return score;
    }

    public float getSlowDown() {
        return slowDown;
    }

    public float getSpeed() {
        return speed;
    }

    public int getMarbleLife() {
        return marbleLife;
    }

    public int getNormalDiameter() {
        return getMarbleAnimation().getDiameter(0);
    }

    public int getRadius() {
        return getMarbleAnimation().getDiameter(position.z) / 2;
    }

    public int getScoreBonus() {
        return scoreBonus;
    }

    public float getSpeedFactor() {
        return difficulty * MOVEMENT * repositioning * slowDown * speed;
    }

    public boolean isCollideWall() {
        return isCollideWall;
    }

    public boolean isDead() {
        return dead;
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
        return position.x <= getRadius()
                || position.x >= (width - getRadius())
                || position.y <= cameraCenterY - height / 2 + getRadius();
    }

    public void render(SpriteBatch spriteBatch) {
        float marbleX = getCenterPosition().x - getRadius();
        float marbleY = getCenterPosition().y - getRadius();

        spriteBatch.draw(getMarble(), marbleX, marbleY);
    }

    private void repositionWithinScreen() {
        // TODO voir ce qu'on fait lorsque la bille touche le bord : est-ce qu'il y a une marge ?
        if (position.x < getRadius())
            position.x = getRadius();

        if (position.x > width - getRadius())
            position.x = width - getRadius();
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

    public void setCollidedWall(int collidedWall) {
        this.collidedWall = collidedWall;
    }

    public void setCollideWall(boolean collideWall) {
        isCollideWall = collideWall;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
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

    public void setMarbleLife(int marbleLife) {
        this.marbleLife = marbleLife;
    }

    public void setRepositioning(float repositioning) {
        this.repositioning = repositioning;
    }

    public void setScore(int score) {
        this.score = score;
    }

    void setSlowDown(float slowDown) {
        this.slowDown = slowDown;
    }

    public void update(float dt) {
        marbleAnimation.update(dt, isDead());
        marbleAnimationInvincible.update(dt, isDead());

        updateSpeed();

        updateJump(isDead());

        updatePosition(dt, isDead());

        repositionWithinScreen();

        bounds.setPosition(position.x, position.y);
    }

    private void updateJump(boolean gameOver) {
        if (myMarble && (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) && position.z <= 0)
            position.z = JUMP_HEIGHT;

        if (position.z > 0 && !gameOver || position.z <= 0 && gameOver && inHole)
            position.add(0, 0, -10 * difficulty * speed * slowDown);
        else
            position.z = 0;
    }

    private void updatePosition(float dt, boolean died) {
        if (died)
            return;

        if(myMarble)
            gyroY = Gdx.input.getGyroscopeY();

        if ((blockedOnRight && gyroY > 0) || (blockedOnLeft && gyroY < 0))
            position.add(0, difficulty * MOVEMENT * speed * slowDown * dt, 0);
        else if ((blockedOnLeft && gyroY < 0) && blockedOnTop)
            position.add(0, 0, 0);
        else if ((blockedOnRight && gyroY > 0) && blockedOnTop)
            position.add(0, 0, 0);
        else if (blockedOnTop)
            position.add(gyroY * width / 75, 0, 0);
        else
            position.add(gyroY * width / 75, difficulty * MOVEMENT * speed * slowDown * dt, 0);
    }

    private void updateSpeed() {
        if (score < 1000)
            speed = 1f;
        else if (score < 2000)
            speed = 1.2f;
        else if (score < 3000)
            speed = 1.4f;
        else if (score < 4000)
            speed = 1.6f;
        else if (score < 5000)
            speed = 1.8f;
        else
            speed = 2f;
    }

}
