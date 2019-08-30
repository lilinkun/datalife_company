package com.datalife.datalife_company.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.FragmentsHealthAdapter;
import com.datalife.datalife_company.adapter.MemberAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseHealthActivity;
import com.datalife.datalife_company.base.BaseHealthFragment;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.contract.HealthMonitorContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.fragment.BpFragment;
import com.datalife.datalife_company.fragment.BtFragment;
import com.datalife.datalife_company.fragment.ECGFragment;
import com.datalife.datalife_company.fragment.MonitorInfoFragment;
import com.datalife.datalife_company.fragment.SPO2HFragment;
import com.datalife.datalife_company.interf.OnDataListener;
import com.datalife.datalife_company.interf.OnPageListener;
import com.datalife.datalife_company.interf.RecyclerItemClickListener;
import com.datalife.datalife_company.presenter.HealthMonitorPresenter;
import com.datalife.datalife_company.service.HcService;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.SearchBlueToothDev;
import com.datalife.datalife_company.widget.AlertDialogBuilder;
import com.datalife.datalife_company.widget.CustomViewPager;
import com.linktop.constant.BluetoothState;
import com.linktop.constant.DeviceInfo;
import com.linktop.constant.WareType;
import com.linktop.infs.OnBatteryListener;
import com.linktop.infs.OnDeviceInfoListener;
import com.linktop.infs.OnDeviceVersionListener;
import com.linktop.whealthService.BleDevManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/16.
 */
public class HealthMonitorActivity extends BaseHealthActivity implements HealthMonitorContract,ServiceConnection, ViewPager.OnPageChangeListener,OnDataListener,OnPageListener, OnDeviceVersionListener, OnBatteryListener, OnDeviceInfoListener {

    @BindView(R.id.view_pager)
    public CustomViewPager viewPager;
    @BindView(R.id.tv_headtop)
    TextView mHeadTopTv;
    @BindView(R.id.iv_head_right)
    ImageView mRightImage;
    @BindView(R.id.iv_bp_operation)
    ImageView mIvOperation;
    @BindView(R.id.include_battery)
    RelativeLayout relativeLayout;
    @BindView(R.id.rv_member)
    RecyclerView recyclerView;
    @BindView(R.id.iv_bluetooth)
    ImageView mIvBluetooth;
    @BindView(R.id.tv_battery)
    TextView mBattery;
    @BindView(R.id.ll_top)
    LinearLayout mTopLayout;

    HealthMonitorPresenter healthMonitorPresenter= new HealthMonitorPresenter();
    public static String mMemberId = "";
    public static MachineBean mMachine;
    public static ArrayList<MachineBindMemberBean> machineBindMemberBean = null;
    private FragmentsHealthAdapter adapter = null;
    public HcService mHcService;
    private MonitorInfoFragment mMonitorInfoFragment;
    private final SparseArray<BaseHealthFragment> sparseArray = new SparseArray<>();
    public static MeasureFamilyUserInfo measureFamilyUserInfo = null;
    public static List<MeasureFamilyUserInfo> familyUserInfoList = null;
    private MemberAdapter memberAdapter = null;
    private SearchBlueToothDev searchBlueToothDev;
    private int batteryValue;
    private String TAG = "HealthMonitorActivity";
    public static String DeviceId = "";

    private ECGFragment mEcgFragment;
    private BpFragment mBpFragment;
    private BtFragment mBtFragment;
    private SPO2HFragment mSpo2HFragment;
    private boolean isBlueToothConnected = false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case IDatalifeConstant.MESSAGESERVICE:
                    if(msg.getData().getString("name").equals(mMachine.getMachineName())){
                        mHcService.stopSearchAndConnect(msg.getData().getString("name"));
                    }
                    break;

                case HcService.BLE_STATE:
                    final int state = (int) msg.obj;
                    if (state == BluetoothState.BLE_NOTIFICATION_ENABLED) {
                        mHcService.dataQuery(HcService.DATA_QUERY_SOFTWARE_VER);
                    } else {
                        mMonitorInfoFragment.onBleState(state);

                        if (state == BluetoothState.BLE_CONNECTED_DEVICE){

                            if (mIvBluetooth != null) {
                                mIvBluetooth.setImageResource(R.mipmap.ic_bluetooth_open);
                            }
                            isBlueToothConnected = true;

                        }else {
                            if (mIvBluetooth != null) {
                                mIvBluetooth.setImageResource(R.mipmap.ic_bluetooth_close);
                            }
                            isBlueToothConnected = false;
                        }
                    }
                    break;

                case IDatalifeConstant.VISIBLEHEALTH:

                    if (batteryValue != 0) {
                        mBattery.setText(batteryValue + "%");
                    }
                    break;

                case IDatalifeConstant.CHARGINGHEALTH:

                    mBattery.setText("充电中");

                    break;

                case IDatalifeConstant.POWER_BATTERY:
                    if (mHcService != null) {
                        mHcService.dataQuery(HcService.DATA_QUERY_BATTERY_INFO);
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_health_monitor;
    }


