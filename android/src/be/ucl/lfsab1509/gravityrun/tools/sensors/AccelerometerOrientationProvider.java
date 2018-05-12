package be.ucl.lfsab1509.gravityrun.tools.sensors;

class AccelerometerOrientationProvider extends GravityOrientationProvider {
    private static float alpha = 0.75f;

    private long lastAccelerometerTimestamp;
    private float[] rawAccelerometerVector;
    private float[] gravityAcceleration;
    private AccelerometerSensorEventListener accelerometerSensorEventListener;

    AccelerometerOrientationProvider(AccelerometerSensorEventListener accelerometerSensorEventListener) {
        super();
        this.accelerometerSensorEventListener = accelerometerSensorEventListener;
        this.gravityAcceleration = new float[3];
        this.rawAccelerometerVector = new float[3];
        accelerometerSensorEventListener.subscribe(this);
    }

    @Override
    public void pauseSensors() {
        accelerometerSensorEventListener.pause();
    }

    @Override
    public void resetOrientation() {
        // nothing to do special
        super.resetOrientation();
    }

    @Override
    public void resumeSensors() {
        accelerometerSensorEventListener.resume();
    }

    @Override
    public void update() {
        float[] currentAccelerometer = accelerometerSensorEventListener.getRawAccelerationVector();
        System.arraycopy(currentAccelerometer, 0, rawAccelerometerVector, 0, rawAccelerometerVector.length);
        long currentTimestamp = accelerometerSensorEventListener.getLastAccelerationTimestamp();
        for (int i = 0; i < 3; i++)
            gravityAcceleration[i] = alpha * gravityAcceleration[i] + (1 - alpha) * currentAccelerometer[i];
        //System.out.println("AccelerometerOrientationProvider.update : calling updateValues " + Arrays.toString(gravityAcceleration) + Arrays.toString(currentAccelerometer) + " " + currentTimestamp);
        updateValues(gravityAcceleration, currentTimestamp);
    }

    @Override
    public String toString() {
        return "Accelerometer";
    }
}
