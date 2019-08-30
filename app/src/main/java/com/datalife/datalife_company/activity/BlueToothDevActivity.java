package com.datalife.datalife_company.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.BlueToothAdapter;
import com.datalife.datalife_company.adapter.MachineAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.BlueType;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.contract.BlueToothDevContract;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.presenter.BlueToothDevPresenter;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.LogUtil;
import com.datalife.datalife_company.util.SearchBlueToothDev;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.util.UToast;
import com.datalife.datalife_company.widget.CustomTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aicare.net.cn.iweightlibrary.utils.AicareBleConfig;
import aicare.net.cn.iweightlibrary.utils.ParseData;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加设备
 * Created by LG on 2019/7/18.
 */
public class BlueToothDevActivity extends BaseActivity implements BlueToothDevContract{

    @BindView(R.id.lv_bluetooth)
    ListView mBluetoothLv;
    @BindView(R.id.lv_searched_bluetooth)
    ListView mSearchLv;
    @BindView(R.id.tv_connect_equip)
    TextView mEquipNameTv;
    @BindView(R.id.rl_binding_equit)
    RelativeLayout mBindingEquitLayout;
    @BindView(R.id.custom_title)
    CustomTitleBar customTitleBar;

    ArrayList<MachineBean> machineBeans = new ArrayList<>();
    private MachineAdapter machineAdapter = null;
    private boolean isSupportBLE;
    private BluetoothAdapter mAdapter;
    private BlueToothAdapter blueToothAdapter = null;
    public HashMap<String,BluetoothDevice> bls = new HashMap<>();
    private ArrayList<BlueType> blueTypes = new ArrayList<>();
    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<>();
    private ArrayList<String> arraynames = new ArrayList<>();
    private String sn = "";
    private String mCurrentName;
    private String TAG = "BlueToothDevActivity";
    private SearchBlueToothDev searchBlueToothDev;

