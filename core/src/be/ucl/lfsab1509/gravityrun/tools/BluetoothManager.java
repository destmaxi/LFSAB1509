package be.ucl.lfsab1509.gravityrun.tools;

import com.badlogic.gdx.utils.Array;

import be.ucl.lfsab1509.gravityrun.screens.AbstractMultiPlayScreen;
import be.ucl.lfsab1509.gravityrun.screens.MultiPlayScreen;

public abstract class BluetoothManager {

    public static BluetoothManager bluetoothManager;
    public static Array<String> devicesNames;
    public static Array<String> devicesAddress;

    public abstract void write(String string);

    public abstract boolean isConnected();

    public abstract void disconnect();

    public abstract void discoverDevices();

    public abstract void startHost();

    public abstract void connect(int devicePosition);

    public abstract void enableBluetooth();

    public abstract void getPairedDevices();

    public abstract boolean supportDeviceBluetooth();

    public abstract void setMultiPlayScreen(AbstractMultiPlayScreen multiPlayScreen);

    public abstract boolean isHost();
}