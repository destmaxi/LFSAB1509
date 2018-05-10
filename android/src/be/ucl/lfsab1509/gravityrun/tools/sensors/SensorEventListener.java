package be.ucl.lfsab1509.gravityrun.tools.sensors;

import android.hardware.Sensor;

public interface SensorEventListener extends android.hardware.SensorEventListener {
    static final float NS2S = 1e-9f;

    void dispose();

    Sensor getSensor();

    void subscribe(OrientationProvider orientationProvider);

    void unsubscribe(OrientationProvider orientationProvider);
}
