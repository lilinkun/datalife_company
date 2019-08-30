package aicare.net.cn.iweightlibrary.wby;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import aicare.net.cn.iweightlibrary.bleprofile.BleManager;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.User;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.utils.ParseData;

public class WBYManager implements BleManager<WBYManagerCallbacks> {
    private static final String TAG = "WBYManager";
    private WBYManagerCallbacks mCallbacks;
    private BluetoothGatt mBluetoothGatt;
    private Context mContext;

    public final static String AICARE_SERVICE_UUID_STR = "0000ffb0-0000-1000-8000-00805f9b34fb";

    public final static UUID AICARE_SERVICE_UUID = UUID.fromString(AICARE_SERVICE_UUID_STR);

    private static final UUID AICARE_NOTIFY_CHARACTERISTIC_UUID = UUID.fromString("0000ffb2-0000-1000-8000-00805f9b34fb");
    private static final UUID AICARE_WRITE_CHARACTERISTIC_UUID = UUID.fromString("0000ffb1-0000-1000-8000-00805f9b34fb");

    private static final UUID DESCR_TWO = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    private final static String ERROR_CONNECTION_STATE_CHANGE = "Error on connection state change";
    private final static String ERROR_DISCOVERY_SERVICE = "Error on discovering services";
    private final static String ERROR_WRITE_DESCRIPTOR = "Error on writing descriptor";

    private BluetoothGattCharacteristic mAicareWCharacteristic, mAicareNCharacteristic;

    private static WBYManager managerInstance = null;

    private List<byte[]> usersByte = new ArrayList<>();
    private int index = 0;
    private byte[] userIdByte;//用户编号
    private byte[] userInfoByte;//用户信息
    private byte[] dateByte;//日期

    /**
     * singleton implementation of WBYManager class
     */
    public static synchronized WBYManager getWBYManager() {
        if (managerInstance == null) {
            managerInstance = new WBYManager();
        }
        return managerInstance;
    }

    @Override
    public void setGattCallbacks(WBYManagerCallbacks callbacks) {
        mCallbacks = callbacks;
    }

    @Override
    public void connect(Context context, BluetoothDevice device) {
        L.i(TAG, TAG + ".connect");
        closeBluetoothGatt();
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
        mContext = context;
    }

