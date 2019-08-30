package com.datalife.datalife_company.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.DownloadBean;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.SettingContract;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/17.
 */

public class SettingPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private SettingContract settingContract;

    public SettingPresenter() {

    }

    @Override
    public void onCreate(Context mContext,IView iView) {
        manager = new DataManager(mContext);
        this.mContext = mContext;
        mCompositeSubscription = new CompositeSubscription();
        settingContract = (SettingContract) iView;
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


    public void loginout(String sessionid){
        final ProgressDialog progressDialog = ProgressDialog.show(mContext,mContext.getResources().getString(R.string.wait),mContext.getResources().getString(R.string.loginout),true);
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "Logout");
        params.put("cls", "userbase");
        params.put("SessionId", sessionid);
        mCompositeSubscription.add(manager.loginout(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status,Object pageBean) {
                        DBManager.getInstance(mContext).deleteWxInfo();
                        DBManager.getInstance(mContext).deleteAllFamilyUserInfoBean();
                        settingContract.loginoutSuccess();
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        settingContract.loginoutFail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }
                })
        );
    }

    public void update(String sessionid){
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "Update");
        params.put("cls", "Home");
        params.put("SessionId", sessionid);
        mCompositeSubscription.add(manager.update(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<DownloadBean, Object>() {

                    @Override
                    public void onResponse(DownloadBean downloadBean, String status,Object pageBean) {
                        settingContract.updateSuccess(downloadBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        settingContract.updateFail(msg);
                    }


                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }
                })
        );

    }


    /**
     * 绑定账号
     */
    public void bindUser(String user_name,String user_password,String openid,String unionid,String sessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "UserBindWx");
        params.put("user_name", user_name);
        params.put("openid", openid);
        params.put("unionid",unionid);
        params.put("user_password", user_password);
        params.put("SessionId", sessionId);
        params.put("MobileType","android");
        mCompositeSubscription.add(manager.bindUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean loginBean, String status,Object pageBean) {
                        settingContract.bindSuccess();
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        settingContract.bindFail(msg);
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }
}
