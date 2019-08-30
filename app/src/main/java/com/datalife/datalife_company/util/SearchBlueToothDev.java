package com.datalife.datalife_company.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.datalife.datalife_company.adapter.BlueToothAdapter;
import com.datalife.datalife_company.bean.BlueType;
import com.datalife.datalife_company.db.DBManager;

import java.util.ArrayList;
import java.util.HashMap;

import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.ParseData;

/**
 * 搜索蓝牙
 * Created by LG on 2019/7/19.
 */
public class SearchBlueToothDev {

    public HashMap<String,BluetoothDevice> bls = new HashMap<>();
    private ArrayList<BlueType> blueTypes = new ArrayList<>();
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();
    private ArrayList<String> arraynames = new ArrayList<>();
    private boolean isSupportBLE;
    private BluetoothAdapter mAdapter;
    private BlueToothAdapter blueToothAdapter = null;
    public static SearchBlueToothDev mInstance = null;
    private Handler myHandler;
    private String TAG = "SearchBlueToothDev";
    private Activity activity;

    public SearchBlueToothDev(Handler handler){
        this.myHandler = handler;
    }

    public static SearchBlueToothDev getInstance(Handler handler){
        if (mInstance == null) {
            synchronized (SearchBlueToothDev.class) {
                if (mInstance == null) {
                    mInstance = new SearchBlueToothDev(handler);
                }
            }
        }
        return mInstance;
    }

    /**
     * 搜索蓝牙
     * @param activity
     */
    public void searchDev(Activity activity) {
        this.activity = activity;

        isSupportBLE = activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!isSupportBLE) {
            UToast.show(activity.getBaseContext(), "Mobile phone not support BLE!");
        } else {
            BluetoothManager bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
            mAdapter = bluetoothManager.getAdapter();
            if(mAdapter.isEnabled()) {
                if (mAdapter != null) {
//            if (mAdapter.isEnabled()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int checkCallPhonePermission = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
                        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                            //判断是否需要 向用户解释，为什么要申请该权限
                            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION))
                                Toast.makeText(activity, "动态请求权限", Toast.LENGTH_LONG).show();
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, IDatalifeConstant.REQUEST_COARSE_LOCATION);
                            return;
                        }
                    }
                    bls.clear();
                    mAdapter.startLeScan(leScanCallback);
                }
            }else {
                onOpenBLE();
            }
        }
    }

    public void stopSearchBlueDev(){
        if (mAdapter != null) {
            mAdapter.stopLeScan(leScanCallback);
        }
    }

    //打开蓝牙
    public void onOpenBLE() {
        activity.startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), IDatalifeConstant.REQUEST_OPEN_BT);
    }

    BluetoothAdapter.LeScanCallback  leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            final String name = device.getName();
            if (!TextUtils.isEmpty(name) && name.startsWith("HC02")) {
                Log.e("onLeScan", "dev name:" + name);
                //  connect(device,name);
                handmessage(name,device,IDatalifeConstant.TYPE_HEALTH);
            }else {
                //此方法是体脂称ｓｄｋ中确认是体脂称的方法
                int flag = AicareBleConfig.isAicareDevice(scanRecord);
                if (flag != Integer.MIN_VALUE) {
                    LogUtil.e(TAG, "name: " + device.getAddress() + "; scanRecord: " + ParseData.byteArr2Str(scanRecord));
                    handmessage(device.getAddress(),device,IDatalifeConstant.TYPE_FAT);
                }
            }
        }
    };

    public void handmessage(String name,BluetoothDevice device,int type){
        if (!bls.containsKey(name)){
            bls.put(name,device);
            arraynames.add(name);
            mDeviceList.add(device);
            BlueType blueType = new BlueType(name,type,device);
            blueTypes.add(blueType);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("name" , name);
            bundle.putInt("type",type);
            bundle.putParcelable("device",device);
            message.setData(bundle);
            message.what = IDatalifeConstant.MESSAGESERVICE;
            myHandler.sendMessage(message);
        }
    }

}
