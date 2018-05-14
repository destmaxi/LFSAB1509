package be.ucl.lfsab1509.gravityrun.tools.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class GravitySensorEventListener implements SensorEventListener {
    private float[] rawGravityVector; // 3
    long lastGravityTimeStamp;
    List<OrientationProvider> orientationProviders;
    private Sensor sensor;
    private SensorManager sensorManager;

    GravitySensorEventListener(SensorManager sensorManager, Sensor gravitySensor) {
        this.rawGravityVector = new float[3];
        lastGravityTimeStamp = System.nanoTime();
        orientationProviders = new ArrayList<>();
        sensor = gravitySensor;
        this.sensorManager = sensorManager;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do in our case
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*
         * event.accuracy, event.sensor
         * event.timestamp : in nanoseconds
         * event.values : values. 0: x, 1: y, 2: z in m/s^2
         * If the device is at rest, the gravity is in the positive direction, upwards (instead of downwards).
         */
        //System.out.println("GravitySensorEventListener.onSensorChanged: got accuracy " + event.accuracy + ", timestamp " + event.timestamp + ", values " + Arrays.toString(event.values));
        if (event.values.length == 3) {
            lastGravityTimeStamp = event.timestamp;
            System.arraycopy(event.values, 0, rawGravityVector, 0, rawGravityVector.length);
            for (OrientationProvider orientationProvider : orientationProviders)
                orientationProvider.update();
        } else {
            Log.d("GravityEventListener", "Wrong length of event.values");
        }
    }

    @Override
    public void pause() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void resume() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void dispose() {
        orientationProviders.clear();
    }

    @Override
    public Sensor getSensor() {
        return sensor;
    }

    @Override
    public void subscribe(OrientationProvider orientationProvider) {
        orientationProviders.add(orientationProvider);
    }

    @Override
    public void unsubscribe(OrientationProvider orientationProvider) {
        orientationProviders.remove(orientationProvider);
    }

    float[] getRawGravityVector() {
        return rawGravityVector;
    }

    long getLastGravityTimeStamp() {
        return lastGravityTimeStamp;
    }
}
