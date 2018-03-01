package be.ucl.lfsab1509.gravityrun;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import be.ucl.lfsab1509.gravityrun.Gravity Run;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;  //default is false

		config.useAccelerometer = false;
		config.useCompass = false;
		initialize(new Gravity Run(), config);
	}
}
