package be.ucl.lfsab1509.gravityrun.tools.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class GyroscopeSensorEventListener implements SensorEventListener {
    private float[] rawGyroscopeVector;
    private long lastGyroscopeTimeStamp;
    private List<OrientationProvider> orientationProviders;
    private Sensor sensor;

    GyroscopeSensorEventListener(Sensor gyroscope) {
        rawGyroscopeVector = new float[] {0.0f, 0.0f, 0.0f};
        lastGyroscopeTimeStamp = System.nanoTime();
        orientationProviders = new ArrayList<>();
        sensor = gyroscope;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nothing to do in our case
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*
         * event.accuracy, event.sensor
         * event.timestamp: in nanoseconds
         * event.values: 0 is x, 1 is y, 2 is z  In rad/s
         */
        //System.out.println("GyroscopeSensorEventListener.onSensorChanged: " + event.timestamp + ", values " + Arrays.toString(event.values));
        if (event.values.length == 3) {
            System.arraycopy(event.values, 0, rawGyroscopeVector, 0, event.values.length);
            lastGyroscopeTimeStamp = event.timestamp;
            for (OrientationProvider orientationProvider : orientationProviders)
                orientationProvider.update();
        } else {
            Log.d("RotationEventListener", "Wrong length of event values");
        }
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
        orientationProviders.add(orientationProvider);
    }

    float[] getRawGyroscopeVector() {
        return rawGyroscopeVector;
    }

    long getLastGyroscopeTimeStamp() {
        return lastGyroscopeTimeStamp;
    }
}
