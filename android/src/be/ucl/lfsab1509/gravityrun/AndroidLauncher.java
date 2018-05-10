package be.ucl.lfsab1509.gravityrun;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.crashlytics.android.Crashlytics;

import be.ucl.lfsab1509.gravityrun.screens.AbstractMultiPlayScreen;
import be.ucl.lfsab1509.gravityrun.screens.MultiPlayFirstModeScreen;
import be.ucl.lfsab1509.gravityrun.tools.AndroidBluetoothManager;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothConstants;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothFragment;
import io.fabric.sdk.android.Fabric;

public class AndroidLauncher extends AndroidApplication {
    public static AbstractMultiPlayScreen multiPlayScreen;
    public static AndroidLauncher instance;
    public static GravityRun gravityRun;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case BluetoothConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    multiPlayScreen.incomingMessage(readMessage);
                    break;

                case BluetoothConstants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    CharSequence connectedDevice = "Connected to " + msg.getData().getString(BluetoothConstants.DEVICE_NAME);
                    Toast.makeText(instance, connectedDevice, Toast.LENGTH_SHORT).show();
                    break;

                case BluetoothConstants.MESSAGE_STATE_CHANGE:
                    if (AndroidBluetoothManager.getState() == BluetoothConstants.STATE_NONE) {
                        multiPlayScreen.onDisconnect();
                    }
                    break;

                case BluetoothConstants.MESSAGE_TOAST:
                    CharSequence content = msg.getData().getString(BluetoothConstants.TOAST);
                    Toast.makeText(instance, content, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        AndroidApplicationConfiguration config;
        config = new AndroidApplicationConfiguration();
        config.useGyroscope = true;
        config.useAccelerometer = true;
        config.useCompass = false;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        BluetoothFragment bluetoothFragment = new BluetoothFragment(this, handler);
        gravityRun = new GravityRun(bluetoothFragment);
        multiPlayScreen = new MultiPlayFirstModeScreen(gravityRun, false);
        initialize(gravityRun, config);
    }

    @Override
    protected void onDestroy() {
        System.out.println("Activity going to dispose");
        super.onDestroy();
        System.out.println("Activity disposed");
    }

}