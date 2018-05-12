package be.ucl.lfsab1509.gravityrun.tools.sensors;

import android.hardware.SensorManager;

public class GyroscopeOrientationProvider implements OrientationProvider {
    private final float MIN_MAGNITUDE;
    private boolean validTimestamp = false;
    private float[] lastGyroscopeVector;
    private float[] lastDeltaRotationVector;
    //private float[] gyroscopeBasedRotationVector; // 4
    private float[] gyroscopeBasedRotationMatrix; // 9
    private long lastGyroscopeTimeStamp;
    private GyroscopeSensorEventListener gyroscopeSensorEventListener;

    GyroscopeOrientationProvider(GyroscopeSensorEventListener gyroscopeSensorEventListener) {
        this.gyroscopeSensorEventListener = gyroscopeSensorEventListener;
        MIN_MAGNITUDE = Math.max(gyroscopeSensorEventListener.getSensor().getResolution(), 0.0001f);
        gyroscopeSensorEventListener.subscribe(this);
        lastGyroscopeVector = new float[] {0.0f, 0.0f, 0.0f};
        lastGyroscopeTimeStamp = System.nanoTime();
        lastDeltaRotationVector = new float[] {0.0f, 0.0f, 0.0f, 0.0f};
        //gyroscopeBasedRotationVector = new float[] {0.0f, 0.0f, 1.0f, 0.0f};
        gyroscopeBasedRotationMatrix = new float[] {
                1f, 0f, 0f,
                0f, 1f, 0f,
                0f, 0f, 1f
        };
    }

    @Override
    public float[] getGravityDirectionVector() {
        float[] ret = new float[3];
        ret[0] = gyroscopeBasedRotationMatrix[2];
        ret[1] = gyroscopeBasedRotationMatrix[5];
        ret[2] = gyroscopeBasedRotationMatrix[8];
        return ret;
    }

    @Override
    public float[] getVelocityVector() {
        float[] ret = new float[2];
        ret[0] = lastGyroscopeVector[1];
        ret[1] = lastGyroscopeVector[0];
        return ret;
    }

    @Override
    public void resetOrientation() {
        //System.arraycopy(NO_ROTATION_VECTOR, 0, gyroscopeBasedRotationVector, 0, gyroscopeBasedRotationVector.length);
        System.arraycopy(NO_ROTATION_MATRIX, 0, gyroscopeBasedRotationMatrix, 0, gyroscopeBasedRotationMatrix.length);
    }

    @Override
    public void update() {
        float[] currentGyroscopeVector = gyroscopeSensorEventListener.getRawGyroscopeVector();
        long currentTimestamp = gyroscopeSensorEventListener.getLastGyroscopeTimeStamp();
        System.arraycopy(currentGyroscopeVector, 0, lastGyroscopeVector, 0, currentGyroscopeVector.length);
        // See https://developer.android.com/guide/topics/sensors/sensors_motion for the original code
        if (!validTimestamp) {
            lastGyroscopeTimeStamp = currentTimestamp; // at least it does nothing more than registering the new value...
            validTimestamp = true;
        }
        float dt = (currentTimestamp - lastGyroscopeTimeStamp) * NS2S;
        float omegaX = currentGyroscopeVector[0], omegaY = currentGyroscopeVector[1], omegaZ = currentGyroscopeVector[2];
        float omegaMagnitude = (float) (Math.sqrt(omegaX*omegaX + omegaY * omegaY + omegaZ * omegaZ));
        if (omegaMagnitude > MIN_MAGNITUDE) {
            omegaX /= omegaMagnitude;
            omegaY /= omegaMagnitude;
            omegaZ /= omegaMagnitude;
        }
        float thetaHalf = omegaMagnitude * dt / 2;
        float sinThetaHalf = (float) Math.sin(thetaHalf);
        float cosThetaHalf = (float) Math.cos(thetaHalf);
        //System.out.println("GyroscopeOrientationProvider.update : " + omegaX + " " + omegaY + " " + omegaZ + " " + omegaMagnitude + " " + thetaHalf + " " + sinThetaHalf + " " + cosThetaHalf + " " + dt);
        lastDeltaRotationVector[0] = sinThetaHalf * omegaX;
        lastDeltaRotationVector[1] = sinThetaHalf * omegaY;
        lastDeltaRotationVector[2] = sinThetaHalf * omegaZ;
        lastDeltaRotationVector[3] = cosThetaHalf;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, lastDeltaRotationVector);
        //System.out.println("GyroscopeOrientationProvider.update : " + Arrays.toString(lastDeltaRotationVector) + " " + Arrays.toString(deltaRotationMatrix) + " " + Arrays.toString(gyroscopeBasedRotationMatrix));
        AndroidSensorHelper.matrixMatrixMultiplication(gyroscopeBasedRotationMatrix, deltaRotationMatrix, 3, 3, gyroscopeBasedRotationMatrix);
        //AndroidSensorHelper.rotationMatrixToVector(gyroscopeBasedRotationMatrix, gyroscopeBasedRotationVector);
        lastGyroscopeTimeStamp = currentTimestamp;
        //System.out.println("GyroscopeOrientationProvider.update " + Arrays.toString(gyroscopeBasedRotationMatrix) /*+ "   " + Arrays.toString(gyroscopeBasedRotationVector)*/);
    }

    @Override
    public String toString() {
        return "Gyroscope";
    }
}
