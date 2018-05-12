package be.ucl.lfsab1509.gravityrun.tools.sensors;

public class GravityOrientationProvider implements OrientationProvider {
    private long lastGravityTimeStamp;
    float[] normalizedGravityVector; // 2
    float[] normalizedGravityVelocityVector; // 2
    private GravitySensorEventListener gravitySensorEventListener;

    GravityOrientationProvider() {
        normalizedGravityVelocityVector = new float[] {0.0f, 0.0f};
        normalizedGravityVector = new float[] {0.0f, 0.0f};
    }

    GravityOrientationProvider(GravitySensorEventListener gravitySensorEventListener) {
        this();
        this.gravitySensorEventListener = gravitySensorEventListener;
        gravitySensorEventListener.subscribe(this);
    }

    @Override
    public float[] getGravityDirectionVector() {
        float[] ret = new float[2];
        ret[0] = -normalizedGravityVector[0];
        ret[1] = -normalizedGravityVector[1];
        return ret;
    }

    @Override
    public float[] getVelocityVector() {
        float[] ret = new float[2];
        ret[0] = -normalizedGravityVelocityVector[0];
        ret[1] = -normalizedGravityVelocityVector[1];
        return ret;
    }

    @Override
    public void resetOrientation() {
        // do nothing as we have no choice
    }

    @Override
    public void update() {
        updateValues(gravitySensorEventListener.getRawGravityVector(), gravitySensorEventListener.getLastGravityTimeStamp());
    }

    void updateValues(float[] newGravityVector, long newGravityTimestamp) {
        float dt = newGravityTimestamp - lastGravityTimeStamp; // in nanoseconds
        lastGravityTimeStamp = newGravityTimestamp;
        float[] newNormalizedGravityVector = new float[2];
        AndroidSensorHelper.normalizeVector(newGravityVector, newNormalizedGravityVector);
        AndroidSensorHelper.computeVelocity(normalizedGravityVector, newNormalizedGravityVector, (dt * NS2S), normalizedGravityVelocityVector);
        //System.out.println("GravityOrientationProvider.updateValues + " + dt + " " + Arrays.toString(normalizedGravityVelocityVector) + " " + Arrays.toString(newNormalizedGravityVector) + " " + Arrays.toString(normalizedGravityVector) + " " + lastGravityTimeStamp);
        System.arraycopy(newNormalizedGravityVector, 0, normalizedGravityVector, 0, newNormalizedGravityVector.length);
    }

    @Override
    public String toString() {
        return "Gravity";
    }
}
