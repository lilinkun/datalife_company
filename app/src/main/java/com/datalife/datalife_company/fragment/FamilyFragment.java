package com.datalife.datalife_company.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.LoginActivity;
import com.datalife.datalife_company.activity.SimplebackActivity;
import com.datalife.datalife_company.adapter.FamilyAdapter;
import com.datalife.datalife_company.adapter.FamilyRecyclerAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseFragment;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.contract.FamilyContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.presenter.FamilyPresenter;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/16.
 */
public class FamilyFragment extends BaseFragment implements FamilyContract {

    //    @BindView(R.id.lv_family)
//    ListView mLvFamily;
    @BindView(R.id.lv_family)
    RecyclerView mLvFamily;
    @BindView(R.id.ll_add_member_center)
    LinearLayout ll_add_member_center;
    @BindView(R.id.ll_add_member)
    LinearLayout ll_add_member;

    List<FamilyUserInfo> familyUserInfos = null;
    FamilyAdapter familyAdapter = null;
    FamilyRecyclerAdapter familyRecyclerAdapter = null;
    private FamilyPresenter familyPresenter = new FamilyPresenter();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x12312:
                    FamilyUserInfo familyUserInfo = (FamilyUserInfo) msg.getData().getSerializable("familyUserInfo");

                    break;
            }
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_family;
    }

    @Override
    protected void initEventAndData() {
        familyUserInfos = DBManager.getInstance(getActivity()).queryFamilyUserInfoList();
        familyAdapter = new FamilyAdapter(getActivity(),familyUserInfos);

        if (familyUserInfos.size() == 0){
            ll_add_member_center.setVisibility(View.VISIBLE);
            ll_add_member.setVisibility(View.GONE);
        }

        familyPresenter.onCreate(getActivity(),this);

        familyPresenter.getFamilyDataList(ProApplication.SESSIONID,"50","1");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLvFamily.setLayoutManager(linearLayoutManager);
        if (familyUserInfos != null && familyUserInfos.size() > 0) {
            familyRecyclerAdapter = new FamilyRecyclerAdapter(familyUserInfos, handler, this);
            mLvFamily.setAdapter(familyRecyclerAdapter);
        }

    }

    @OnClick({R.id.ll_add_member,R.id.ll_add_member_center})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.ll_add_member_center:
            case R.id.ll_add_member:
                UIHelper.showSimpleBackForResult2(this, SimplebackActivity.RESULT_ADDUSER, SimpleBackPage.ADDUSER);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == SimplebackActivity.RESULT_ADDUSER){
                FamilyUserInfo familyUserInfo = (FamilyUserInfo)data.getSerializableExtra("familyUserInfo");
//                familyUserInfos.add(familyUserInfo);
//                familyAdapter.notifyDataSetChanged();
//                familyRecyclerAdapter.notifyDataSetChanged();
//                familyPresenter.getFamilyDataList(ProApplication.SESSIONID,"50","1");

                familyUserInfos = DBManager.getInstance(getActivity()).queryFamilyUserInfoList();

                familyRecyclerAdapter.setData(familyUserInfos);
                familyRecyclerAdapter.notifyDataSetChanged();

                if (familyUserInfo != null){
                    ll_add_member_center.setVisibility(View.GONE);
                    ll_add_member.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> listResultBean) {
        DBManager.getInstance(getActivity()).deleteAllFamilyUserInfoBean();
        for (int i = 0;i < listResultBean.size();i++){
            DBManager.getInstance(getActivity()).insertMember(listResultBean.get(i));
        }
        if (familyUserInfos != null && familyUserInfos.size() > 0) {
            if (familyRecyclerAdapter != null) {
                familyRecyclerAdapter.setData(familyUserInfos);
                familyRecyclerAdapter.notifyDataSetChanged();
            }

                ll_add_member_center.setVisibility(View.GONE);
                ll_add_member.setVisibility(View.VISIBLE);

        }else {
            familyUserInfos = listResultBean;
            if (familyRecyclerAdapter == null){
                familyRecyclerAdapter = new FamilyRecyclerAdapter(familyUserInfos, handler, this);
                mLvFamily.setAdapter(familyRecyclerAdapter);
            }
        }
    }

    @Override
    public void onBackFamilyListDataFail(String str) {
        if (str.equals("登录已失效")){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(IDatalifeConstant.LOGIN, Context.MODE_PRIVATE);
            String sre = sharedPreferences.getString("account","");
            if (sharedPreferences != null && sre.trim().length()>0) {
                Intent intent = new Intent();
                intent.putExtra("restart", true);
                intent.setClass(getActivity(), LoginActivity.class);
                startActivityForResult(intent, IDatalifeConstant.INTENT_LOGIN);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onBindDataSuccess(String str) {
        toast(str);
        familyPresenter.getFamilyDataList(ProApplication.SESSIONID,"50","1");
    }

    @Override
    public void onBindDataFail(String str) {
        toast(str);
    }
}
