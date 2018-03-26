package be.ucl.lfsab1509.gravityrun.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.AndroidApplication;

import java.util.Arrays;

public class AndroidSensorHelper implements SensorHelper {

    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final long DELAY_JUMP = 800;
    private static final double JUMP_MIN_GYROSCOPE = 5.0 * Math.PI / 180.0;

    private OrientationSensorType orientationSensorType;
    private GyroscopeSensorType gyroscopeSensorType;
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private Sensor linearAccelerationSensor;
    private Sensor rotationSensor;
    private Sensor gyroscopeSensor;
    private GravitySensorEventListener gravitySensorEventListener;
    private LinearAccelerationSensorEventListener linearAccelerationSensorEventListener;
    private RotationSensorEventListener rotationSensorEventListener;
    private GyroscopeSensorEventListener gyroscopeSensorEventListener;
    // Raw values, immediately from the sensor
    private float[] rawGravityVector;
    private long lastGravityTimeStamp;
    private float[] rawGyroscopeVector;
    private long lastGyroscopeTimeStamp;
    private float[] rawLinearAcceleration;
    // Computed values, from other sensor data
    private float[] normalizedGravityVector;
    private float[] lastDeltaRotationVector;
    private float[] gyroscopeBasedRotationVector;
    private float[] normalizedGravityVelocityVector;
    //private long lastJumpTimestamp;

