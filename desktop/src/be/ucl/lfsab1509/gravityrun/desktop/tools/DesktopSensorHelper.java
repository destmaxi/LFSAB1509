package be.ucl.lfsab1509.gravityrun.desktop.tools;

import be.ucl.lfsab1509.gravityrun.tools.SensorHelper;

public class DesktopSensorHelper extends SensorHelper {
    public DesktopSensorHelper() {
        //
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
        return new float[2]; // TODO
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
