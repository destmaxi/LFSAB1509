package be.ucl.lfsab1509.gravityrun.desktop;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] arg) {
        GravityRun game = new GravityRun();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 1824;
        config.overrideDensity = 2 * 160;
        config.title = GravityRun.TITLE;
        config.width = 1200;

        new LwjglApplication(game, config);
    }

}