    @Override
    protected void initEventAndData() {
        healthMonitorPresenter.onCreate(this,this);
        healthMonitorPresenter.getMachineInfo("1","50", ProApplication.SESSIONID);
        familyUserInfoList = DBManager.getInstance(this).queryMeasureFamilyUserInfoList();

        searchBlueToothDev = SearchBlueToothDev.getInstance(mHandler);

        if (getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER) != null) {
            Bundle bundle = getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER);

            if (bundle.getSerializable("machine") != null) {
                mMachine = (MachineBean) bundle.getSerializable("machine");

                if (familyUserInfoList != null && familyUserInfoList.size() > 0) {
                    measureFamilyUserInfo = familyUserInfoList.get(0);
                    mMemberId = String.valueOf(measureFamilyUserInfo.getMember_Id());
                }

            }

            viewPager.setCanScroll(false);//设置viewpager不能滑动
            viewPager.setOffscreenPageLimit(1);
            viewPager.setPageMargin(10);
            viewPager.addOnPageChangeListener(this);
            viewPager.setCurrentItem(0, false);

            //绑定服务，
            // 类型是 HealthMonitor（HealthMonitor健康检测仪），
            if (ProApplication.isUseCustomBleDevService) {
                Intent serviceIntent = new Intent(this, HcService.class);
                bindService(serviceIntent, this, BIND_AUTO_CREATE);
            }


            if (viewPager.getCurrentItem() == IDatalifeConstant.PAGE_HOME) {
                mTopLayout.setVisibility(View.GONE);
            } else {
                mTopLayout.setVisibility(View.VISIBLE);
            }
        }


    }

    @OnClick({R.id.iv_head_left,R.id.iv_head_right,R.id.btn_connect,R.id.iv_bluetooth})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_head_left:
                back();
                break;

            case R.id.iv_head_right:
                switch (viewPager.getCurrentItem()){
                    case IDatalifeConstant.PAGE_BP:
                        onIvOperation(R.mipmap.ic_bp_operation);
                        break;

                    case IDatalifeConstant.PAGE_ECG:
                        onIvOperation(R.mipmap.ic_ecg_operation);
                        break;

                    case IDatalifeConstant.PAGE_TEMP:
                        onIvOperation(R.mipmap.ic_temp_operation);
                        break;

                    case IDatalifeConstant.PAGE_SPO2H:
                        onIvOperation(R.mipmap.ic_spo2h_operation);
                        break;
                }
                break;

            case R.id.btn_connect:
//                mMonitorInfoFragment.clickConnect();
                break;

            case R.id.iv_bluetooth:
