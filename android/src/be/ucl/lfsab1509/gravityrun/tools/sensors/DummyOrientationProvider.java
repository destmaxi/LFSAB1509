package be.ucl.lfsab1509.gravityrun.tools.sensors;

class DummyOrientationProvider implements OrientationProvider {
    @Override
    public float[] getGravityDirectionVector() {
        return new float[]{0.0f, 0.0f};
    }

    @Override
    public float[] getVelocityVector() {
        return new float[]{0.0f, 0.0f};
    }

    @Override
    public void pauseSensors() {
    }

    @Override
    public void resetOrientation() {
    }

    @Override
    public void resumeSensors() {
    }

    @Override
    public void update() {
    }

    @Override
    public String toString() {
        return "";
    }
}
