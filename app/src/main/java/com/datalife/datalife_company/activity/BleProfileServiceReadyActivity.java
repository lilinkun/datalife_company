package com.datalife.datalife_company.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;

import aicare.net.cn.iweightlibrary.bleprofile.BleProfileService;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.utils.ParseData;
import aicare.net.cn.iweightlibrary.wby.WBYService;

import static android.Manifest.permission.BLUETOOTH_ADMIN;

/**
 * Created by LG on 2019/7/24.
 */
public abstract class BleProfileServiceReadyActivity <E extends WBYService.WBYBinder> extends AppCompatActivity {

    private final static String TAG = "BleProfileServiceReadyActivity";

    protected static final int REQUEST_ENABLE_BT = 2;

    private E mService;

    private boolean mIsScanning = false;
    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter adapter = null;
    private ScanCallback scanCallback = null;
    private String mDevName = null;
    private BluetoothAdapter.LeScanCallback mLEScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            L.e(TAG, "onLeScan");
            if (device != null) {
                L.e(TAG, "address: " + device.getAddress() + "; name: " + device.getName() + "; scanRecord:" + ParseData.byteArr2Str(scanRecord));
                L.e(TAG, ParseData.byteArr2Str(scanRecord));
                int flag = AicareBleConfig.isAicareDevice(scanRecord);
                if (flag != Integer.MIN_VALUE) {
                    L.e(TAG, "name: " + device.getName() + "; scanRecord: " + ParseData.byteArr2Str(scanRecord));
                    getAicareDevice(device, rssi, flag);
                }
            }
        }
    };

    private BroadcastReceiver mCommonBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                bluetoothStateChanged(state);
            } else if (BleProfileService.ACTION_CONNECT_STATE_CHANGED.equals(action)) {
                int connectState = intent.getIntExtra(BleProfileService.EXTRA_CONNECT_STATE, -1);
                String address = intent.getStringExtra(BleProfileService.EXTRA_DEVICE_ADDRESS);
                onStateChanged(address, connectState);
//                service = null;
            } else if (BleProfileService.ACTION_CONNECT_ERROR.equals(action)) {
                String errMsg = intent.getStringExtra(BleProfileService.EXTRA_ERROR_MSG);
                int errCode = intent.getIntExtra(BleProfileService.EXTRA_ERROR_CODE, -1);
                onError(errMsg, errCode);
            } else if (WBYService.ACTION_WEIGHT_DATA.equals(action)) {
                WeightData weightData = (WeightData) intent.getSerializableExtra(WBYService.EXTRA_WEIGHT_DATA);
                onGetWeightData(weightData);
            } else if (WBYService.ACTION_SETTING_STATUS_CHANGED.equals(action)) {
                int settingStatus = intent.getIntExtra(WBYService.EXTRA_SETTING_STATUS, AicareBleConfig.SettingStatus.UNKNOWN);
                onGetSettingStatus(settingStatus);
            } else if (WBYService.ACTION_RESULT_CHANGED.equals(action)) {
                int index = intent.getIntExtra(WBYService.EXTRA_RESULT_INDEX, -1);
                String result = intent.getStringExtra(WBYService.EXTRA_RESULT);
                onGetResult(index, result);
            } else if (WBYService.ACTION_FAT_DATA.equals(action)) {
                boolean isHistory = intent.getBooleanExtra(WBYService.EXTRA_IS_HISTORY, false);
                BodyFatData bodyFatData = (BodyFatData) intent.getSerializableExtra(WBYService.EXTRA_FAT_DATA);
                onGetFatData(isHistory, bodyFatData);
            }
        }
    };

    @RequiresPermission(BLUETOOTH_ADMIN)
    protected void bluetoothStateChanged(int state) {
        switch (state) {
            case BluetoothAdapter.STATE_TURNING_OFF:
                if (mService != null) {
                    mService.disconnect();
                }
                stopScan();
                break;
        }
    }

    /**
     * 连接状态改变
     *
     * @param deviceAddress 设备地址
     * @param state         状态
     * @see BleProfileService#STATE_CONNECTED
     * @see BleProfileService#STATE_DISCONNECTED
     * @see BleProfileService#STATE_INDICATION_SUCCESS
     * @see BleProfileService#STATE_SERVICES_DISCOVERED
     */
    protected void onStateChanged(String deviceAddress, int state) {
        switch (state) {
            case BleProfileService.STATE_DISCONNECTED:
                try {
                    unbindService(mServiceConnection);
                    mService = null;
                } catch (final IllegalArgumentException e) {
                    // do nothing. This should never happen but does...
                }
                break;
            case BleProfileService.STATE_CONNECTED:
                stopScan();//连接设备时需停止扫描
                break;
        }
    }

    /**
     * 连接异常
     *
     * @param errMsg  错误信息
     * @param errCode 错误码
     */
    protected abstract void onError(String errMsg, int errCode);

    protected abstract void onGetWeightData(WeightData weightData);

    protected abstract void onGetSettingStatus(@AicareBleConfig.SettingStatus int status);

    protected abstract void onGetResult(int index, String result);

    protected abstract void onGetFatData(boolean isHistory, BodyFatData bodyFatData);

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName name, final IBinder service) {
            final E bleService = mService = (E) service;
            onServiceBinded(bleService);
            // and notify user if device is connected
            if (bleService.isConnected())
                onStateChanged(bleService.getDeviceAddress(), BleProfileService.STATE_CONNECTED);
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            onServiceUnbinded();
        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanCallBack();
        onInitialize();
        final Intent service = new Intent(this, WBYService.class);
        bindService(service, mServiceConnection, 0);
        getApplication().registerReceiver(mCommonBroadcastReceiver, makeIntentFilter());

    }

    protected void onInitialize() {
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        adapter = bluetoothManager.getAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getApplication().unregisterReceiver(mCommonBroadcastReceiver);
        try {
            unbindService(mServiceConnection);
            mService = null;
            onServiceUnbinded();
        } catch (final IllegalArgumentException e) {
            // do nothing, we were not connected to the sensor
        }
    }

    private static IntentFilter makeIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BleProfileService.ACTION_CONNECT_STATE_CHANGED);
        intentFilter.addAction(BleProfileService.ACTION_CONNECT_ERROR);
        intentFilter.addAction(WBYService.ACTION_WEIGHT_DATA);
        intentFilter.addAction(WBYService.ACTION_SETTING_STATUS_CHANGED);
        intentFilter.addAction(WBYService.ACTION_RESULT_CHANGED);
        intentFilter.addAction(WBYService.ACTION_FAT_DATA);
        intentFilter.addAction(WBYService.ACTION_AUTH_DATA);
        intentFilter.addAction(WBYService.ACTION_DID);
        return intentFilter;
    }

    protected abstract void onServiceBinded(E binder);

    protected abstract void onServiceUnbinded();

    /**
     * 开始连接设备
     *
     * @param address
     */
    public void startConnect(String address) {
        stopScan();
        Intent service = new Intent(this, WBYService.class);
        service.putExtra(BleProfileService.EXTRA_DEVICE_ADDRESS, address);
        bindService(service, mServiceConnection, 0);
        startService(service);
    }

    /**
     * 是否已连接
     *
     * @return true:已连接; false:未连接
     */
    protected boolean isDeviceConnected() {
        return mService != null && mService.isConnected();
    }

    /**
     * 是否支持BLE
     *
     * @return true:支持; false:不支持
     */
    protected boolean ensureBLESupported() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    /**
     * 蓝牙是否开启
     *
     * @return true:开启; false:未开启
     */
    protected boolean isBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothAdapter adapter = bluetoothManager.getAdapter();
        return adapter != null && adapter.isEnabled();
    }

    /**
     * 显示开启蓝牙提示框
     */
    protected void showBLEDialog() {
        final Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
    }

    /**
     * 是否正在扫描
     *
     * @return true:正在扫描; false:已停止扫描
     */
    public boolean isScanning() {
        return mIsScanning;
    }

    /**
     * 开始扫描
     */
    protected void startScan() {
        if (isBLEEnabled()) {
            if (!mIsScanning) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    adapter.getBluetoothLeScanner().startScan(scanCallback);
//                }else {
                adapter.startLeScan(mLEScanCallback);

//                }
                mIsScanning = true;
            }
        } else {
            showBLEDialog();
        }
    }

    /**
     * 停止扫描
     */
    protected void stopScan() {
        if (mIsScanning) {
            if (adapter != null) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    adapter.getBluetoothLeScanner().stopScan(scanCallback);
//                }else {
                adapter.stopLeScan(mLEScanCallback);
//                }
            }
            mIsScanning = false;
        }
    }

    private void scanCallBack(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            scanCallback = new ScanCallback() {
//                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                @Override
//                public void onScanResult(int callbackType, ScanResult result) {
//                    final byte[] scanRecord = result.getScanRecord().getBytes();
//                    BluetoothDevice device = result.getDevice();
//                    if (device != null) {
//                        L.e(TAG, "address: " + device.getAddress() + "; name: " + device.getName() + "; scanRecord:" + ParseData.byteArr2Str(scanRecord));
//                        L.e(TAG, ParseData.byteArr2Str(scanRecord));
//                        //L.e(TAG, "name: " + device.getName() + "; address: " + device.getAddress());
//                        int flag = AicareBleConfig.isAicareDevice(scanRecord);
//                        if (flag != Integer.MIN_VALUE) {
//                            L.e(TAG, "name: " + device.getName() + "; scanRecord: " + ParseData.byteArr2Str(scanRecord));
//                            getAicareDevice(device, result.getRssi(), flag);
//                        }
//                    }
//                    super.onScanResult(callbackType, result);
//                }
//
//                @Override
//                public void onBatchScanResults(List<ScanResult> results) {
//                    super.onBatchScanResults(results);
//                }
//
//                @Override
//                public void onScanFailed(int errorCode) {
//                    super.onScanFailed(errorCode);
//                }
//            };
//        }else{
//        mLEScanCallback = new BluetoothAdapter.LeScanCallback() {
//            @Override
//            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//                L.e(TAG, "onLeScan");
//                if (device != null) {
//                    L.e(TAG, "address: " + device.getAddress() + "; name: " + device.getName() + "; scanRecord:" + ParseData.byteArr2Str(scanRecord));
//                    L.e(TAG, ParseData.byteArr2Str(scanRecord));
//                    int flag = AicareBleConfig.isAicareDevice(scanRecord);
//                    if (flag != Integer.MIN_VALUE) {
//                        L.e(TAG, "name: " + device.getName() + "; scanRecord: " + ParseData.byteArr2Str(scanRecord));
//                        getAicareDevice(device, rssi, flag);
//                    }
//                }
//            }
//        };
//        }
    }


    public void connectBle(){

    }

    /**
     * 获取到符合Aicare协议的体脂秤
     * @param device
     * @param rssi
     */
    protected abstract void getAicareDevice(BluetoothDevice device, int rssi, int flag);
}
