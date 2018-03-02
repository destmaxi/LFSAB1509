package be.ucl.lfsab1509.gravityrun;

import android.os.Bundle;
import android.view.WindowManager;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config;
		config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;  //default is false

		config.useAccelerometer = false;
		config.useCompass = false;
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initialize(new GravityRun(), config);
	}
}