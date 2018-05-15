package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SensorHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class Marble {

    public static final float GRAVITY_COMPENSATION = 1.4f;
    static final int JUMP_HEIGHT = 666;
    private static int MOVEMENT;

    private ArrayList<Bonus> caughtBonuses;
    private boolean blockedOnLeft = false, blockedOnRight = false, blockedOnTop = false, dead = false, inHole = false, invincible = false, inWall = false, isCollidingWall = false, lifeLost = false, myMarble;
    private Circle bounds;
    private float gyroY, repositioning = 1f, slowDown = 1f, speed = 1f, speedUp = 1f;
    private int activeInvincibles, activeSlowdowns, activeSpeedUps, collidedWall, difficulty, height, lives = 5, score, scoreBonus, width;
    private MarbleAnimation marbleAnimation, marbleAnimationInvincible;
    private SensorHelper sensorHelper;
    private Vector3 position;

    public Marble(boolean multiplayer, boolean myMarble, int level, int standardWidth, int x, int y, SensorHelper sensorHelper, Texture marble, Texture marbleInvincible) {
        this.myMarble = myMarble;
        this.sensorHelper = sensorHelper;
        caughtBonuses = new ArrayList<>();
        marbleAnimation = new MarbleAnimation(marble, standardWidth);
        marbleAnimationInvincible = new MarbleAnimation(marbleInvincible, standardWidth);

        height = multiplayer ? GravityRun.MULTI_HEIGHT : GravityRun.HEIGHT;
        width = multiplayer ? GravityRun.MULTI_WIDTH : GravityRun.WIDTH;

        MOVEMENT = height / 5;

        position = new Vector3(x, y, 0);
        bounds = new Circle(x, y, getRadius());
        difficulty = level;
    }

    public void addCaughtBonuses(Bonus bonus) {
        caughtBonuses.add(bonus);
    }

    public void addMarbleLife(int lives) {
        this.lives += lives;
        if (this.lives < 0)
            this.lives = 0;
    }

    public void addPosition(float gyroY, float positionZ) {
        this.gyroY = gyroY;
        this.position.z = positionZ;
    }

    void addScoreBonus() {
        scoreBonus += 100;
    }

    int decreaseActiveInvicibles() {
        return --activeInvincibles;
    }

    int decreaseActiveSpeedUps() {
        return --activeSpeedUps;
    }

    int decreaseActiveSlowdowns() {
        return --activeSlowdowns;
    }

    public void decreaseScoreBonus() {
        scoreBonus -= 100;
    }

    int getActiveSpeedUps() {
        return activeSpeedUps;
    }

    Circle getBounds() {
        return bounds;
    }

    public ArrayList<Bonus> getCaughtBonuses() {
        return caughtBonuses;
    }

    public Vector3 getCenterPosition() {
        return position;
    }

    public int getCollidedWall() {
        return collidedWall;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public float getGyroY() {
        return gyroY;
    }

    public Integer getLives() {
        return lives;
    }

    public TextureRegion getMarble() {
        return getMarbleAnimation().getFrame(position.z);
    }

    private MarbleAnimation getMarbleAnimation() {
        return invincible ? marbleAnimationInvincible : marbleAnimation;
    }

    int getNormalDiameter() {
        return getMarbleAnimation().getDiameter(0);
    }

    private int getRadius() {
        return getMarbleAnimation().getDiameter(position.z) / 2;
    }

    public Integer getScore() {
        return score;
    }

    public float getSlowDown() {
        return slowDown;
    }

    public float getSpeed() {
        return speed;
    }

    public float getSpeedFactor() {
        return difficulty * MOVEMENT * repositioning * slowDown * speed * speedUp;
    }

    public float getSpeedUp() {
        return speedUp;
    }

    void increaseActiveInvincibles() {
        activeInvincibles++;
    }

    void increaseActiveSpeedUps() {
        activeSpeedUps++;
    }

    void increaseActiveSlowdowns() {
        activeSlowdowns++;
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

    public boolean isCollidingWall() {
        return isCollidingWall;
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
        return position.y <= cameraCenterY - height / 2 + getRadius();
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

    public void setBlockedObstacle(boolean blockedOnLeft, boolean blockedOnRight, boolean blockedOnTop) {
        this.blockedOnLeft = blockedOnLeft;
        this.blockedOnRight = blockedOnRight;
        this.blockedOnTop = blockedOnTop;
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

    public void setBonusStatus(boolean invincible, float slowDown, float speed, float speedUp) {
        this.invincible = invincible;
        this.slowDown = slowDown;
        this.speed = speed;
        this.speedUp = speedUp;
    }

    public void setCollidedWall(int collidedWall) {
        this.collidedWall = collidedWall;
    }

    void setCollidingWall(boolean collidingWall) {
        isCollidingWall = collidingWall;
    }

    public void setDead() {
        this.dead = true;
    }

    public void setInHole() {
        this.inHole = true;
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

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setRepositioning(float repositioning) {
        this.repositioning = repositioning;
    }

    public void setScore(int score) {
        this.score = score;
    }

    void setSpeedUp(float speedUp) {
        this.speedUp = speedUp;
    }

    void setSlowDown(float slowDown) {
        this.slowDown = slowDown;
    }

    public void update(float dt) {
        updateJump();
        updatePosition(dt);

        if (dead)
            return;

        marbleAnimation.update(dt);
        marbleAnimationInvincible.update(dt);

        updateSpeed();

        if (myMarble)
            updateScore();

        repositionWithinScreen();

        bounds.setPosition(position.x, position.y);
    }

    private void updateJump() {
        if (myMarble && sensorHelper.hasJumped() && position.z == 0)
            position.z = JUMP_HEIGHT;

        if (position.z > 0 && !dead || (position.z <= 0 && dead && inHole))
            position.add(0, 0, -10 * difficulty * speed * slowDown * speedUp);
        else
            position.z = 0;
    }

    private void updatePosition(float dt) {
        if (dead)
            return;

        //float positionX;
        if (myMarble) {
            //float[] gravity = sensorHelper.getGravityDirectionVector();
            float[] speed1 = sensorHelper.getVelocityVector();
            gyroY = speed1[0];
            //positionX = (GravityRun.WIDTH / 2) * (gravity[0] * GRAVITY_COMPENSATION + 1);
            //System.out.println("updatePosition " + gravity[0] + " " + gravity[1] + " " + positionX + " " + speed1[0]);
        }

        float x = 0f, y = 0f;
        if (!(blockedOnRight && gyroY > 0 || blockedOnLeft && gyroY < 0))
            x = gyroY * width / 75;
        if (!blockedOnTop)
            y = difficulty * MOVEMENT * speed * slowDown * speedUp * dt;
        position.add(x, y, 0);
        // cas presque défaut : !((blockedOnRight && gyroY>0)||(blockedOnLeft && gyroY<0)) && !(blockedOnLeft && gyroY<0 && blockedOnTop) && !(blockedOnRight && gyroY>0 && blockedOnTop)
        //position.x = positionX; // should add 0 for the x position
        // cas défaut : !((blockedOnRight && gyroY>0)||(blockedOnLeft && gyroY<0)) && !(blockedOnLeft && gyroY<0 && blockedOnTop) && !(blockedOnRight && gyroY>0 && blockedOnTop) && !blockedOnTop
        //position.add(0, difficulty * MOVEMENT * speed * slowDown * dt, 0);
        //position.x = positionX; // should add 0 for the x position
    }

    private void updateScore() {
        score = (int) (getCenterPosition().y / height * 100) + scoreBonus;
    }

    private void updateSpeed() {
        speed = (float) Math.log10(score / 100. + 10);
    }

}
