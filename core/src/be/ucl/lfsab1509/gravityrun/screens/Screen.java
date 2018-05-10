package be.ucl.lfsab1509.gravityrun.screens;

import be.ucl.lfsab1509.gravityrun.GravityRun;
import be.ucl.lfsab1509.gravityrun.tools.BluetoothManager;
import be.ucl.lfsab1509.gravityrun.tools.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public abstract class Screen extends BluetoothManager implements com.badlogic.gdx.Screen {

    BluetoothManager bluetoothManager;
    public int height, width;
    GravityRun game;
    ScreenManager screenManager;
    SoundManager soundManager;

    Screen(GravityRun gravityRun) {
        game = gravityRun;
        height = GravityRun.HEIGHT;
        width = GravityRun.WIDTH;
        bluetoothManager = game.bluetoothManager;
        screenManager = game.screenManager;
        soundManager = game.soundManager;
    }

    @Override
    public void write(String string) {
        bluetoothManager.write(string);
    }

    @Override
    public boolean isConnected() {
        return bluetoothManager.isConnected();
    }

    @Override
    public void disconnect() {
        bluetoothManager.disconnect();
    }

    @Override
    public void discoverDevices() {
        bluetoothManager.discoverDevices();
    }

    @Override
    public void startHost() {
        bluetoothManager.startHost();
    }

    @Override
    public void connect(int devicePosition) {
        bluetoothManager.connect(devicePosition);
    }

    @Override
    public void enableBluetooth() {
        bluetoothManager.enableBluetooth();
    }

    @Override
    public void getPairedDevices() {
        bluetoothManager.getPairedDevices();
    }

    @Override
    public boolean supportDeviceBluetooth() {
        return bluetoothManager.supportDeviceBluetooth();
    }

    @Override
    public void setMultiPlayScreen(AbstractMultiPlayScreen multiPlayScreen) {
        bluetoothManager.setMultiPlayScreen(multiPlayScreen);
    }

    @Override
    public boolean isHost() {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void render(float dt) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    boolean clickedBack() {
        return Gdx.input.isKeyJustPressed(Input.Keys.BACK) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE);
    }

}