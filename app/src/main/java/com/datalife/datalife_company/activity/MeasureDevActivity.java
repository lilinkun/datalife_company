package com.datalife.datalife_company.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.adapter.MainMachineAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.contract.MeasureDevContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.presenter.MeasureDevPresenter;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.widget.Eyes;
import com.datalife.datalife_company.widget.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LG on 2019/7/17.
 */

public class MeasureDevActivity extends BaseActivity implements MeasureDevContract, MainMachineAdapter.OnItemClickListener {

    @BindView(R.id.rv_equit)
    RecyclerView recyclerView;
    @BindView(R.id.ll_no_net)
    LinearLayout mNoNetLayout;

    private MeasureDevPresenter measureDevPresenter = new MeasureDevPresenter();
//    public List<FamilyUserInfo> familyUserInfos= null;
    private List<MachineBean> machineBeans = null;
    private String memberid ="";
    private MainMachineAdapter mainMachineAdapter;
    private ArrayList<MeasureFamilyUserInfo> measureFamilyUserInfos = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_measure_dev;
    }

    @Override
    protected void initEventAndData() {
        Eyes.translucentStatusBar(this);
        measureDevPresenter.onCreate(this,this);

        ProApplication.MEASURE_SESSIONID = IDatalifeConstant.getMeasureUniqueId(this);

        measureDevPresenter.login("liguo","123456", ProApplication.MEASURE_SESSIONID);

//        familyUserInfos = DBManager.getInstance(this).queryFamilyUserInfoList();

        if (getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER) != null && getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER).getString("memberid") != null){
            memberid = getIntent().getBundleExtra(IDatalifeConstant.BUNDLEMEMBER).getString("memberid");
        }

        machineBeans = DBManager.getInstance(this).queryMachineList();
//        Collections.reverse(machineBeans);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        int spanCount = 2; // 2 columns
        int spacing = 20; // 50px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        recyclerView.setLayoutManager(gridLayoutManager);

        mainMachineAdapter = new MainMachineAdapter(this,machineBeans);
        mainMachineAdapter.setItemClickListener(this);
        recyclerView.setAdapter(mainMachineAdapter);

//        homePagePresenter.getDevMachineInfo("1","20", ProApplication.SESSIONID);

        if (machineBeans != null){
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position) {
        if (machineBeans.get(position).getMachineName().startsWith("HC02")){
            healthTest(machineBeans.get(position));
        }else if (machineBeans.get(position).getMachineName().startsWith("SWAN")){
            fatTest(machineBeans.get(position));
        }else if (machineBeans.get(position).getMachineName().startsWith("ZSON")){

        }
    }


    //点击健康监测仪
    private void healthTest(MachineBean machine){
        Bundle bundle = new Bundle();
        bundle.putSerializable("machine",machine);
        onJumpTest(HealthMonitorActivity.class, 111,bundle);
    }


    //点击体脂称
    private void fatTest(MachineBean machine){
        Bundle bundle = new Bundle();
        bundle.putSerializable("machine",machine);
        onJumpTest( FatActivity.class, 121,bundle);
    }

    private void onJumpTest(Class<?> targetActivity, int requestCode, Bundle bundle){
        SharedPreferences mySharedPreferences = this.getSharedPreferences(IDatalifeConstant.LOGIN, MODE_PRIVATE);
        if (mySharedPreferences.getBoolean(IDatalifeConstant.LOGIN,false) == false){
            Intent intent = new Intent();
            intent.setClass(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        UIHelper.launcherForResultBundle(this, targetActivity, requestCode,bundle);

    }

    @Override
    public void loginSuccess(LoginBean loginBean) {
        measureDevPresenter.getFamilyDataList(ProApplication.MEASURE_SESSIONID,"200","1");
    }

    @Override
    public void loginFail(String msg) {
        toast(msg);
    }

    @Override
    public void loginMsg(int msg) {
        toast(msg);
    }

    @Override
    public void getFamilyListDataSuccess(ArrayList<MeasureFamilyUserInfo> measureUserInfos) {
        this.measureFamilyUserInfos = measureUserInfos;
        DBManager.getInstance(this).deleteAllMeasureFamilyUserInfoBean();
        for (int i = 0;i < measureFamilyUserInfos.size();i++){
            DBManager.getInstance(this).insertMeasureMember(measureFamilyUserInfos.get(i));
        }
    }

    @Override
    public void getFamilyListDataFail(String msg) {
        toast(msg);
    }
}
