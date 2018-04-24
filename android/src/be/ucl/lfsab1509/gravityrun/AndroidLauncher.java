package be.ucl.lfsab1509.gravityrun;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import be.ucl.lfsab1509.gravityrun.gpgs.Gpgs;
import be.ucl.lfsab1509.gravityrun.gpgs.Gpgs2;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication {

    private Gpgs2 gpgs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = true;
        config.useCompass = false;
        config.useGyroscope = true;

        Fabric.with(this, new Crashlytics());

        gpgs = new Gpgs2(this);

        GravityRun game = new GravityRun();
        game.gpgs = gpgs;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initialize(game, config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        gpgs.onActivityResult(requestCode, resultCode, data);
    }

}