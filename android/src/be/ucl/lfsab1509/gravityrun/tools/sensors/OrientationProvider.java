package be.ucl.lfsab1509.gravityrun.tools.sensors;

interface OrientationProvider {
    float NS2S = 1e-9f;
    float[] NO_ROTATION_MATRIX = {
            1f, 0f, 0f,
            0f, 1f, 0f,
            0f, 0f, 1f
    };
    float[] NO_ROTATION_VECTOR = {0f, 0f, 1f, 0f};

    float[] getGravityDirectionVector();

    float[] getVelocityVector();

    void pauseSensors();

    void resetOrientation();

    void resumeSensors();

    void update();
}