    BlueToothDevPresenter blueToothDevPresenter = new BlueToothDevPresenter();

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
                if (msg.what == IDatalifeConstant.MESSAGESERVICE){
                    mSearchLv.setVisibility(View.VISIBLE);
                    if (blueToothAdapter == null){

                        mDeviceList.add((BluetoothDevice)(msg.getData().getParcelable("device")));
                        BlueType blueType = new BlueType(msg.getData().getString("name"),msg.getData().getInt("type"),(BluetoothDevice)(msg.getData().getParcelable("device")));
                        blueTypes.add(blueType);

                        blueToothAdapter = new BlueToothAdapter(BlueToothDevActivity.this,mDeviceList,myHandler,blueTypes);
                        mSearchLv.setAdapter(blueToothAdapter);
                    }else {
                        blueToothAdapter.setMachineList(mDeviceList,blueTypes);
                    }
                }else if (msg.what == IDatalifeConstant.MESSAGEMACHINE) {
                    final int position = msg.getData().getInt("position");

                    mCurrentName = arraynames.get(position);
                    int type = blueTypes.get(position).getBlueType();
                    if (type == IDatalifeConstant.TYPE_HEALTH) {
                        sn = "C4" + arraynames.get(position).substring(5);
                    } else if (type == IDatalifeConstant.TYPE_FAT) {
                        sn = mDeviceList.get(position).getAddress();
                        mCurrentName = IDatalifeConstant.SWAN + "-" + sn;
                    } else if (type == IDatalifeConstant.TYPE_TOOTH) {
                        sn = mDeviceList.get(position).getAddress();
                        mCurrentName = IDatalifeConstant.ZSONIC + "-" + sn;
                    }


                    if (type == IDatalifeConstant.TYPE_TOOTH) {
                        Bundle bundle = new Bundle();
                        bundle.putString("sn", sn);
                        bundle.putString("MachineName", mCurrentName);
//                        UIHelper.launcherForResultBundle(BlueToothDevActivity.this, ToothBindActivity.class, mRequestCode, bundle);
                        searchBlueToothDev.stopSearchBlueDev();
                        return;
                    }

                    new AlertDialog.Builder(BlueToothDevActivity.this).setMessage("确定是否绑定机器?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            blueToothDevPresenter.bindToothMachine(mCurrentName, sn, ProApplication.SESSIONID);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_bluetooth;
    }

    @Override
    protected void initEventAndData() {

        blueToothDevPresenter.onCreate(this,this);

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineList();
        if (machineBeans != null && machineBeans.size() > 0){
            machineAdapter = new MachineAdapter(this,machineBeans);
            mBluetoothLv.setAdapter(machineAdapter);
            mBluetoothLv.setVisibility(View.VISIBLE);
            int totalHeight = 0;
            if (machineBeans.size() >= 3){
                for(int i=0;i<3;i++) {
                    View viewItem = machineAdapter.getView(i, null, mBluetoothLv);
                    viewItem.measure(0, 0);
                    totalHeight += viewItem.getMeasuredHeight();
                }
                ViewGroup.LayoutParams layoutParams = mBluetoothLv.getLayoutParams();
                layoutParams.height = totalHeight + 60;
                mBluetoothLv.setLayoutParams(layoutParams);
            }
            mBluetoothLv.setAdapter(machineAdapter);
        }

        initBluetooth();

    }

    /**
     * 初始化蓝牙
     */
    public void initBluetooth() {
        arraynames.clear();
        mDeviceList.clear();
        if (blueToothAdapter != null){
            blueToothAdapter.notifyDataSetChanged();
            bls.clear();
        }

        searchBlueToothDev = SearchBlueToothDev.getInstance(myHandler);

        searchBlueToothDev.searchDev(this);

    }


    @OnClick({R.id.btn_bluetooth_search,R.id.tv_bind_member,R.id.iv_head_left})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_bluetooth_search:

                initBluetooth();
                break;

            case R.id.tv_bind_member:
                Bundle bundle = new Bundle();
                for (int i = 0;i<machineBeans.size();i++) {
                    String str = mEquipNameTv.getText().toString();
                    if (machineBeans.get(i).getMachineName().equals(str)){
                        bundle.putSerializable("machine", machineBeans.get(i));
                    }
                }
                UIHelper.showSimpleBackBundleForResult(this, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER,bundle);
                break;

            case R.id.iv_head_left:

                setResult(Activity.RESULT_OK);
                finish();

                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 权限返回方法
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == IDatalifeConstant.REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                searchBlueToothDev.searchDev(this);
            } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == IDatalifeConstant.REQUEST_OPEN_BT){
                searchBlueToothDev.searchDev(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        searchBlueToothDev.stopSearchBlueDev();
        super.onDestroy();
    }

    @Override
    public void bindSuccess() {
        blueToothDevPresenter.getDevMachineInfo("1","15",ProApplication.SESSIONID);
    }

    @Override
    public void bindFail(String msg) {
        toast(msg);
    }

    @Override
    public void getMachineSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultMachines) {
        ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBindBean = resultMachines;
        DBManager.getInstance(this).deleteAllMachineBean();
        for (int i = 0;i<machineBindBean.size();i++) {
            try{
                MachineBean machineBean = new MachineBean();
                machineBean.setCreateDate(machineBindBean.get(i).getCreateDate());
                machineBean.setMachineBindId(machineBindBean.get(i).getMachineBindId());
                machineBean.setMachineId(machineBindBean.get(i).getMachineId());
                machineBean.setMachineName(machineBindBean.get(i).getMachineName());
                machineBean.setMachineSn(machineBindBean.get(i).getMachineSn());
                machineBean.setMachineStatus(machineBindBean.get(i).getMachineStatus());
                machineBean.setUser_id(machineBindBean.get(i).getUser_id());
                machineBean.setUser_name(machineBindBean.get(i).getUser_name());
                DBManager.getInstance(this).insertMachine(machineBean);
            }catch (SQLiteConstraintException e){
                toast(e.getMessage());
                Log.e("error:" , e.getMessage());
            }
        }

        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(this).queryMachineList();
        if (machineAdapter == null){
            machineAdapter = new MachineAdapter(this,machineBeans);
            mBluetoothLv.setAdapter(machineAdapter);
            mBluetoothLv.setVisibility(View.VISIBLE);
        }else {
            machineAdapter.setMachineBean(machineBeans);
            machineAdapter.notifyDataSetChanged();
        }
        for(int i = 0;i<machineBeans.size();i++) {
            if (machineBeans.get(i).getMachineName().equals(mCurrentName)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("machine", machineBeans.get(i));
                UIHelper.showSimpleBackBundleForResult(this, SimplebackActivity.RESULT_BINDMEMBER, SimpleBackPage.BINDMEMBER, bundle);
            }
        }
    }

    @Override
    public void getMachineFail(String msg) {
        if (msg.contains("查无数据")){
            DBManager.getInstance(this).deleteAllMachineBean();
        }
    }
}
