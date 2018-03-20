package be.ucl.lfsab1509.gravityrun.tools;

public abstract class SensorHelper {
    public static SensorHelper MAIN = null;

    /**
     * Choix du capteur pour déterminer l'orientation de l'appareil.
     *
     * - ORIENTATION : utilisation des capteurs liés à l'orientation, en particulier le capteur du rotation vector et de la rotation matrix.
     * - GRAVITY : utilisation du capteur déterminant l'orientation de la gravité, qui lui-même tire parti de l'accéléromètre, du magnétomètre et éventuellement du gyroscope.
     */
    public enum OrientationSensorType {
        ORIENTATION,
        GRAVITY;
    }

    /**
     * Choix du capteur pour la mesure de l'accélération angulaire, pour le gyroscope.
     *
     * - GYROSCOPE : utiliser directement les données du gyroscope. Le gyroscope peut être absent sur certaines plateformes, et sa précision/calibration peut varier.
     * - ORIENTATION_DERIVED : utiliser les données du capteur pour l'orientation, en dérivant les données.
     */
    public enum GyroscopeSensorType {
        GYROSCOPE,
        ORIENTATION_DERIVED;
    }

    /**
     * Met les différents capteurs utilisés par l'activité en pause, afin d'économiser les ressources.
     */
    public abstract void pauseSensors();

    /**
     * Rétablit les différents capteurs utilisés par l'activité.
     */
    public abstract void resumeSensors();

    public abstract float getYGyroscope();

    /**
     * Retourne un vecteur de taille 2 représentant la direction de la gravité par rapport à l'appareil.
     * La valeur en 0 est l'accélération normalisée dans l'axe x (horizontal vers la droite pour la majorité des devices),
     * la valeur en 1 est dans l'axe y (vertical vers le haut).
     * Les vecteurs sont normalisés par rapport à l'accélération gravitationnelle mesurée.
     * Par conséquent, la norme de ce vecteur n'est pas nécessairement de 1 ; elle vaut 0
     * si l'appareil est à plat, et 1 si la gravité est dans le plan de l'écran.
     *
     * @return le vecteur décrit ci-dessus.
     */
    public abstract float[] getGravityDirectionVector();

    public abstract void setOrientationSensorType(OrientationSensorType type);

    public abstract void setGyroscopeSensorType(GyroscopeSensorType type);
}
