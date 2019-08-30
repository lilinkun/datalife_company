package com.datalife.datalife_company.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.MainContract;
import com.datalife.datalife_company.contract.MeasureDevContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/17.
 */

public class MeasureDevPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private MeasureDevContract measureDevContract;

    @Override
    public void onCreate(Context context, IView view) {
        this.mContext = context;
        manager = new DataManager(context);
        mCompositeSubscription = new CompositeSubscription();
        measureDevContract = (MeasureDevContract) view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void pause() {

    }

    /**
     * 登陆
     */
    public void login(String username,String psw,String sessionId){
        if(username == null || username.isEmpty()){
            measureDevContract.loginMsg(R.string.prompt_login_name_not_empty);
            return;
        }

        if(psw == null || psw.isEmpty()){
            measureDevContract.loginMsg(R.string.prompt_login_passwrod_not_empty);
            return;
        }

        final ProgressDialog progressDialog = ProgressDialog.show(mContext,mContext.getResources().getString(R.string.wait),mContext.getResources().getString(R.string.logon),true);
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "Login");
        params.put("UserName", username);
        params.put("PassWord", psw);
        params.put("SessionId", sessionId);
        params.put("MobileType","android");
        mCompositeSubscription.add(manager.login(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean loginBean, String status,Object pageBean) {
                        measureDevContract.loginSuccess(loginBean);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        measureDevContract.loginFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }


    public void getFamilyDataList(String sessionId,String PageCount,String PageIndex){

        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("SessionId",sessionId);
        hashMap.put("cls","BigDataMember");
        hashMap.put("fun","BigDataMemberList");
        hashMap.put("PageCount",PageCount);
        hashMap.put("PageIndex",PageIndex);

        final ProgressDialog progressDialog = ProgressDialog.show(mContext,mContext.getResources().getString(R.string.wait),mContext.getResources().getString(R.string.logon),true);
        mCompositeSubscription.add(manager.getMeasureFamilyList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MeasureFamilyUserInfo>, PageBean>() {
                    @Override
                    public void onResponse(ArrayList<MeasureFamilyUserInfo> familyUserInfos, String status, PageBean pageBean) {
                        measureDevContract.getFamilyListDataSuccess(familyUserInfos);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        measureDevContract.getFamilyListDataFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MeasureFamilyUserInfo>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }


}
