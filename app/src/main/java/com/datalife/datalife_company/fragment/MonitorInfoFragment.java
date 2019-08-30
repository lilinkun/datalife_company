package com.datalife.datalife_company.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.HealthMonitorActivity;
import com.datalife.datalife_company.activity.RecordActivity;
import com.datalife.datalife_company.adapter.MemberItemAdapter;
import com.datalife.datalife_company.base.BaseHealthFragment;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.interf.OnPageListener;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.linktop.constant.BluetoothState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 进入健康监测仪的首页
 * Created by LG on 2019/7/19.
 */
public class MonitorInfoFragment extends BaseHealthFragment{

    @BindView(R.id.ll_electrocardiogram)
    LinearLayout mElectrocarDiogramLayout;
    @BindView(R.id.ll_search_layout)
    LinearLayout mSearchLayout;
    @BindView(R.id.tv_binddev)
    TextView tv_search;
    @BindView(R.id.ll_dev_top)
    LinearLayout linearLayout;
    @BindView(R.id.ll_dev_bottom)
    LinearLayout bottomLayout;
    @BindView(R.id.tv_my_dev)
    TextView mDevNameTv;
    @BindView(R.id.tv_my_dev_member)
    TextView mDevMemberName;
    @BindView(R.id.tv_connect)
    TextView mTvDevConnect;
    @BindView(R.id.iv_dev_icon)
    ImageView mIcDev;
    @BindView(R.id.iv_member_icon)
    ImageView mIcMember;

    private PopupWindow popupWindow;
    private String mMemberId = "";
    private OnPageListener onPageListener;
    private final int CONNECTHANDLER = 0x1231;

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_health_info;
    }

    @Override
    protected void initEventAndData() {
        mDevNameTv.setText(HealthMonitorActivity.mMachine.getMachineName());
        mMemberId = HealthMonitorActivity.mMemberId;
        mDevMemberName.setText(HealthMonitorActivity.measureFamilyUserInfo.getMember_Name());
        onPageListener = (OnPageListener) getActivity();

        mTvDevConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvDevConnect.getText().toString().equals("点击连接")){
                    onPageListener.connect();
                }
            }
        });
    }

    @OnClick({R.id.ll_temp,R.id.ll_oxygen,R.id.ll_ecg,R.id.ll_heartrate,R.id.tv_history,R.id.rl_dev_member})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_temp:
                if (isHaveMember()) {
                    onPageListener.onPage(IDatalifeConstant.PAGE_TEMP);
                }
                break;

            case R.id.ll_oxygen:
                if (isHaveMember()) {
                    onPageListener.onPage(IDatalifeConstant.PAGE_SPO2H);
                }
                break;

            case R.id.ll_ecg:
                if (isHaveMember()) {
                    onPageListener.onPage(IDatalifeConstant.PAGE_ECG);
                }
                break;

            case R.id.ll_heartrate:
                if (isHaveMember()) {
                    onPageListener.onPage(IDatalifeConstant.PAGE_BP);
                }
                break;
            case R.id.rl_dev_member:
                if (mDevMemberName.getText().toString().equals(getActivity().getResources().getString(R.string.my_member))){

                    return;

                }

                View contentView1 = LayoutInflater.from(getActivity()).inflate(R.layout.member_popup_listview, null);

                ListView listView1 = (ListView) contentView1.findViewById(R.id.lv_member);
                listView1.setVisibility(View.VISIBLE);
                listView1.setAdapter(new MemberItemAdapter(getActivity(),HealthMonitorActivity.machineBindMemberBean,mDevMemberName.getText().toString()));

                popupWindow = new PopupWindow(contentView1,
                        LinearLayout.LayoutParams.MATCH_PARENT,  LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setContentView(contentView1);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(linearLayout);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        bottomLayout.setBackgroundResource(R.color.transparent);
                        mIcMember.setImageResource(R.mipmap.ic_unclick_tip);
                        mDevMemberName.setTextColor(getResources().getColor(R.color.black_text_bg));
                    }
                });
                bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        getMemberId(HealthMonitorActivity.familyUserInfoList.get(position));
                        HealthMonitorActivity.measureFamilyUserInfo = HealthMonitorActivity.familyUserInfoList.get(position);

                        popupWindow.dismiss();

                    }
                });
                mIcMember.setImageResource(R.mipmap.ic_click_tip);
                mDevMemberName.setTextColor(getResources().getColor(R.color.ecg_bg));

                break;

            case R.id.tv_history:

                Bundle bundle = new Bundle();
                bundle.putString(IDatalifeConstant.BUNDLEMEMBERID,mMemberId);
                UIHelper.launcherForResultBundle(getActivity(),RecordActivity.class,22331,bundle);

                break;
        }
    }

    //判断设备是否绑定了成员
    public boolean isHaveMember(){
        if (mDevMemberName.getText().toString().equals(getActivity().getResources().getString(R.string.my_member))){
            toast("请先绑定健康监测仪成员再测量");
            return false;
        }
        return true;
    }

    /**
     * 点击成员名popwindow选择成员
     * @param measureFamilyUserInfo
     */
    public void getMemberId(MeasureFamilyUserInfo measureFamilyUserInfo) {
        this.mMemberId = String.valueOf(measureFamilyUserInfo.getMember_Id());
        HealthMonitorActivity.mMemberId = mMemberId;
        onDataListener.onChageMember();
    }

    /**
     * 设置成员名
     * @param familyUserInfo
     */
    public void setMemberName(MeasureFamilyUserInfo familyUserInfo){
        mDevMemberName.setText(familyUserInfo.getMember_Name());
    }

    public void onBleState(int bleState){
        Message message = new Message();
        message.what = CONNECTHANDLER;
        Bundle bundle = new Bundle();
        switch (bleState) {
            case BluetoothState.BLE_CONNECTED_DEVICE:

                if (getActivity() != null) {
                    mTvDevConnect.setText(getActivity().getResources().getString(R.string.connected));
                }else {
                    mTvDevConnect.setText("设备已连接");
                }

                break;

            case BluetoothState.BLE_CONNECTING_DEVICE:
                if (getActivity() != null) {
                    mTvDevConnect.setText(getActivity().getResources().getString(R.string.connecting));
                }else {
                    mTvDevConnect.setText( "设备连接中");
                }
                break;

            case BluetoothState.BLE_CLOSED:
                if (getActivity() != null) {
                    mTvDevConnect.setText(getActivity().getResources().getString(R.string.openble));
                }else {
                    mTvDevConnect.setText("打开蓝牙");
                }
                break;

            case BluetoothState.BLE_OPENED_AND_DISCONNECT:
                if (getActivity() != null) {
                    mTvDevConnect.setText(getActivity().getResources().getString(R.string.connecting));
                }else {
                    mTvDevConnect.setText("点击连接");
                }

                break;
        }
    }
}
