package be.ucl.lfsab1509.gravityrun.tools;

public interface BluetoothConstants {
    String DEVICE_NAME = "device_name";
    String TOAST = "toast";
    String MY_UUID = "DDD59690-4FBA-11E2-BCFD-0800200C9A66";

    int REQUEST_ENABLE_BT = 3;

    // Constants that indicate the current connection state
    int STATE_CONNECTED = 3;
    int STATE_CONNECTING = 2;
    int STATE_LISTEN = 1;
    int STATE_NONE = 0;

    // Message types sent from the BluetoothChatService Handler
    int MESSAGE_STATE_CHANGE = 1;
    int MESSAGE_READ = 2;
    int MESSAGE_DEVICE_NAME = 4;
    int MESSAGE_TOAST = 5;
}