    private class GravitySensorEventListener implements SensorEventListener {
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
            if (event.values.length == 3) {
                float[] newGravityVector = new float[3];
                System.arraycopy(event.values, 0, newGravityVector, 0, event.values.length);
                float[] newNormalizedGravityVector = new float[2];
                normalizeVector(newGravityVector, newNormalizedGravityVector);
                float dt = event.timestamp - lastGravityTimeStamp; // in nanoseconds
                for (int i = 0; i < newNormalizedGravityVector.length; i++) {
                    normalizedGravityVelocityVector[i] = (newNormalizedGravityVector[i] - normalizedGravityVector[i]) / (dt * NS2S);
                }
                lastGravityTimeStamp = event.timestamp;
                System.arraycopy(newGravityVector, 0, rawGravityVector, 0, rawGravityVector.length);
                System.out.println("GravitySensorEventListener.onSensorChanged + " + dt + " " + Arrays.toString(normalizedGravityVelocityVector) + " " + Arrays.toString(newNormalizedGravityVector) + " " + Arrays.toString(normalizedGravityVector));
                System.arraycopy(newNormalizedGravityVector, 0, normalizedGravityVector, 0, newNormalizedGravityVector.length);
            } else {
                Log.d("GravityEventListener", "Wrong length of event.values");
            }
        }
    }

    private class LinearAccelerationSensorEventListener implements SensorEventListener {
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
             */
            if (event.values.length == 3) {
                System.arraycopy(event.values, 0, rawLinearAcceleration, 0, event.values.length);
            } else {
                Log.d("LineaccEventListener", "Wrong length of event.values");
            }
        }
    }

    private class RotationSensorEventListener implements SensorEventListener {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // Nothing to do in our case
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            //
        }
    }

    private class GyroscopeSensorEventListener implements SensorEventListener {
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
            if (event.values.length == 3) {
                System.arraycopy(event.values, 0, rawGyroscopeVector, 0, event.values.length);
                float dt = (event.timestamp - lastGyroscopeTimeStamp) / NS2S;
                float omegaX = event.values[0], omegaY = event.values[1], omegaZ = event.values[2];
                float omegaMagnitude = (float) (Math.sqrt(omegaX*omegaX + omegaY * omegaY + omegaZ * omegaZ));
                if (omegaMagnitude > 0.001f) {
                    omegaX /= omegaMagnitude;
                    omegaY /= omegaMagnitude;
                    omegaZ /= omegaMagnitude;
                }
                float thetaHalf = omegaMagnitude * dt / 2;
                float sinThetaHalf = (float) Math.sin(thetaHalf);
                float cosThetaHalf = (float) Math.cos(thetaHalf);
                lastDeltaRotationVector[0] = sinThetaHalf * omegaX;
                lastDeltaRotationVector[1] = sinThetaHalf * omegaY;
                lastDeltaRotationVector[2] = sinThetaHalf * omegaZ;
                lastDeltaRotationVector[3] = cosThetaHalf;
                float[] deltaRotationMatrix = new float[9];
                SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, lastDeltaRotationVector);
                gyroscopeBasedRotationVector = matrixVectorMultiplication(deltaRotationMatrix, gyroscopeBasedRotationVector);
                lastGyroscopeTimeStamp = event.timestamp;
            } else {
                Log.d("RotationEventListener", "Wrong length of event values");
            }
        }
    }

    public AndroidSensorHelper(AndroidApplication activity) {
        this.orientationSensorType = OrientationSensorType.GRAVITY;
        this.gyroscopeSensorType = GyroscopeSensorType.GYROSCOPE;

        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;

        this.gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        this.linearAccelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        this.rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        gravitySensorEventListener = new GravitySensorEventListener();
        linearAccelerationSensorEventListener = new LinearAccelerationSensorEventListener();
        rotationSensorEventListener = new RotationSensorEventListener();
        gyroscopeSensorEventListener = new GyroscopeSensorEventListener();

        rawGravityVector = new float[3];
        lastGravityTimeStamp = System.nanoTime();

        rawGyroscopeVector = new float[] {0.0f, 0.0f, 0.0f};
        lastDeltaRotationVector = new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        gyroscopeBasedRotationVector = new float[] {0.0f, 0.0f, 0.0f};
        lastGyroscopeTimeStamp = System.nanoTime();
        rawLinearAcceleration = new float[] {0.0f, 0.0f, 0.0f};
        normalizedGravityVelocityVector = new float[] {0.0f, 0.0f};
        normalizedGravityVector = new float[] {0.0f, 0.0f};

        resumeSensors();
    }

    private static float[] matrixVectorMultiplication(float[] R, float[] v) {
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

    private static void normalizeVector(float[] rawVector, float[] normalizedVector) {
        float magnitude = 0.0f;
        for (int i = 0; i < rawVector.length; i++)
            magnitude += rawVector[i]*rawVector[i];
        magnitude = (float) Math.sqrt(magnitude);
        for (int i = 0; i < normalizedVector.length; i++)
            normalizedVector[i] = rawVector[i] / magnitude;
    }

    @Override
    public void resumeSensors() {
        sensorManager.registerListener(gravitySensorEventListener, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(linearAccelerationSensorEventListener, linearAccelerationSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(rotationSensorEventListener, rotationSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(gyroscopeSensorEventListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void pauseSensors() {
        sensorManager.unregisterListener(gravitySensorEventListener);
        sensorManager.unregisterListener(linearAccelerationSensorEventListener);
        sensorManager.unregisterListener(rotationSensorEventListener);
        sensorManager.unregisterListener(gyroscopeSensorEventListener);
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
    public float getGyroscopeY() {
        return rawGyroscopeVector[1];
    }

    @Override
    public boolean hasJumped() {
        return (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE));// || rawGyroscopeVector[0] > 2);
        /*
        TODO vérifier si ça marche
        long currentTimestamp = System.currentTimeMillis();
        if ((currentTimestamp - lastJumpTimestamp) > DELAY_JUMP && Math.abs(rawGyroscopeVector[0]) > JUMP_MIN_GYROSCOPE) {
            lastJumpTimestamp = currentTimestamp;
            return true;
        } else {
            return false;
        }
        */
    }

    @Override
    public void setOrientationSensorType(OrientationSensorType type) {
        this.orientationSensorType = type;
    }

    @Override
    public void setGyroscopeSensorType(GyroscopeSensorType type) {
        this.gyroscopeSensorType = type;
    }

    public GyroscopeSensorType getGyroscopeSensorType() {
        return gyroscopeSensorType;
    }

    public OrientationSensorType getOrientationSensorType() {
        return orientationSensorType;
    }
}
