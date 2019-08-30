package com.datalife.datalife_company.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.DownloadBean;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.contract.MainContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.fragment.HealthHomeFragment;
import com.datalife.datalife_company.fragment.HomeFragment;
import com.datalife.datalife_company.fragment.MallFragment;
import com.datalife.datalife_company.fragment.MeFragment;
import com.datalife.datalife_company.interf.OnBackListener;
import com.datalife.datalife_company.presenter.MainPresenter;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainContract,OnBackListener, MeFragment.FragmentInteraction{

    @BindView(R.id.ll_homepage)
    LinearLayout mHomePageLayout;
    @BindView(R.id.tv_me)
    TextView mMeText;
    @BindView(R.id.tv_homepage) TextView mHomePageText;
    @BindView(R.id.tv_health_home) TextView mHealthHomeText;
    @BindView(R.id.tv_mall) TextView mMallText;
    @BindView(R.id.ic_homepage)
    ImageView mHomePageImage;
    @BindView(R.id.ic_health_home) ImageView mHealthImage;
    @BindView(R.id.ic_mall) ImageView mMallImage;
    @BindView(R.id.ic_me) ImageView mMeImage;
    @BindView(R.id.drawer_layout)
    LinearLayout drawerLayout;
    @BindView(R.id.iv_red_circle) ImageView mRedCircle;

    private AlertDialog alertDialog = null;
    private HealthHomeFragment mHealthHomeFragment;
    private MallFragment mMallFragment;
    private HomeFragment mHomeFragment;
    private MeFragment mMeFragment;
    // 当前正在显示的Fragment
    private Fragment mCurrentFragment;
    public static String STOREID = "";
    MainPresenter mainPresenter = new MainPresenter();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {

        mainPresenter.onCreate(this,this);

        SharedPreferences sharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, Context.MODE_PRIVATE);
        String str = sharedPreferences.getString("logininfo","");
        try {
            LoginBean loginBean = (LoginBean) IDatalifeConstant.deSerialization(str);
            STOREID = loginBean.getStore_Id();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 注意此方法只有在父类的onCreate(Bundle)调用之后才有效.
        retrieveFragments();

        if (mMeFragment != null) {
            mMeFragment.setFragmentInteraction(this);
        }
        changeTab(0);

        String sessionid = IDatalifeConstant.getUniqueId(this);
        ProApplication.SESSIONID = sessionid;

        mainPresenter.getMachineInfo("1","20",sessionid);

        onClick(mHomePageLayout);
    }

    @OnClick({R.id.ll_health_home,R.id.ll_homepage,R.id.ll_mall,R.id.ll_me})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_health_home:
                changeTab(IDatalifeConstant.PAGE_HEALTHHOME);
                if (mHealthHomeFragment == null) mHealthHomeFragment = new HealthHomeFragment();
                switchFragment(mHealthHomeFragment);
                break;
            case R.id.ll_homepage:
                changeTab(IDatalifeConstant.PAGE_HOMEPAGE);
                if (mHomeFragment == null) mHomeFragment = new HomeFragment();
                switchFragment(mHomeFragment);
                break;
            case R.id.ll_mall:
                changeTab(IDatalifeConstant.PAGE_MALL);
                if (mMallFragment == null) mMallFragment = new MallFragment();
                switchFragment(mMallFragment);
                break;
            case R.id.ll_me:
                changeTab(IDatalifeConstant.PAGE_ME);
                if (mMeFragment == null) mMeFragment = new MeFragment();
                switchFragment(mMeFragment);
                break;

        }
    }

    //改变tab
    private void changeTab(int position){
        switch (position){
            case IDatalifeConstant.PAGE_HOMEPAGE:
                mHomePageText.setTextColor(getResources().getColor(R.color.blue));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_index_icon));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));

                break;

            case IDatalifeConstant.PAGE_MALL:
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_index_icon_in));
                mMallText.setTextColor(getResources().getColor(R.color.blue));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_actived));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;

            case IDatalifeConstant.PAGE_HEALTHHOME:
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_index_icon_in));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.blue));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_actived));
                mMeText.setTextColor(getResources().getColor(R.color.gray));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_normal));
                break;


            case IDatalifeConstant.PAGE_ME:
                mHomePageText.setTextColor(getResources().getColor(R.color.gray));
                mHomePageImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_index_icon_in));
                mMallText.setTextColor(getResources().getColor(R.color.gray));
                mMallImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_mall_normal));
                mHealthHomeText.setTextColor(getResources().getColor(R.color.gray));
                mHealthImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_health_normal));
                mMeText.setTextColor(getResources().getColor(R.color.blue));
                mMeImage.setImageDrawable(getResources().getDrawable(R.mipmap.ic_me_actived));

                SharedPreferences sharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean(IDatalifeConstant.LOGIN,false) == false){

                    if(alertDialog == null) {
                        alertDialog = new AlertDialog.Builder(this).setMessage("您还没有登录，请先登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, LoginActivity.class);
                                startActivityForResult(intent, IDatalifeConstant.INTENT_LOGIN);
                                finish();
                                alertDialog = null;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                aaa();
                                alertDialog = null;
                            }
                        }).create();

                        alertDialog.setCanceledOnTouchOutside(false);
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }

                    return;
                }

                break;
        }

    }

    /**
     * 首页4个Fragment切换, 使用hide和show, 而不是replace.
     *
     * @param target 要显示的目标Fragment.
     */
    private void switchFragment(Fragment target) {
        if (mCurrentFragment == target) return;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (mCurrentFragment != null) {
            // 隐藏当前正在显示的Fragment
            transaction.hide(mCurrentFragment);
        }

        if (target.isAdded()) {
            // 如果目标Fragment已经添加过, 就显示它
            transaction.show(target);
        } else {
            // 否则直接添加该Fragment
            transaction.add(R.id.layout_container, target, target.getClass().getName());
        }

        transaction.commit();

        mCurrentFragment = target;
    }


    public void aaa(){
        onClick(mHomePageLayout);
    }


    // 找回FragmentManager中存储的Fragment
    private void retrieveFragments() {
        FragmentManager manager = getSupportFragmentManager();
        mHomeFragment = (HomeFragment) manager.findFragmentByTag(HomeFragment.class.getName());
        mHealthHomeFragment = (HealthHomeFragment) manager
                .findFragmentByTag(HealthHomeFragment.class.getName());
        mMallFragment = (MallFragment) manager.findFragmentByTag(MallFragment.class.getName());
        mMeFragment = (MeFragment) manager.findFragmentByTag(MeFragment.class.getName());
    }




    @Override
    public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> listResultBean) {

    }

    @Override
    public void onBackFamilyListDataFail(String str) {

    }

    @Override
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews) {
        ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans = resultNews;
        DBManager.getInstance(this).deleteAllMachineBean();
        DBManager.getInstance(this).deleteAllMachineBindBean();
        ArrayList<MachineBindMemberBean> machineBindMemberBeans = null;
        for (int i = 0;i<machineBeans.size();i++) {
            try{
                MachineBean machineBean = new MachineBean();
                machineBean.setCreateDate(machineBeans.get(i).getCreateDate());
                machineBean.setMachineBindId(machineBeans.get(i).getMachineBindId());
                machineBean.setMachineId(machineBeans.get(i).getMachineId());
                machineBean.setMachineName(machineBeans.get(i).getMachineName());
                machineBean.setMachineSn(machineBeans.get(i).getMachineSn());
                machineBean.setMachineStatus(machineBeans.get(i).getMachineStatus());
                machineBean.setUser_id(machineBeans.get(i).getUser_id());
                machineBean.setUser_name(machineBeans.get(i).getUser_name());
                DBManager.getInstance(this).insertMachine(machineBean);
                machineBindMemberBeans = machineBeans.get(i).getMachineMemberBind();
                for (int j = 0;j<machineBindMemberBeans.size();j++){
                    DBManager.getInstance(this).insertMachineBindMember(machineBindMemberBeans.get(j));
                }
            }catch (SQLiteConstraintException e){
                toast(e.getMessage());
                Log.e("error:" , e.getMessage());
            }
        }
    }

    @Override
    public void onfail(String str) {
        if (str.contains("登录已失效")){
            SharedPreferences mySharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, MODE_PRIVATE);
            mySharedPreferences.edit().putString("account","").putBoolean(IDatalifeConstant.LOGIN,false).commit();
        }else if (str.contains("查无数据")){

        }
    }

    @Override
    public void updateSuccess(DownloadBean downloadBean) {

    }

    @Override
    public void updateFail(String str) {

    }

    @Override
    public void onBack() {

    }

    @Override
    public void process(String str) {
        if (mRedCircle != null && mRedCircle.isShown()) {
            mRedCircle.setVisibility(View.GONE);
        }
    }
}
