package be.ucl.lfsab1509.gravityrun.tools;


import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;

import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;
import java.util.Set;

import be.ucl.lfsab1509.gravityrun.AndroidLauncher;
import be.ucl.lfsab1509.gravityrun.R;
import be.ucl.lfsab1509.gravityrun.screens.AbstractMultiPlayScreen;
import be.ucl.lfsab1509.gravityrun.screens.MultiPlayScreen;

public class BluetoothFragment extends BluetoothManager implements BluetoothConstants
{
    private AndroidBluetoothManager androidBluetoothManager;
    private LinkedList<BluetoothDevice> mDevices;
    private Activity activity;

    private static final String TAG = "BluetoothFragment";

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "start onReceive");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "found new device " + device.getName());

                if (device.getName() != null && !devicesNames.contains(device.getName(), false)) {
                    mDevices.add(device);
                    devicesNames.insert(devicesNames.size - 1, device.getName());
                    devicesAddress.add(device.getAddress());
                }
            }
            else {
                Log.i(TAG, "no devices found");
            }
        }
    };

    public BluetoothFragment (Activity activity, Handler handler) {
        this.activity = activity;
        devicesNames = new Array<>();
        devicesAddress = new Array<>();
        mDevices = new LinkedList<>();
        androidBluetoothManager = new AndroidBluetoothManager(activity, handler);
    }

    @Override
    public void disconnect() {
        androidBluetoothManager.stop();
    }

    @Override
    public boolean supportDeviceBluetooth() {
        return androidBluetoothManager.supportDeviceBluetooth();
    }

    @Override
    public void enableBluetooth() {
        androidBluetoothManager.enableBluetooth();
    }

    @Override
    public void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = androidBluetoothManager.getBluetoothAdapter().getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                mDevices.add(device);
                devicesNames.add(device.getName());
                devicesAddress.add(device.getAddress());
            }
        }
    }

    @Override
    public void write(String str) {
        androidBluetoothManager.write(str.getBytes());
    }

    @Override
    public boolean isConnected() {
        return (androidBluetoothManager.getState() == STATE_CONNECTED);
    }

    @Override
    public void discoverDevices() {
        devicesNames.clear();
        devicesNames.add(activity.getString(R.string.searching));

        devicesAddress.clear();
        mDevices.clear();
        androidBluetoothManager.stop();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        androidBluetoothManager.getmCurrentActivity().registerReceiver(mReceiver, filter);

        androidBluetoothManager.discoverDevices();
    }

    @Override
    public void startHost() {
        androidBluetoothManager.stop();
        androidBluetoothManager.enableDiscoveribility();
        androidBluetoothManager.startListening();
    }

    @Override
    public void setMultiPlayScreen(AbstractMultiPlayScreen multiPlayScreen) {
        AndroidLauncher.multiPlayScreen = multiPlayScreen;
    }

    @Override
    public void connect(int devicePosition) {
        if(!mDevices.isEmpty()) {
            androidBluetoothManager.connect(mDevices.get(devicePosition));
        }
    }

    public AndroidBluetoothManager getAndroidBluetoothManager() {
        return androidBluetoothManager;
    }

    @Override
    public boolean isHost() {
        return androidBluetoothManager.isHost();
    }
}