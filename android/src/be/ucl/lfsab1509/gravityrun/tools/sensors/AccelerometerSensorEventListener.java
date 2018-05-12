package be.ucl.lfsab1509.gravityrun.tools.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class AccelerometerSensorEventListener implements SensorEventListener {
    private float[] rawAcceleration;
    private long lastAccelerationTimestamp;
    private List<OrientationProvider> orientationProviders;
    private Sensor sensor;
    private SensorManager sensorManager;

    AccelerometerSensorEventListener(SensorManager sensorManager, Sensor accelerometer) {
        rawAcceleration = new float[] {0.0f, 0.0f, SensorManager.GRAVITY_EARTH};
        lastAccelerationTimestamp = System.nanoTime();
        orientationProviders = new ArrayList<>();
        sensor = accelerometer;
        this.sensorManager = sensorManager;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do in our case
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //System.out.println("AccelerometerSensorEventListener.onSensorChanged " + Arrays.toString(event.values) + " " + event.timestamp);
        if (event.values.length == 3) {
            //float dt = event.timestamp - lastAccelerationTimestamp;
            lastAccelerationTimestamp = event.timestamp;
            System.arraycopy(event.values, 0, rawAcceleration, 0, rawAcceleration.length);
            for (OrientationProvider orientationProvider : orientationProviders)
                orientationProvider.update();
        } else {
            Log.d("AccelerometerListener", "Wrong length of event.values");
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

    float[] getRawAccelerationVector() {
        return rawAcceleration;
    }

    long getLastAccelerationTimestamp() {
        return lastAccelerationTimestamp;
    }
}
