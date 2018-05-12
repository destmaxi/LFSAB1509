package be.ucl.lfsab1509.gravityrun.tools.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import be.ucl.lfsab1509.gravityrun.tools.SensorHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.AndroidApplication;

import java.util.ArrayList;
import java.util.List;

public class AndroidSensorHelper implements SensorHelper {

    private SensorManager sensorManager;

    private Sensor accelerometerSensor;
    private Sensor magnetoSensor;
    //private Sensor orientationSensor;
    private Sensor gyroscopeSensor;
    private Sensor gravitySensor;
    private Sensor rotationSensor; // requires API 18
    //private Sensor gameRotationSensor;
    private Sensor geomagneticRotationSensor; // requires API 19

    private List<SensorEventListener> sensorEventListeners;
    private List<OrientationProvider> orientationProviders;
    private int currentOrientationProvider;

    private GravitySensorEventListener gravitySensorEventListener;
    //private RotationVectorSensorEventListener rotationVectorSensorEventListener;
    private GyroscopeSensorEventListener gyroscopeSensorEventListener;

    private GravityOrientationProvider gravityOrientationProvider;
    private GyroscopeOrientationProvider gyroscopeOrientationProvider;
    private AccelerometerOrientationProvider accelerometerOrientationProvider;

    /*static class RotationVectorSensorEventListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Nothing to do in our case
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            //
            //System.out.println("RotationVectorSensorEventListener.onSensorChanged: got accuracy " + event.accuracy + ", timestamp " + event.timestamp + ", values " + Arrays.toString(event.values));
        }
    }*/

    public AndroidSensorHelper(AndroidApplication activity) {
        this.orientationProviders = new ArrayList<>();
        this.sensorEventListeners = new ArrayList<>();

        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;

        this.gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        this.accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetoSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //this.gameRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        this.geomagneticRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);

        System.out.println("AndroidSensorHelper.AndroidSensorHelper: initialized " + gravitySensor + accelerometerSensor);
        System.out.println("AndroidSensorHelper.AndroidSensorHelper: " + rotationSensor + /*gameRotationSensor +*/ geomagneticRotationSensor);
        System.out.println("AndroidSensorHelper.AndroidSensorHelper  " + gyroscopeSensor + magnetoSensor);

        if (gyroscopeSensor != null) {
            gyroscopeSensorEventListener = new GyroscopeSensorEventListener(gyroscopeSensor);
            sensorEventListeners.add(gyroscopeSensorEventListener);
            gyroscopeOrientationProvider = new GyroscopeOrientationProvider(gyroscopeSensorEventListener);
            orientationProviders.add(gyroscopeOrientationProvider);
        }
        if (gravitySensor != null) {
            GravitySensorEventListener gravitySensorEventListener = new GravitySensorEventListener(gravitySensor);
            sensorEventListeners.add(gravitySensorEventListener);
            gravityOrientationProvider = new GravityOrientationProvider(gravitySensorEventListener);
            orientationProviders.add(gravityOrientationProvider);
        }
        if (accelerometerSensor != null) {
            AccelerometerSensorEventListener accelerometerSensorEventListener = new AccelerometerSensorEventListener(accelerometerSensor);
            sensorEventListeners.add(accelerometerSensorEventListener);
            accelerometerOrientationProvider = new AccelerometerOrientationProvider(accelerometerSensorEventListener);
            orientationProviders.add(accelerometerOrientationProvider);
        }
        StringBuilder sb = new StringBuilder();
        for (OrientationProvider orientationProvider : orientationProviders)
            sb.append(orientationProvider.toString());
        System.out.println("AndroidSensorHelper.AndroidSensorHelper " + sb.toString());
        /*if (rotationSensor != null) {
            rotationVectorSensorEventListener = new RotationVectorSensorEventListener();
            //orientationProviders.add(rotationVectorSensorEventListener);
        }*/
        currentOrientationProvider = 0;

        resumeSensors();
    }

