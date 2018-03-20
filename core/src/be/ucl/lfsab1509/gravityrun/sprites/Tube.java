package be.ucl.lfsab1509.gravityrun.sprites;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Random;

public class Tube {

    private static int MIN_SPACE;
    public static int TUBE_HEIGHT;

    private Random rand;
    private Rectangle boundsLeft, boundsRight;
    private Texture leftTube, rightTube;
    private Vector2 posLeftTube, posRightTube;
    private int sw;

    public Tube(float y, boolean first, int marbleWidth, int sw) {
        rand = new Random();
        this.sw = sw;

        leftTube = new Texture("drawable-" + sw + "/lefttube.png");
        rightTube = new Texture("drawable-" + sw + "/righttube.png");

        posLeftTube = first
                //? new Vector2((GravityRun.WIDTH) / 2, y)
                ? new Vector2((GravityRun.WIDTH - MIN_SPACE) / 2 - leftTube.getWidth(), y)
                : new Vector2(rand.nextInt(sw - MIN_SPACE) - leftTube.getWidth(), y);
        posRightTube = new Vector2(posLeftTube.x + rightTube.getWidth() + MIN_SPACE, y);

        System.out.println("W of Ltube : " + leftTube.getWidth());
        System.out.println("R of Ltube : " + (posLeftTube.x + leftTube.getWidth()));
        System.out.println("L of Ltube : " + posLeftTube.x);
        System.out.println("L of Rtube : " + posRightTube.x);

        boundsRight = new Rectangle(posRightTube.x, posRightTube.y, rightTube.getWidth(), rightTube.getHeight());
        boundsLeft = new Rectangle(posLeftTube.x, posLeftTube.y, leftTube.getWidth(), leftTube.getHeight());

        MIN_SPACE = 3 * marbleWidth;
        TUBE_HEIGHT = leftTube.getHeight();
    }

    public Texture getRightTube() {
        return rightTube;
    }

    public Texture getLeftTube() {
        return leftTube;
    }

    public Vector2 getPosRightTube() {
        return posRightTube;
    }

    public Vector2 getPosLeftTube() {
        return posLeftTube;
    }

    public void reposition(float y) {
        // posLeftTube = new Vector2(rand.nextInt(GravityRun.WIDTH - MIN_SPACE) - leftTube.getWidth(), y);
        // posRightTube = new Vector2(posLeftTube.x + rightTube.getWidth() + MIN_SPACE, y);
        posLeftTube = new Vector2((GravityRun.WIDTH - MIN_SPACE) / 2 - leftTube.getWidth(), y);
        posRightTube = new Vector2(posLeftTube.x + rightTube.getWidth() + MIN_SPACE, y);
        boundsRight.setPosition(posRightTube.x, y);
        boundsLeft.setPosition(posLeftTube.x, y);
    }

    public boolean collides(Circle player) {
        return false;//Intersector.overlaps(player, boundsLeft) || Intersector.overlaps(player, boundsRight);
    }

    public void dispose(){
        leftTube.dispose();
        rightTube.dispose();
    }

}