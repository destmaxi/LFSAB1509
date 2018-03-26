package be.ucl.lfsab1509.gravityrun.desktop.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.SensorHelper;

public class DesktopSensorHelper extends SensorHelper {
    private float lastTimestamp;
    private float[] lastGravityVector;
    private long lastJumpTimestamp;
    private static final long JUMP_DELAY = 800;

    public DesktopSensorHelper() {
        lastGravityVector = new float[] {0.0f, 0.0f};
        lastJumpTimestamp = System.currentTimeMillis();
    }

    @Override
    public void pauseSensors() {
        // nothing to do
    }

    @Override
    public void resumeSensors() {
        // nothing to do
    }

    @Override
    public float getYGyroscope() {
        return 0; // TODO
    }

    @Override
    public float[] getGravityDirectionVector() {
        int x = Gdx.input.getX() - GravityRun.WIDTH / 2, y = Gdx.input.getY() - GravityRun.HEIGHT / 2;
        System.out.println(x + " " + y);
        int windowWidth = GravityRun.WIDTH, windowHeight = GravityRun.HEIGHT;
        int minWidth = Math.min(windowWidth, windowHeight);
        if (x < -minWidth)
            x = -minWidth;
        if (x > +minWidth)
            x = +minWidth;
        if (y < -minWidth)
            y = -minWidth;
        if (y > +minWidth)
            y = +minWidth;
        float[] ret = new float[] {((float)x) * 2.0f / minWidth, ((float)y) * 2.0f / minWidth};
        System.out.println(ret[0] + " " + ret[1]);
        float[] deltaGravity = new float[] {ret[0] - lastGravityVector[0], ret[1] - lastGravityVector[1]};
        lastGravityVector[0] = ret[0];
        lastGravityVector[1] = ret[1];
        float timestamp = System.nanoTime();
        float dt = timestamp - lastTimestamp;
        lastTimestamp = timestamp;
        // TODO use deltaGravity and dt to measure "angular" velocity of the pointer.
        return ret;
    }

    @Override
    public boolean hasJumped() {
        long currentTimestamp = System.currentTimeMillis();
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && currentTimestamp - lastJumpTimestamp > JUMP_DELAY) {
            lastJumpTimestamp = currentTimestamp;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setGyroscopeSensorType(GyroscopeSensorType type) {
        // nothing to do, as we have no choice
    }

    @Override
    public void setOrientationSensorType(OrientationSensorType type) {
        // nothing to do, as we have no choice
    }
}
