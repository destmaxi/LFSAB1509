package be.ucl.lfsab1509.gravityrun.tools;

import be.ucl.lfsab1509.gravityrun.screens.AbstractMultiPlayScreen;
import com.badlogic.gdx.utils.Array;

public abstract class BluetoothManager {

    public static Array<String> devicesNames;

    public abstract void connect(int devicePosition);

    public abstract void disconnect();

    public abstract void discoverDevices();

    public abstract void enableBluetooth();

    public abstract void getPairedDevices();

    public abstract boolean isConnected();

    public abstract boolean isHost();

    public abstract void setMultiPlayScreen(AbstractMultiPlayScreen multiPlayScreen);

    public abstract void startHost();

    public abstract boolean supportDeviceBluetooth();

    public abstract void write(String string);

}