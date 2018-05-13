package be.ucl.lfsab1509.gravityrun.tools;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class AndroidBluetoothManager implements BluetoothConstants {

    private AcceptThread mAcceptThread;
    private final Activity mCurrentActivity;
    private BluetoothAdapter bluetoothAdapter;
    private boolean isHost;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private final Handler mHandler;
    private static int mState;

    private static final String TAG = "AndroidBluetoothManager";

    AndroidBluetoothManager(Activity activity, Handler handler) {
        super();
        mCurrentActivity = activity;
        mHandler = handler;
        mState = STATE_NONE;
    }

    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Log.i(TAG, "checking permission");
            int permissionCheck = mCurrentActivity.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += mCurrentActivity.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0)
                mCurrentActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        } else
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
    }

    private void closeServerSocket(BluetoothServerSocket serverSocket) {
        try {
            serverSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "error while closing socket");
        }
    }

    private void closeSocket(BluetoothSocket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "error while closing socket");
        }
    }

    synchronized void connect(BluetoothDevice device) {
        isHost = false;

        if (mState == STATE_CONNECTING && mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    private synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        Message msg = mHandler.obtainMessage(MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    private void connectionFailed() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
    }

    private void connectionLost() {
        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(TOAST, "Device connection was lost");
        stop();
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        mState = STATE_NONE;
    }

    void discoverDevices() {
        if (bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();

        checkBTPermissions();

        bluetoothAdapter.startDiscovery();
    }

    void enableBluetooth() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mCurrentActivity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    void enableDiscoveribility() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 60);
        mCurrentActivity.startActivity(discoverableIntent);
    }

    BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    Activity getmCurrentActivity() {
        return mCurrentActivity;
    }

    public static synchronized int getState() {
        return mState;
    }

    boolean isHost() {
        return this.isHost;
    }

    private synchronized void setState(int state) {
        mState = state;
        mHandler.obtainMessage(MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    synchronized void startListening() {
        Log.d(TAG, "start listListening");

        isHost = true;

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to listen on a BluetoothServerSocket
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

        setState(STATE_LISTEN);
    }

    synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        setState(STATE_NONE);
    }

    boolean supportDeviceBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        return bluetoothAdapter != null;
    }

    void write(byte[] out) {
        // Create temporary object
        ConnectedThread connectedThread;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED) return;
            connectedThread = mConnectedThread;
        }
        // Perform the write unsynchronized
        connectedThread.write(out);
    }

    private class AcceptThread extends Thread {

        private final BluetoothServerSocket mmServerSocket;

        AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {

                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(
                        "GravityRun",
                        UUID.fromString(MY_UUID));

            } catch (IOException e) {
                Log.e(TAG, "accept() failed", e);
            }
            mmServerSocket = tmp;
        }

        void cancel() {
            Log.d(TAG, "cancel " + this);
            closeServerSocket(mmServerSocket);
        }

        private void checkState(BluetoothSocket socket) {
            switch (mState) {
                case STATE_LISTEN:
                case STATE_CONNECTING:
                    // Situation normal. Start the connected thread.
                    Log.i(TAG, "Accept Connecting");
                    connected(socket, socket.getRemoteDevice());
                    break;
                case STATE_NONE:
                case STATE_CONNECTED:
                    closeSocket(socket);
                    break;
            }
        }

        public void run() {
            BluetoothSocket socket;
            while (mState != STATE_CONNECTED) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                if (socket != null) {
                    Log.i(TAG, "Connection accepted :" + mState);

                    synchronized (AndroidBluetoothManager.this) {
                        checkState(socket);
                    }
                    closeServerSocket(mmServerSocket);
                    break;
                }
            }
            Log.i(TAG, "END mAcceptThread");
        }

    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "error while getting input/output-stream of socket");
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        void cancel() {
            closeSocket(mmSocket);
        }

        private boolean readByffer(byte[] buffer) {
            try {
                int bytes = mmInStream.read(buffer);
                // Send the obtained bytes to the UI Activity
                mHandler.obtainMessage(BluetoothConstants.MESSAGE_READ, bytes, -1, buffer).sendToTarget();

            } catch (IOException e) {
                connectionLost();
                return false;
            }
            return true;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];

            while (true)
                if (!mHandler.hasMessages(MESSAGE_READ) && !readByffer(buffer))
                    break;
        }

        void write(byte[] buffer) {
            try {
//                for (byte b : buffer)
                mmOutStream.write(buffer);

            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }


    }

    private class ConnectThread extends Thread {

        private final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;

        ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
            } catch (IOException e) {
                Log.e(TAG, "error while creating socket");
            }
            mmSocket = tmp;
        }

        void cancel() {
            closeSocket(mmSocket);
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");

            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                Log.e(TAG, "couldn't connect socket");
                closeSocket(mmSocket);
                connectionFailed();
                return;
            }

            synchronized (AndroidBluetoothManager.this) {
                mConnectThread = null;
            }

            connected(mmSocket, mmDevice);
        }

    }

}
