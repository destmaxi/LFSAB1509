package be.ucl.lfsab1509.gravityrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import be.ucl.lfsab1509.gravityrun.gpgs.MyGpgsClient;
import be.ucl.lfsab1509.gravityrun.tools.GpgsMappers;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import de.golfgl.gdxgamesvcs.GpgsClient;
import de.golfgl.gdxgamesvcs.IGameServiceIdMapper;
import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication {

    private GpgsClient gpgsClient;
    private MyGpgsClient myGpgsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = false;
        config.useGyroscope = true;

        Fabric.with(this, new Crashlytics());

        myGpgsClient = new MyGpgsClient(this);

        initGpgsClient();

        GravityRun game = new GravityRun();
        game.gsClient = myGpgsClient;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize(game, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        gpgsClient.onGpgsActivityResult(requestCode, resultCode, data);
        myGpgsClient.onActivityResult(requestCode, resultCode, data);
    }

    private void initGpgsClient() {
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
        gpgsClient.initialize(this, false);
    }

}