    @Override
    public void disconnect() {
        L.d(TAG, "disconnect方法被调用");
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // This will send callback to HTSActivity when device get
                    // connected
                    mCallbacks.onDeviceConnected();
                    /*try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                    if (mBluetoothGatt != null) {
                        mBluetoothGatt.discoverServices();
                    }
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    L.d(TAG, "Device disconnected");
                    // This will send callback to HTSActivity when device get
                    // disconnected
                    mCallbacks.onDeviceDisconnected();
                }
            } else {
                L.e(TAG, "onConnectionStateChange error: (" + status + ")");
                mCallbacks.onError(ERROR_CONNECTION_STATE_CHANGE, status);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                L.i(TAG, "onServicesDiscovered Success");
                L.i(TAG, "onServicesDiscovered status = " + status);
                List<BluetoothGattService> services = gatt.getServices();
                L.i(TAG, "onServicesDiscovered services = " + services.size());
                if (services.size() == 0 || services == null) {
                    mBluetoothGatt.discoverServices();
                }
                for (BluetoothGattService service : services) {
                    L.e(TAG, service.getUuid().toString());
                    for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
                        L.e(TAG, characteristic.getUuid().toString() + "; permission: " + ParseData.int2HexStr(characteristic.getPermissions())
                                + "; property: " + ParseData.int2HexStr(characteristic.getProperties()));
                    }
                }
                BluetoothGattService aicareService = gatt.getService(AICARE_SERVICE_UUID);
                if (services.contains(gatt.getService(AICARE_SERVICE_UUID))) {
                    mAicareWCharacteristic = aicareService.getCharacteristic(AICARE_WRITE_CHARACTERISTIC_UUID);
                    mAicareNCharacteristic = aicareService.getCharacteristic(AICARE_NOTIFY_CHARACTERISTIC_UUID);
                    if (hasAicareUUID()) {
                        mCallbacks.onServicesDiscovered();
                        enableAicareIndication();
                    }
                }
            } else {
                L.e(TAG, "onServicesDiscovered error: (" + status + ")");
                mCallbacks.onError(ERROR_DISCOVERY_SERVICE, status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (characteristic.getUuid().equals(AICARE_WRITE_CHARACTERISTIC_UUID)) {
                    byte[] b = characteristic.getValue();
                    L.i(TAG, "onCharacteristicWrite: " + ParseData.byteArr2Str(b));
                    if (usersByte.size() != 0) {
                        System.out.println("index = " + index);
                        if (index < usersByte.size() - 1) {
                            if (Arrays.equals(b, usersByte.get(index))) {
                                writeValue(usersByte.get(++index));
                            }
                        } else {
                            if (Arrays.equals(b, usersByte.get(index))) {
                                sendCmd(AicareBleConfig.SYNC_LIST_OVER, AicareBleConfig.UNIT_KG);
                            }
                        }
                    }

                    if (Arrays.equals(b, userIdByte)) {
                        syncUserInfo();
                    }

                    if (Arrays.equals(b, dateByte)) {
                        sendCmd(AicareBleConfig.SYNC_TIME, AicareBleConfig.UNIT_KG);
                    }
                }
            } else {
                L.e(TAG, "onCharacteristicWrite error: +  (" + status + ")");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] b = characteristic.getValue();
            L.i(TAG, "onCharacteristicChanged: " + ParseData.byteArr2Str(b));
            if (characteristic.getUuid().equals(AICARE_NOTIFY_CHARACTERISTIC_UUID)) {
                handleData(b);
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //indications has been enabled
                L.i(TAG, "onDescriptorWrite");
                mCallbacks.onIndicationSuccess();
            } else {
                L.e(TAG, "onDescriptorWrite error: +  (" + status + ")");
                mCallbacks.onError(ERROR_WRITE_DESCRIPTOR, status);
            }
        }
    };

    /**
     * 使能(订阅通知)
     */
    private void enableAicareIndication() {
        L.i(TAG, "enableAicareIndication");
        boolean isNotify = mBluetoothGatt.setCharacteristicNotification(mAicareNCharacteristic, true);
        if (isNotify) {
            mCallbacks.onIndicationSuccess();
        }
        /*BluetoothGattDescriptor descriptor = mAicareNCharacteristic.getDescriptor(DESCR_TWO);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);*/
        L.i(TAG, "enableAicareIndication sync.......................");
    }

    @Override
    public void closeBluetoothGatt() {
        if (mBluetoothGatt != null) {
            refresh(mBluetoothGatt);
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            mAicareWCharacteristic = null;
            mAicareNCharacteristic = null;
        }
    }

    /**
     * 判断是否有aicareUUID
     *
     * @return
     */
    private boolean hasAicareUUID() {
        if (mAicareWCharacteristic != null) {
            return true;
        }
        return false;
    }

    /**
     * 往aicare有写入属性的特征值中写值
     *
     * @param b
     */
    private void writeValue(byte[] b) {
        if (hasAicareUUID()) {
            mAicareWCharacteristic.setValue(b);
            mAicareWCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            boolean success = mBluetoothGatt.writeCharacteristic(mAicareWCharacteristic);
            if (success) {
                L.e(TAG, "writeValue: bytes = " + ParseData.byteArr2Str(b));
            }
        }
    }

    /**
     * 同步
     * @param index
     * @param unitType
     */
    public void sendCmd(byte index, byte unitType) {
        byte[] b = AicareBleConfig.initCmd(index, null, unitType);
        writeValue(b);
    }

    /**
     * 同步当前用户
     *
     * @param user
     */
    public void syncUser(User user) {
        userIdByte =AicareBleConfig.initCmd(AicareBleConfig.SYNC_USER_ID, user, AicareBleConfig.UNIT_KG);
        userInfoByte = AicareBleConfig.initCmd(AicareBleConfig.SYNC_USER_INFO, user, AicareBleConfig.UNIT_KG);
        syncUserId();
    }

    /**
     * 同步用户编号
     */
    private void syncUserId() {
        L.e(TAG, "syncUserId");
        writeValue(userIdByte);
    }

    /**
     * 同步用户信息
     */
    private void syncUserInfo() {
        L.e(TAG, "syncUserInfo");
        writeValue(userInfoByte);
    }

    /**
     * 同步日期
     */
    public void syncDate() {
        L.e(TAG, "syncDate");
        dateByte = AicareBleConfig.initCmd(AicareBleConfig.SYNC_DATE, null, AicareBleConfig.UNIT_KG);
        writeValue(dateByte);
    }

    /**
     * 当用户列表不为空时，同步list中index为0的用户
     *
     * @param userList
     */
    public void syncUserList(List<User> userList) {
        usersByte = AicareBleConfig.initUserListCmds(userList);
        if (usersByte.size() != 0) {
            writeValue(usersByte.get(index));
        }
    }

    /**
     * 更新用户信息
     * @param user
     */
    public void updateUser(User user) {
        byte[] b = AicareBleConfig.initUpdateUserCmd(user);
        writeValue(b);
    }

    /**
     * 处理ble传过来的数据
     *
     * @param b
     */
    private void handleData(byte[] b) {
        SparseArray<Object> sparseArray = AicareBleConfig.getDatas(b);
        if (sparseArray != null && sparseArray.size() != 0) {
            if (sparseArray.indexOfKey(AicareBleConfig.WEIGHT_DATA) >= 0) {
                mCallbacks.getWeightData((WeightData) sparseArray.get(AicareBleConfig.WEIGHT_DATA));
            } else if (sparseArray.indexOfKey(AicareBleConfig.SETTINGS_STATUS) >= 0) {
                if ((int) sparseArray.get(AicareBleConfig.SETTINGS_STATUS) == AicareBleConfig.SettingStatus.REQUEST_DISCONNECT) {
                    disconnect();
                }
                mCallbacks.getSettingStatus((Integer) sparseArray.get(AicareBleConfig.SETTINGS_STATUS));
            } else if (sparseArray.indexOfKey(AicareBleConfig.BLE_VERSION) >= 0) {
                mCallbacks.getResult(WBYService.BLE_VERSION, String.valueOf(sparseArray.get(AicareBleConfig.BLE_VERSION)));
            } else if (sparseArray.indexOfKey(AicareBleConfig.USER_ID_STR) >= 0) {
                mCallbacks.getResult(WBYService.USER_ID, String.valueOf(sparseArray.get(AicareBleConfig.USER_ID_STR)));
            } else if (sparseArray.indexOfKey(AicareBleConfig.MCU_DATE_STR) >= 0) {
                mCallbacks.getResult(WBYService.MCU_DATE, String.valueOf(sparseArray.get(AicareBleConfig.MCU_DATE_STR)));
            } else if (sparseArray.indexOfKey(AicareBleConfig.MCU_TIME_STR) >= 0) {
                mCallbacks.getResult(WBYService.MCU_TIME, String.valueOf(sparseArray.get(AicareBleConfig.MCU_TIME_STR)));
            } else if (sparseArray.indexOfKey(AicareBleConfig.ADC_STR) >= 0) {
                mCallbacks.getResult(WBYService.ADC, String.valueOf(sparseArray.get(AicareBleConfig.ADC_STR)));
            } else if (sparseArray.indexOfKey(AicareBleConfig.HISTORY_DATA) >= 0) {
                mCallbacks.getFatData(true, (BodyFatData) sparseArray.get(AicareBleConfig.HISTORY_DATA));
            } else if (sparseArray.indexOfKey(AicareBleConfig.BODY_FAT_DATA) >= 0) {
                mCallbacks.getFatData(false, (BodyFatData) sparseArray.get(AicareBleConfig.BODY_FAT_DATA));
            }
        }
    }

    /**
     * 清除设备缓存
     *
     * @param gatt
     * @return
     */
    public static boolean refresh(BluetoothGatt gatt) {
        try {
            L.e(TAG, "refresh device cache");
            Method localMethod = gatt.getClass().getMethod("refresh", (Class[]) null);
            if (localMethod != null) {
                boolean result = (Boolean) localMethod.invoke(gatt, (Object[]) null);
                if (!result)
                    L.e(TAG, "refresh failed");
                return result;
            }
        } catch (Exception e) {
            L.e(TAG, "An exception occurred while refreshing device cache");
        }
        return false;
    }
}
