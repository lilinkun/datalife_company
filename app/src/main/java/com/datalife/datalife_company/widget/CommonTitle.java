package com.datalife.datalife_company.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.MemberItemAdapter;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2019/7/24.
 */
public class CommonTitle extends LinearLayout implements View.OnClickListener{

    private RelativeLayout mRlMyDev;
    private RelativeLayout mRlDevMember;
    private LinearLayout commonTitleLayout;
    private TextView mTvMyDev;
    private TextView mTvDevMember;
    private ImageView mIcMachine;
    private ImageView mIcMember;

    private PopupWindow popupWindow;
    private Context mContext;
    private ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
    private ArrayList<MachineBean> machineBeans = null;
    private ArrayList<MeasureFamilyUserInfo> familyUserInfoArrayList = null;
    private LinearLayout bottomLayout;
    private Handler myHandler;

    public CommonTitle(Context context) {
        super(context);
        initView(context);
    }

    public CommonTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CommonTitle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        familyUserInfoArrayList = (ArrayList<MeasureFamilyUserInfo>) DBManager.getInstance(mContext).queryMeasureFamilyUserInfoList();
        this.mContext = context;

        View v = LayoutInflater.from(mContext).inflate(R.layout.commom_title,null);
        commonTitleLayout = (LinearLayout) v.findViewById(R.id.tv_common_title);
        mRlMyDev = (RelativeLayout) v.findViewById(R.id.rl_my_dev);
        mRlDevMember = (RelativeLayout) v.findViewById(R.id.rl_dev_member);
        mTvMyDev = (TextView) v.findViewById(R.id.tv_my_dev);
        mTvDevMember = (TextView) v.findViewById(R.id.tv_my_dev_member);
        mIcMember = (ImageView) v.findViewById(R.id.ic_member_click);
        mIcMachine = (ImageView) v.findViewById(R.id.ic_machine_click);
        mRlDevMember.setOnClickListener(this);
        commonTitleLayout.setOnClickListener(this);
        mRlMyDev.setOnClickListener(this);
        this.addView(v ,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        machineBean();

        if (machineBeans != null  && machineBeans.size() > 0) {
            setDevMemberName(machineBeans.get(0));
        }
    }

    private void machineBean(){
        machineBeans = (ArrayList<MachineBean>) DBManager.getInstance(mContext).queryMachineList();
        ArrayList<MachineBean> machineBeans1 = new ArrayList<>();
        for (MachineBean machineBean : machineBeans){
            if (machineBean.getMachineName().startsWith("SWAN") || machineBean.getMachineName().startsWith("SWAN")){
                machineBeans1.add(machineBean);
            }
        }
        machineBeans = machineBeans1;
    }

    public void setDevMemberName(MachineBean devMemberName){
        mTvMyDev.setText(devMemberName.getMachineName());

        if (familyUserInfoArrayList != null){
            mTvDevMember.setText(familyUserInfoArrayList.get(0).getMember_Name());
        }

    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.rl_dev_member:
                if (machineBindMemberBeans != null) {
                    View contentView = LayoutInflater.from(mContext).inflate(R.layout.member_popup_listview, null);

                    mTvDevMember.setTextColor(getResources().getColor(R.color.bg_toolbar_title));
                    mIcMember.setImageDrawable(getResources().getDrawable(R.mipmap.ic_click_tip));
                    ListView listView = (ListView) contentView.findViewById(R.id.lv_member);
                    listView.setVisibility(View.VISIBLE);
                    MemberItemAdapter memberItemAdapter = new MemberItemAdapter(mContext, machineBindMemberBeans, mTvDevMember.getText().toString());
                    listView.setAdapter(memberItemAdapter);

                    popupWindow = new PopupWindow(contentView,
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.setContentView(contentView);
                    popupWindow.setOutsideTouchable(true);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.showAsDropDown(commonTitleLayout);
                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            bottomLayout.setBackgroundResource(R.color.transparent);
                            mTvDevMember.setTextColor(Color.WHITE);
                            mIcMember.setImageDrawable(getResources().getDrawable(R.mipmap.ic_unclick_tip));
                        }
                    });
                    bottomLayout.setBackgroundResource(R.drawable.ic_transparent_bg);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            mTvDevMember.setText(familyUserInfoArrayList.get(position).getMember_Name());

                            changeMember(familyUserInfoArrayList.get(position));

                            popupWindow.dismiss();

                        }
                    });
                } else {
                    UToast.show(mContext, "暂未连接设备");
                }
                break;
        }
    }

    public void setLayout(View bottomLayout){
        this.bottomLayout = (LinearLayout) bottomLayout;
    }

    public void setHandler(Handler handler){
        this.myHandler = handler;
    }

    public void setDevName(MachineBean devName, String memberid){
        mTvDevMember.setText(memberid);
        mTvMyDev.setText(devName.getMachineName());
    }

    //设备绑定成员变化
    public void changeMember(MeasureFamilyUserInfo measureFamilyUserInfo){
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("memberid",String.valueOf(measureFamilyUserInfo.getMember_Id()));
        bundle.putSerializable("measureFamilyUserInfo",measureFamilyUserInfo);
        message.setData(bundle);
        message.what = IDatalifeConstant.COMMOMHANDLERMEMBER;
        if (myHandler != null) {
            myHandler.sendMessage(message);
        }
    }
}
