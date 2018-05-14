package be.ucl.lfsab1509.gravityrun.tools;

import java.util.List;

public interface SensorHelper {

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
    float[] getGravityDirectionVector();

    /**
     * Retourne un vecteur de taille 2 représentant la vitesse du changement du vecteur retourné par {@link SensorHelper#getGravityDirectionVector()}.
     * La valeur en 0 est positive si le téléphone tourne dans le sens horlogique par rapport à un utilisateur placé en dessous du téléphone,
     * bouton home visible et situé plus bas que le haut parleur de l'appareil (bref, la position normale pour utiliser un téléphone).
     * La valeur en 1 est positive si le téléphone s'incline "en tombant" (le bouton home se rapproche, le haut parleur s'éloigne).
     *
     * @return Voir la description...
     */
    float[] getVelocityVector();

    /**
     * Retourne true si l'utilisateur a fait sauté la bille dans les quelques millisecondes précédentes, false sinon.
     * Lors d'un saut, seul le premier appel à cette fonction retournera true, les suivants retourneront nécessairement false, jusqu'au prochain saut.
     * Il est possible qu'un délai soit présent entre le saut détecté et le prochain saut qui pourra être détecté et qui sera reporté.
     *
     * @return true si un saut a été détecté et n'a pas encore été rapporté, false sinon.
     */
    boolean hasJumped();

    /**
     * Met les différents capteurs utilisés par l'activité en pause, afin d'économiser les ressources.
     */
    void pauseSensors();

    /**
     * Rétablit les différents capteurs utilisés par l'activité.
     */
    void resumeSensors();

    List<String> getOrientationProviders();

    int getOrientationProvider();

    void setOrientationProvider(int index);

}
