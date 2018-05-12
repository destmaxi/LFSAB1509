package be.ucl.lfsab1509.gravityrun.tools.sensors;

import android.hardware.Sensor;

interface SensorEventListener extends android.hardware.SensorEventListener {
    float NS2S = 1e-9f;

    void dispose();

    Sensor getSensor();

    void pause();

    void resume();

    void subscribe(OrientationProvider orientationProvider);

    void unsubscribe(OrientationProvider orientationProvider);
}
