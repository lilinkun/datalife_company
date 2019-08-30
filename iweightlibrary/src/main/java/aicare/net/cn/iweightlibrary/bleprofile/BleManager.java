package aicare.net.cn.iweightlibrary.bleprofile;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

public interface BleManager<E extends BleManagerCallbacks> {

    /**
     * Connects to the Bluetooth Smart device
     *
     * @param context this must be an application context, not the Activity. Call {@link Activity#getApplicationContext()} to get one.
     * @param device  a device to connect to
     */
    void connect(final Context context, final BluetoothDevice device);

    /**
     * Disconnects from the device. Does nothing if not connected.
     */
    void disconnect();

    /**
     * Sets the manager callback listener
     *
     * @param callbacks the callback listener
     */
    void setGattCallbacks(E callbacks);

    /**
     * Closes and releases resources. May be also used to unregister broadcast listeners.
     */
    void closeBluetoothGatt();
}
