package be.ucl.lfsab1509.gravityrun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.desktop.tools.DesktopSensorHelper;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 1824;
		config.overrideDensity = 2 * 160;
		config.title = GravityRun.TITLE;
		config.overrideDensity = 160;
		new LwjglApplication(new GravityRun(new DesktopSensorHelper()), config);
	}
}
