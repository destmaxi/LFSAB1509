package be.ucl.lfsab1509.gravityrun.desktop;

import be.ucl.lfsab1509.gravityrun.GravityRun;

import be.ucl.lfsab1509.gravityrun.tools.GpgsMappers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.golfgl.gdxgamesvcs.GpgsClient;
import de.golfgl.gdxgamesvcs.IGameServiceIdMapper;

public class DesktopLauncher {

	public static void main (String[] arg) {
	    GravityRun game = new GravityRun();
//        game.gsClient = initGpgsClient();

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 1824;
		config.overrideDensity = 2 * 160;
		config.title = GravityRun.TITLE;
		config.width = 1200;

		new LwjglApplication(game, config);
	}

    private static GpgsClient initGpgsClient() {
        GpgsClient gpgsClient;

        gpgsClient = new GpgsClient() {
            @Override
            public boolean submitEvent(String eventId, int increment) {
                return super.submitEvent(GpgsMappers.mapToGpgsEvent(eventId), increment);
            }
        };
        gpgsClient.setGpgsAchievementIdMapper(new IGameServiceIdMapper<String>() {
            @Override
            public String mapToGsId(String independantId) {
                return GpgsMappers.mapToGpgsAchievement(independantId);
            }
        });
        gpgsClient.setGpgsLeaderboardIdMapper(new IGameServiceIdMapper<String>() {
            @Override
            public String mapToGsId(String independantId) {
                return GpgsMappers.mapToGpgsLeaderBoard(independantId);
            }
        });
        gpgsClient.initialize(GravityRun.TITLE, Gdx.files.internal("gpgs-client_secret.json"), false);

        return gpgsClient;
    }

}
