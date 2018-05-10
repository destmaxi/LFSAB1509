package be.ucl.lfsab1509.gravityrun.tools;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.Log;
import be.ucl.lfsab1509.gravityrun.AndroidLauncher;
import be.ucl.lfsab1509.gravityrun.R;
import be.ucl.lfsab1509.gravityrun.screens.AbstractMultiPlayScreen;
import com.badlogic.gdx.utils.Array;

import java.util.LinkedList;
import java.util.Set;

public class BluetoothFragment extends BluetoothManager implements BluetoothConstants {

    private static final String TAG = "BluetoothFragment";

    private AndroidLauncher activity;
    private AndroidBluetoothManager androidBluetoothManager;
    private LinkedList<BluetoothDevice> mDevices;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "start onReceive");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i(TAG, "found new device " + device.getName());

                if (device.getName() != null && !devicesNames.contains(device.getName(), false)) {
                    mDevices.add(device);
                    devicesNames.insert(devicesNames.size - 1, device.getName());
                }
            } else
                Log.i(TAG, "no devices found");
        }
    };

    public BluetoothFragment(AndroidLauncher activity, Handler handler) {
        this.activity = activity;
        androidBluetoothManager = new AndroidBluetoothManager(activity, handler);
        devicesNames = new Array<>();
        mDevices = new LinkedList<>();
    }

    @Override
    public void connect(int devicePosition) {
        if (!mDevices.isEmpty()) {
            androidBluetoothManager.connect(mDevices.get(devicePosition));
        }
    }

    @Override
    public void disconnect() {
        androidBluetoothManager.stop();
    }

    @Override
    public void discoverDevices() {
        devicesNames.clear();
        devicesNames.add(activity.getString(R.string.searching));

        androidBluetoothManager.stop();
        mDevices.clear();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        androidBluetoothManager.getmCurrentActivity().registerReceiver(mReceiver, filter);

        androidBluetoothManager.discoverDevices();
    }

    @Override
    public void enableBluetooth() {
        androidBluetoothManager.enableBluetooth();
    }

    @Override
    public void getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = androidBluetoothManager.getBluetoothAdapter().getBondedDevices();

        if (pairedDevices.size() > 0)
            for (BluetoothDevice device : pairedDevices) {
                mDevices.add(device);
                devicesNames.add(device.getName());
            }
    }

    @Override
    public boolean isConnected() {
        return AndroidBluetoothManager.getState() == STATE_CONNECTED;
    }

    @Override
    public boolean isHost() {
        return androidBluetoothManager.isHost();
    }

    @Override
    public void setMultiPlayScreen(AbstractMultiPlayScreen multiPlayScreen) {
        activity.multiPlayScreen = multiPlayScreen;
    }

    @Override
    public void startHost() {
        androidBluetoothManager.stop();
        androidBluetoothManager.enableDiscoveribility();
        androidBluetoothManager.startListening();
    }

    @Override
    public boolean supportDeviceBluetooth() {
        return androidBluetoothManager.supportDeviceBluetooth();
    }

    @Override
    public void write(String str) {
        androidBluetoothManager.write(str.getBytes());
    }

}