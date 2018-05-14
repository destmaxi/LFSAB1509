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

    private List<SensorEventListener> sensorEventListeners;
    private List<OrientationProvider> orientationProviders;
    private int currentOrientationProvider;

    private GyroscopeSensorEventListener gyroscopeSensorEventListener;

    private GravityOrientationProvider gravityOrientationProvider;
    private GyroscopeOrientationProvider gyroscopeOrientationProvider;
    private AccelerometerOrientationProvider accelerometerOrientationProvider;

    public AndroidSensorHelper(AndroidApplication activity) {
        this.orientationProviders = new ArrayList<>();
        this.sensorEventListeners = new ArrayList<>();

        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;

        Sensor gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Sensor rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR); // requires API 18
        Sensor gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnetoSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        //Sensor gameRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        Sensor geomagneticRotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR); // requires API 19

        System.out.println("AndroidSensorHelper.AndroidSensorHelper: initialized " + gravitySensor + accelerometerSensor);
        System.out.println("AndroidSensorHelper.AndroidSensorHelper: " + rotationSensor + /*gameRotationSensor +*/ geomagneticRotationSensor);
        System.out.println("AndroidSensorHelper.AndroidSensorHelper  " + gyroscopeSensor + magnetoSensor);

        if (gyroscopeSensor != null) {
            gyroscopeSensorEventListener = new GyroscopeSensorEventListener(sensorManager, gyroscopeSensor);
            sensorEventListeners.add(gyroscopeSensorEventListener);
            gyroscopeOrientationProvider = new GyroscopeOrientationProvider(gyroscopeSensorEventListener);
            orientationProviders.add(gyroscopeOrientationProvider);
        }
        if (gravitySensor != null) {
            GravitySensorEventListener gravitySensorEventListener = new GravitySensorEventListener(sensorManager, gravitySensor);
            sensorEventListeners.add(gravitySensorEventListener);
            gravityOrientationProvider = new GravityOrientationProvider(gravitySensorEventListener);
            orientationProviders.add(gravityOrientationProvider);
        }
        if (accelerometerSensor != null) {
            AccelerometerSensorEventListener accelerometerSensorEventListener = new AccelerometerSensorEventListener(sensorManager, accelerometerSensor);
            sensorEventListeners.add(accelerometerSensorEventListener);
            accelerometerOrientationProvider = new AccelerometerOrientationProvider(accelerometerSensorEventListener);
            orientationProviders.add(accelerometerOrientationProvider);
        }
        StringBuilder sb = new StringBuilder();
        for (OrientationProvider orientationProvider : orientationProviders)
            sb.append(orientationProvider.toString());
        System.out.println("AndroidSensorHelper.AndroidSensorHelper " + sb.toString());

        // Empty OrientationProvider, to prevent NullPointerException and IndexOutOfRangeException
        orientationProviders.add(new DummyOrientationProvider());
        currentOrientationProvider = 0;

        resumeSensors();
    }

    @Override
    public void resumeSensors() {
        getCurrentOrientationProvider().resumeSensors();
    }

    @Override
    public void pauseSensors() {
        getCurrentOrientationProvider().pauseSensors();
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
        return (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE));
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
        pauseSensors();
        currentOrientationProvider = index;
        resumeSensors();
    }

    static void computeVelocity(float[] oldVector, float[] newVector, float dt, float[] ret) {
        for (int i = 0; i < newVector.length; i++) {
            ret[i] = (newVector[i] - oldVector[i]) / dt;
        }
    }

    static void matrixMatrixMultiplication(float[] A, float[] B, float[] C) {
        if (B.length != 9 || A.length != 9)
            throw new IllegalArgumentException("Wrong size of matrices");
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                float acc = 0.0f;
                for (int i = 0; i < 3; i++)
                    acc += A[3 * r + i] * B[3 * i + c];
                C[3 * r + c] = acc;
            }
        }
    }

    static void normalizeVector(float[] rawVector, float[] normalizedVector) {
        float magnitude = 0.0f;
        for (float aRawVector : rawVector)
            magnitude += aRawVector * aRawVector;
        magnitude = (float) Math.sqrt(magnitude);
        for (int i = 0; i < normalizedVector.length; i++)
            normalizedVector[i] = rawVector[i] / magnitude;
    }
}
