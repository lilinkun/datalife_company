package com.datalife.datalife_company.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.bean.LastFatMeasureDataBean;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.contract.FatContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.presenter.FatPresenter;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.util.UToast;
import com.datalife.datalife_company.widget.CommonLayout;
import com.datalife.datalife_company.widget.CommonTitle;
import com.datalife.datalife_company.widget.DialProgress;
import com.datalife.datalife_company.widget.Eyes;

import java.util.ArrayList;
import java.util.List;

import aicare.net.cn.iweightlibrary.bleprofile.BleProfileService;
import aicare.net.cn.iweightlibrary.entity.BodyFatData;
import aicare.net.cn.iweightlibrary.entity.User;
import aicare.net.cn.iweightlibrary.entity.WeightData;
import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.L;
import aicare.net.cn.iweightlibrary.wby.WBYService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LG on 2019/7/24.
 */

public class FatActivity extends BleProfileServiceReadyActivity implements FatContract{

    private WBYService.WBYBinder binder;
    private final static String TAG = "FatActivity";
    private Unbinder unbinder = null;

    @BindView(R.id.commonlayout)
    CommonLayout mCommonLayout;
    @BindView(R.id.mcctitle)
    CommonTitle mCommonChangeTitle;
    @BindView(R.id.btn_connect)
    TextView mConnectBtn;
    @BindView(R.id.dial_progress_bar)
    DialProgress mDialProgress;
    @BindView(R.id.pg_weight_value)
    TextView mWeightValue;
    @BindView(R.id.ll_fat_test)
    LinearLayout mTestFatLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_connected_equit)
    TextView mConnectedEquit;
    @BindView(R.id.iv_head_right)
    ImageView mRoundImageView;
    @BindView(R.id.pb_loadding)
    ProgressBar mProgressBar;

    private BluetoothDevice device;
    private ArrayList<MachineBean> machineBeans;
    private MachineBean machineBean;
    private List<MeasureFamilyUserInfo> measureFamilyUserInfoList;
    MeasureFamilyUserInfo measureFamilyUserInfo = null;
    private String mMemberId = "";
    private FatPresenter fatPresenter = new FatPresenter();
    private final int FATSYCNUSER = 0x987;
    private BodyFatData bodyFatData = null;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FATSYCNUSER:
                    mCommonLayout.onGetFatData(bodyFatData);
                    break;

                case IDatalifeConstant.COMMOMHANDLERMEMBER:
                    measureFamilyUserInfo = (MeasureFamilyUserInfo) msg.getData().getSerializable("measureFamilyUserInfo");
                    mMemberId = msg.getData().getString("memberid");
                    fatPresenter.getNewMeasureInfo(mMemberId,IDatalifeConstant.MACHINE_FAT,ProApplication.SESSIONID);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fat);

        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
        unbinder = ButterKnife.bind(this);

        fatPresenter.onCreate(this,this);

        mCommonLayout.setHandler(myHandler);
        startScan();
        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineList();
        measureFamilyUserInfoList = DBManager.getInstance(this).queryMeasureFamilyUserInfoList();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        if (getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER) != null && getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER).getSerializable("machine") != null){
            this.machineBean = (MachineBean) (getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER).getSerializable("machine"));
        }

        if (measureFamilyUserInfoList != null && measureFamilyUserInfoList.size() != 0){

            mCommonChangeTitle.setHandler(myHandler);
            mCommonChangeTitle.setLayout(mTestFatLayout);

            mRoundImageView.setVisibility(View.VISIBLE);
            mRoundImageView.setImageResource(R.mipmap.ic_fat_history);

        }
        measureFamilyUserInfo = measureFamilyUserInfoList.get(0);
        mMemberId = String.valueOf(measureFamilyUserInfo.getMember_Id());


        fatPresenter.getNewMeasureInfo(mMemberId,machineBean.getMachineId(),ProApplication.SESSIONID);

        mCommonChangeTitle.setDevName(machineBean,measureFamilyUserInfo.getMember_Name());

        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams)mDialProgress.getLayoutParams();
        layoutParams.width = width*3/5;
        mDialProgress.setLayoutParams(layoutParams);
        mConnectBtn.setVisibility(View.GONE);
        mConnectBtn.setText(getString(R.string.connecting_equit));
        mConnectedEquit.setText(getString(R.string.connecting_equit));

        mConnectedEquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDeviceConnected() && mConnectedEquit.getText().toString().equals(getString(R.string.click_connect))){
                    stopScan();
                    startScan();
                }
            }
        });

    }

    @OnClick({R.id.iv_head_left,R.id.btn_connect,R.id.iv_head_right})
    public void onClick(View v){
        switch (v.getId()){

            case R.id.iv_head_left:
                finish();
                break;

            case R.id.btn_connect:

                if (mConnectBtn.getText().toString().equals(getString(R.string.click_connect))) {
                    startScan();
                }else if (mConnectBtn.getText().toString().equals(getString(R.string.connected))){
                    binder.disconnect();
                }

                break;

            case R.id.iv_head_right:

                Bundle bundle = new Bundle();
                bundle.putString(IDatalifeConstant.BUNDLEMEMBERID, mMemberId);
                bundle.putSerializable(IDatalifeConstant.FAMILYUSERINFO, measureFamilyUserInfo);
                bundle.putString("MachineId", machineBean.getMachineBindId());
                UIHelper.launcherForResultBundle(this, FatRecordActivity.class,333,bundle);

                break;
        }
    }

    @Override
    protected void onError(String errMsg, int errCode) {

    }

    @Override
    protected void onGetWeightData(WeightData weightData) {

        mCommonLayout.onWeight(weightData);
        mDialProgress.setValue((float) (weightData.getWeight()));
        mWeightValue.setText(String.valueOf(weightData.getWeight()));
    }

    @Override
    protected void onGetSettingStatus(int status) {
        L.e(TAG, "SettingStatus = " + status);
        switch (status) {
            case AicareBleConfig.SettingStatus.NORMAL:
                showInfo(getString(R.string.settings_status, getString(R.string.normal)));
                break;
            case AicareBleConfig.SettingStatus.LOW_POWER:
                showInfo(getString(R.string.settings_status, getString(R.string.low_power)));
                break;
            case AicareBleConfig.SettingStatus.LOW_VOLTAGE:
                showInfo(getString(R.string.settings_status, getString(R.string.low_voltage)));
                break;
            case AicareBleConfig.SettingStatus.ERROR:
                showInfo(getString(R.string.settings_status, getString(R.string.error)));
                break;
            case AicareBleConfig.SettingStatus.TIME_OUT:
                showInfo(getString(R.string.settings_status, getString(R.string.time_out)));
                break;
            case AicareBleConfig.SettingStatus.UNSTABLE:
                showInfo(getString(R.string.settings_status, getString(R.string.unstable)));
                break;
            case AicareBleConfig.SettingStatus.SET_UNIT_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.set_unit_success)));
                break;
            case AicareBleConfig.SettingStatus.SET_UNIT_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.set_unit_failed)));
                break;
            case AicareBleConfig.SettingStatus.SET_TIME_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.set_time_success)));
                break;
            case AicareBleConfig.SettingStatus.SET_TIME_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.set_time_failed)));
                break;
            case AicareBleConfig.SettingStatus.SET_USER_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.set_user_success)));
                break;
            case AicareBleConfig.SettingStatus.SET_USER_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.set_user_failed)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_LIST_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_list_success)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_LIST_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_list_failed)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_SUCCESS:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_success)));
                break;
            case AicareBleConfig.SettingStatus.UPDATE_USER_FAILED:
                showInfo(getString(R.string.settings_status, getString(R.string.update_user_failed)));
                break;
            case AicareBleConfig.SettingStatus.NO_HISTORY:
                showInfo(getString(R.string.settings_status, getString(R.string.no_history)));
                break;
            case AicareBleConfig.SettingStatus.HISTORY_START_SEND:
                showInfo(getString(R.string.settings_status, getString(R.string.history_start_send)));
                break;
            case AicareBleConfig.SettingStatus.HISTORY_SEND_OVER:
                showInfo(getString(R.string.settings_status, getString(R.string.history_send_over)));
                break;
            case AicareBleConfig.SettingStatus.NO_MATCH_USER:
                showInfo(getString(R.string.settings_status, getString(R.string.no_match_user)));
                break;
            case AicareBleConfig.SettingStatus.ADC_MEASURED_ING:
                showInfo(getString(R.string.settings_status, getString(R.string.adc_measured_ind)));
                syncDate();
                break;
            case AicareBleConfig.SettingStatus.ADC_ERROR:
                showInfo(getString(R.string.settings_status, getString(R.string.adc_error)));
                break;
            case AicareBleConfig.SettingStatus.UNKNOWN:
                showInfo(getString(R.string.settings_status, getString(R.string.unknown)));
                break;
            case AicareBleConfig.SettingStatus.REQUEST_DISCONNECT:
                showInfo(getString(R.string.settings_status, getString(R.string.request_disconnect)));
                break;
        }
    }

    private void showInfo(String str){
        Log.i(TAG,"SettingStatus = " + str);
    }

    @Override
    protected void onGetResult(int index, String result) {

    }

    @Override
    protected void onGetFatData(boolean isHistory, BodyFatData bodyFatData) {
        if(bodyFatData!= null){

            if (bodyFatData.getHeight() == 0){
                for (int i = 0;i < measureFamilyUserInfoList.size();i++){
                    if (measureFamilyUserInfoList.get(i).getMember_Id() == Integer.valueOf(mMemberId)){
                        measureFamilyUserInfo = measureFamilyUserInfoList.get(i);
                        bodyFatData.setSex(Integer.valueOf(measureFamilyUserInfo.getMember_Sex()));
                        bodyFatData.setAge(IDatalifeConstant.getAgeByBirthDay(measureFamilyUserInfo.getMember_DateOfBirth()));
//                        bodyFatData.setWeight(familyUserInfo.getMember_Stature());
                        bodyFatData.setHeight((int)measureFamilyUserInfo.getMember_Stature());
                    }
                }
            }

            mCommonLayout.onGetFatData(bodyFatData);

            if (bodyFatData.getBmi() == 0){
                UToast.show(this,"未得出数据,请再次测量");
                return;
            }
            IDatalifeConstant.startAlarm(this);

            double value = 0;

            if (bodyFatData.getDbw() == 0){
                value = bodyFatData.getWeight() *(1-bodyFatData.getBfr()/100);
            }else {
                value = bodyFatData.getDbw();
            }

            fatPresenter.putfattest(mMemberId + "", machineBean.getMachineBindId(), device.getAddress(), DeviceData.getUniqueId(this), bodyFatData.getHeight() + "", bodyFatData.getAge() + "",
                    bodyFatData.getWeight() + "", bodyFatData.getBmi() + "", bodyFatData.getBfr() + "", bodyFatData.getRom() + "",
                    bodyFatData.getVwc() + "", value + "", bodyFatData.getBm() + "", bodyFatData.getUvi() + "", bodyFatData.getBmr() + "",
                    bodyFatData.getSex() + "");
//            Toast.makeText(this,s + " ",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,bodyFatData.toString() + " ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onServiceBinded(WBYService.WBYBinder binder) {
        this.binder = binder;
    }

    @Override
    protected void onServiceUnbinded() {
        this.binder = null;
    }

    @Override
    protected void getAicareDevice(BluetoothDevice device, int rssi, int flag) {
        this.device = device;
        for (int i = 0;i<machineBeans.size();i++) {
            if (machineBean != null) {
                if (device.getAddress().equals(machineBean.getMachineSn())) {
                    startConnect(device.getAddress());
                }
            }else {
                if (device.getAddress().equals(machineBeans.get(i).getMachineSn())) {
                    machineBean = machineBeans.get(i);
                    startConnect(device.getAddress());
                }
            }
        }
    }

    @Override
    protected void onStateChanged(String deviceAddress, int state) {
        super.onStateChanged(deviceAddress, state);
        switch (state) {
            case BleProfileService.STATE_CONNECTED:
//                Toast.makeText(this,getString(R.string.state_connected, deviceAddress),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_connected, deviceAddress));
                syncDate();

                mConnectBtn.setText(getString(R.string.connected));
                mConnectedEquit.setText(getString(R.string.connected));

                break;
            case BleProfileService.STATE_DISCONNECTED:
//                Toast.makeText(this,getString(R.string.state_disconnected),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_disconnected));
                mConnectBtn.setText(R.string.click_connect);
                mConnectedEquit.setText(getString(R.string.connecting));
                startScan();
                break;
            case BleProfileService.STATE_SERVICES_DISCOVERED:
//                Toast.makeText(this,getString(R.string.state_service_discovered),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_service_discovered));
                break;
            case BleProfileService.STATE_INDICATION_SUCCESS:
//                Toast.makeText(this,getString(R.string.state_indication_success),Toast.LENGTH_LONG).show();
                showInfo(getString(R.string.state_indication_success));
                syncDate();
                if (this.binder != null) {
                    this.binder.queryBleVersion();
                }
                break;
        }
    }

    private void syncDate(){
        for (int i = 0;i<measureFamilyUserInfoList.size();i++){
            if(measureFamilyUserInfoList.get(i).getMember_Id() == Integer.parseInt(mMemberId)){
                measureFamilyUserInfo= measureFamilyUserInfoList.get(i);
            }
        }
        int age = 0;
        try{
            String str = measureFamilyUserInfo.getMember_DateOfBirth();
            age = IDatalifeConstant.getAge(IDatalifeConstant.parse(measureFamilyUserInfo.getMember_DateOfBirth()));
        }catch  (Exception e) {
            e.printStackTrace();
        }
        int sex = Integer.parseInt(measureFamilyUserInfo.getMember_Sex()) + 1;

        User user = new User(age, sex, age, (int)(measureFamilyUserInfo.getMember_Stature()), (int)measureFamilyUserInfo.getMember_Weight(), 551);
        binder.syncUser(user);
    }

    @Override
    public void onLastSuccess(LastFatMeasureDataBean<MeasureRecordBean> measureRecordBeans) {
        mProgressBar.setVisibility(View.GONE);
        if (measureRecordBeans != null) {
            MeasureRecordBean measureRecordBean = measureRecordBeans.getProject6();
            if (measureRecordBean != null) {
                BodyFatData bodyFatData = new BodyFatData();
                bodyFatData.setWeight(Double.parseDouble(measureRecordBean.getCheckValue3()));
                bodyFatData.setBmi(Double.parseDouble(measureRecordBean.getCheckValue4()));
                bodyFatData.setBfr(Double.parseDouble(measureRecordBean.getCheckValue5()));
                bodyFatData.setRom(Double.parseDouble(measureRecordBean.getCheckValue6()));
                bodyFatData.setVwc(Double.parseDouble(measureRecordBean.getCheckValue7()));
                bodyFatData.setDbw(Double.parseDouble(measureRecordBean.getCheckValue8()));
                bodyFatData.setBm(Double.parseDouble(measureRecordBean.getCheckValue9()));

                if (measureRecordBean.getCheckValue10().equals("0.0")) {
                    bodyFatData.setUvi(0);
                } else {
                    double v = Double.parseDouble(measureRecordBean.getCheckValue10());
                    bodyFatData.setUvi((int) v);
                }
                bodyFatData.setBmr(Double.parseDouble(measureRecordBean.getCheckValue11()));
                if (mCommonLayout != null) {
                    mCommonLayout.onGetFatData(bodyFatData);
                }
            }else {
                mCommonLayout.onGetFatData(null);
            }
        }
    }

    @Override
    public void onLastFail(String msg) {
        UToast.show(this,msg);
    }

    @Override
    protected void onDestroy() {
        stopScan();
        if (isDeviceConnected()) {
            this.binder.disconnect();
        }
        super.onDestroy();
    }
}
