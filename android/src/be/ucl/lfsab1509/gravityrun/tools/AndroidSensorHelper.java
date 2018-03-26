package be.ucl.lfsab1509.gravityrun.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;

public class AndroidSensorHelper extends SensorHelper {

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
    private float[] gravityVector;
    private long lastGravityTimeStamp;
    private float[] gyroscopeVector;
    private long lastGyroscopeTimeStamp;
    private float[] lastDeltaRotationVector;
    private float[] gyroscopeBasedRotationVector;
    private float[] lastLinearAcceleration;
    private long lastJumpTimestamp;

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
                System.arraycopy(event.values, 0, gravityVector, 0, event.values.length);
                lastGravityTimeStamp = event.timestamp;
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
                System.arraycopy(event.values, 0, lastLinearAcceleration, 0, event.values.length);
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
                System.arraycopy(event.values, 0, gyroscopeVector, 0, event.values.length);
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

        gravityVector = new float[3];
        lastGravityTimeStamp = System.nanoTime();

        gyroscopeVector = new float[] {0.0f, 0.0f, 0.0f};
        lastDeltaRotationVector = new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        gyroscopeBasedRotationVector = new float[] {0.0f, 0.0f, 0.0f};
        lastGyroscopeTimeStamp = System.nanoTime();
        lastLinearAcceleration = new float[] {0.0f, 0.0f, 0.0f};

        resumeSensors();
    }

    public static float[] matrixVectorMultiplication(float[] R, float[] v) {
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
        float[] current = gravityVector;
        float gravityMagnitude = (float)Math.sqrt(current[0] * current[0] + current[1] * current[1] + current[2] * current[2]);
        ret[0] = -current[0] / gravityMagnitude;
        ret[1] = -current[1] / gravityMagnitude;
        return ret;
    }

    @Override
    public boolean hasJumped() {
        return (gyroscopeVector[0] > 2); // default implementation
        /*
        TODO vérifier si ça marche
        long currentTimestamp = System.currentTimeMillis();
        if ((currentTimestamp - lastJumpTimestamp) > DELAY_JUMP && Math.abs(gyroscopeVector[0]) > JUMP_MIN_GYROSCOPE) {
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