    @Override
    public void resumeSensors() {
        for (SensorEventListener listener : sensorEventListeners)
            if (listener != null)
                sensorManager.registerListener(listener, listener.getSensor(), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void pauseSensors() {
        for (SensorEventListener listener : sensorEventListeners)
            if (listener != null)
                sensorManager.unregisterListener(listener);
    }

    @Override
    public float[] getGravityDirectionVector() {
        return getCurrentOrientationProvider().getGravityDirectionVector();
    }

    @Override
    public float[] getVelocityVector() {
        return getCurrentOrientationProvider().getVelocityVector();
    }

    @Override
    public boolean hasJumped() {
        return (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE));// || rawGyroscopeVector[0] > 2);
    }

    private OrientationProvider getCurrentOrientationProvider() {
        return orientationProviders.get(currentOrientationProvider);
    }

    @Override
    public List<String> getOrientationProviders() {
        List<String> list = new ArrayList<>();
        for (OrientationProvider orientationProvider : orientationProviders) {
            list.add(orientationProvider.toString());
        }
        return list;
    }

    @Override
    public int getOrientationProvider() {
        return currentOrientationProvider;
    }

    @Override
    public void setOrientationProvider(int index) {
        currentOrientationProvider = index;
    }

    static void computeVelocity(float[] oldVector, float[] newVector, float dt, float[] ret) {
        for (int i = 0; i < newVector.length; i++) {
            ret[i] = (newVector[i] - oldVector[i]) / dt;
        }
    }

    static void applyRotationVectorChange(float[] prevR, float[] delta, float[] newR) {
        float[] prevRmatrix = new float[9];
        float[] deltaMatrix = new float[9];
        float[] newRmatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(prevRmatrix, prevR);
        SensorManager.getRotationMatrixFromVector(deltaMatrix, delta);
        matrixMatrixMultiplication(prevRmatrix, deltaMatrix, 3, 3, newRmatrix);
        rotationMatrixToVector(newRmatrix, newR);
    }

    static void matrixMatrixMultiplication(float[] A, float[] B, int row, int column, float[] C) {
        int rc = A.length / row;
        if (B.length != rc * column)
            throw new IllegalArgumentException("Wrong size of matrices");
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                float acc = 0.0f;
                for (int i = 0; i < rc; i++)
                    acc += A[r * rc + i] * B[i * rc + c];
                C[r * column + c] = acc;
            }
        }
    }

    static void rotationMatrixToVector(float[] matrix, float[] vector) {
        // vector : 0 is real, 1, 2, 3 is complex
        if (matrix.length == 9) {
            vector[3] = 0.5f * (float)(Math.sqrt(1 + matrix[0] + matrix[4] + matrix[8]));
            float inv4qr = 1.0f / (4*vector[0]);
            vector[0] = (matrix[7] - matrix[5]) * inv4qr;
            vector[1] = (matrix[2] - matrix[6]) * inv4qr;
            vector[2] = (matrix[3] - matrix[1]) * inv4qr;
        } else {
            vector[3] = 0.5f * (float)(Math.sqrt(1 + matrix[0] + matrix[5] + matrix[10]));
            float inv4qr = 1.0f / (4*vector[0]);
            vector[0] = (matrix[9] - matrix[6]) * inv4qr;
            vector[1] = (matrix[2] - matrix[8]) * inv4qr;
            vector[2] = (matrix[4] - matrix[1]) * inv4qr;
        }
    }

    static float[] matrixVectorMultiplication(float[] R, float[] v) {
        if (R.length == 9 && v.length == 3) {
            return new float[] {
                    R[0] * v[0] + R[1] * v[1] + R[2] * v[2],
                    R[3] * v[0] + R[4] * v[1] + R[5] * v[2],
                    R[6] * v[0] + R[7] * v[1] + R[8] * v[2]
            };
        } else {
            // TODO généraliser
            return new float[] {0.0f, 0.0f, 0.0f};
        }
    }

    static void normalizeVector(float[] rawVector, float[] normalizedVector) {
        float magnitude = 0.0f;
        for (int i = 0; i < rawVector.length; i++)
            magnitude += rawVector[i]*rawVector[i];
        magnitude = (float) Math.sqrt(magnitude);
        for (int i = 0; i < normalizedVector.length; i++)
            normalizedVector[i] = rawVector[i] / magnitude;
    }
}