//                if (!mHcService.isConnected) {
//                    mMonitorInfoFragment.clickConnect();
//                }
                break;

        }
    }

    //点击提示键的时候
    public void onIvOperation(int idres){

        if (mIvOperation != null && mIvOperation.isShown()){
            mIvOperation.setVisibility(View.GONE);
        }else {
            mIvOperation.setImageResource(idres);
            mIvOperation.setVisibility(View.VISIBLE);
        }
    }

    //点击回退键的时候
    public void back(){
        if (onBackMeasuring()){
            return;
        }

        if (mIvOperation != null && mIvOperation.isShown()){
            mIvOperation.setVisibility(View.GONE);
            return;
        }

        if (viewPager.getCurrentItem() == IDatalifeConstant.PAGE_HOME) {
            finish();
        }else{
            onBack();
        }
    }

    private boolean onBackMeasuring(){
            if(mHcService.getBleDevManager().isMeasuring()){
                new AlertDialogBuilder
                        (this)
                        .setTitle("提示")
                        .setMessage("测量中，若要退出界面请先停止测量。")
                        .setPositiveButton("好的", null)
                        .create().show();
                return true;
            }
            return false;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mHcService = ((HcService.LocalBinder) service).getService();
        mHcService.setHandler(mHandler);
        //服务绑定成功后加载各个测量界面
        adapter = new FragmentsHealthAdapter(getSupportFragmentManager());
        getMenusFragments();
        adapter.setFragments(sparseArray);

        viewPager.setAdapter(adapter);
        mHcService.setDeviceName(mMachine.getMachineName());
        onMember();

//        searchBlueToothDev.searchDev(this);
        BleDevManager bleDevManager = mHcService.getBleDevManager();
        mHcService.setOnDeviceVersionListener(this);
        bleDevManager.getBatteryTask().setBatteryStateListener(this);
        bleDevManager.getDeviceTask().setOnDeviceInfoListener(this);

        mHcService.initBluetooth();
        mHcService.quicklyConnect();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


    private void getMenusFragments() {
        mMonitorInfoFragment = new MonitorInfoFragment();
        mEcgFragment = new ECGFragment();
        mBpFragment =  new BpFragment();
        mBtFragment = new BtFragment();
        mSpo2HFragment = new SPO2HFragment();
        sparseArray.put(IDatalifeConstant.PAGE_HOME, mMonitorInfoFragment);
        sparseArray.put(IDatalifeConstant.PAGE_BP,mBpFragment);
        sparseArray.put(IDatalifeConstant.PAGE_TEMP, mBtFragment);
        sparseArray.put(IDatalifeConstant.PAGE_SPO2H, mSpo2HFragment);
        sparseArray.put(IDatalifeConstant.PAGE_ECG, mEcgFragment);
    }

    @Override
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {

    }

    @Override
    public void onfail(String str) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        if (position == IDatalifeConstant.PAGE_HOME){
            mRightImage.setVisibility(View.GONE);
            mTopLayout.setVisibility(View.GONE);
            mHeadTopTv.setText(getString(R.string.health_testing));
            if (measureFamilyUserInfo != null) {
                mMonitorInfoFragment.setMemberName(measureFamilyUserInfo);
            }
        }else if (position == IDatalifeConstant.PAGE_BP || position == IDatalifeConstant.PAGE_ECG || position == IDatalifeConstant.PAGE_TEMP || position == IDatalifeConstant.PAGE_SPO2H){
            mRightImage.setImageResource(R.mipmap.ic_operation);
            mRightImage.setVisibility(View.VISIBLE);
            mTopLayout.setVisibility(View.VISIBLE);
            switch (position){
                case IDatalifeConstant.PAGE_BP:
                    mHeadTopTv.setText(getString(R.string.heartrate));
                    break;
                case IDatalifeConstant.PAGE_ECG:
                    mHeadTopTv.setText(getString(R.string.electrocardiogram));
                    break;
                case IDatalifeConstant.PAGE_TEMP:
                    mHeadTopTv.setText(getString(R.string.temp));
                    break;
                case IDatalifeConstant.PAGE_SPO2H:
                    mHeadTopTv.setText(getString(R.string.oxygen));
                    break;
            }
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        if (mHcService != null) {
            mHcService.disConnect();
        }
        unbindService(this);
        super.onDestroy();
    }

    @Override
    public void onBack() {
        onBackMeasuring();
        viewPager.setCurrentItem(0);
        mTopLayout.setVisibility(View.GONE);
//        mMonitorInfoFragment.setDevMemberId(mMemberId);
    }

    @Override
    public void onPage(int page) {
        if (page == IDatalifeConstant.PAGE_ECG){
//            mEcgFragment.onVisible();
        }
//        mPosition = page;

        viewPager.setCurrentItem(page,false);
    }

    @Override
    public void connect() {
        if (mHcService != null  && !mHcService.isSearch()) {
            mHcService.quicklyConnect();
        }
    }

    public void onMember() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        if (recyclerView == null){
            return;
        }
        recyclerView.setLayoutManager(linearLayoutManager);
        memberAdapter = new MemberAdapter(this);
        recyclerView.setAdapter(memberAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < 0){
                    return;
                }
                mMemberId = familyUserInfoList.get(position).getMember_Id()+"";

                onChageMember();
            }

            @Override
            public void onLongClick(View view, int posotion) {

            }
        }));
    }

    @Override
    public void onMachine(MachineBean machineBean) {

    }

    @Override
    public void onChageMember() {
        for (int i = 0; i < familyUserInfoList.size();i++){
            if (familyUserInfoList.get(i).getMember_Id() == Integer.valueOf(HealthMonitorActivity.mMemberId)){
                measureFamilyUserInfo = familyUserInfoList.get(i);
                memberAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(i);
                mMonitorInfoFragment.setMemberName(measureFamilyUserInfo);
            }
        }
    }

    @Override
    public void onBattery(int batteryValue) {
        this.batteryValue = batteryValue;
        mHandler.sendEmptyMessage(IDatalifeConstant.VISIBLEHEALTH);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDeviceVersion(WareType wareType, String s) {
        switch (wareType) {
            case VER_SOFTWARE:
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_HARDWARE_VER);
                }
                break;
            case VER_HARDWARE:
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_FIRMWARE_VER);
                }
                break;
            case VER_FIRMWARE:
                if (mHcService != null) {
                    mHcService.dataQuery(HcService.DATA_QUERY_CONFIRM_ECG_MODULE_EXIST);
                }
                break;
        }
    }

    @Override
    public void onBatteryCharging() {
        mHandler.sendEmptyMessage(IDatalifeConstant.CHARGINGHEALTH);
    }

    @Override
    public void onBatteryQuery(int i) {
        this.batteryValue = i;
        mHandler.sendEmptyMessage(IDatalifeConstant.VISIBLEHEALTH);
    }

    @Override
    public void onBatteryFull() {
    }

    @Override
    public void onDeviceInfo(DeviceInfo deviceInfo) {
//            mHcService.dataQuery(HcService.DATA_QUERY_BATTERY_INFO);
        mHandler.sendEmptyMessage(IDatalifeConstant.POWER_BATTERY);
        DeviceId = deviceInfo.getDeviceId().toLowerCase();
    }

    @Override
    public void onReadDeviceInfoFailed() {

    }
}