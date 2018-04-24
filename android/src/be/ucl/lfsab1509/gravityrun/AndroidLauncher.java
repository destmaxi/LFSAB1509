package be.ucl.lfsab1509.gravityrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import be.ucl.lfsab1509.gravityrun.gpgs.Gpgs;
import be.ucl.lfsab1509.gravityrun.gpgs.GpgsMultiplayer;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication {

    private Gpgs gpgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = false;
        config.useGyroscope = true;

        Fabric.with(this, new Crashlytics());

        final GravityRun game = new GravityRun();
        gpgs = new Gpgs(this, new GpgsMultiplayer.ErrorCallback() {
            @Override
            public void error(String message) {
                game.errorMessage(message);
            }
        });

        game.gpgs = gpgs;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize(game, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        gpgs.onActivityResult(requestCode, resultCode, data);
    }

}