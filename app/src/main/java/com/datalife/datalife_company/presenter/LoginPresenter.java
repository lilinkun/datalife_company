package com.datalife.datalife_company.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.RegisterActivity;
import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.LoginContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/16.
 */
public class LoginPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private LoginContract loginContract;
    private Context mContext;

    @Override
    public void onCreate(Context context,IView iView) {
        this.mContext = context;
        manager = new DataManager(context);
        mCompositeSubscription = new CompositeSubscription();
        loginContract = (LoginContract)iView;
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
     * 注册
     * @param mContext
     */
    public void registerText(Activity mContext, String sessionid, String openid, String unionid){
        Intent intent = new Intent();
        intent.setClass(mContext, RegisterActivity.class);
        intent.putExtra(IDatalifeConstant.SESSIONID, sessionid);
        intent.putExtra(IDatalifeConstant.OPENID, openid);
        intent.putExtra(IDatalifeConstant.UNIONID, unionid);
        mContext.startActivityForResult(intent, IDatalifeConstant.RESULT_REGISTER);
    }



    /**
     * 登陆
     */
    public void login(String username,String psw,String sessionId){
        if(username == null || username.isEmpty()){
            loginContract.loginMsg(R.string.prompt_login_name_not_empty);
            return;
        }

        if(psw == null || psw.isEmpty()){
            loginContract.loginMsg(R.string.prompt_login_passwrod_not_empty);
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
                        loginContract.loginSuccess(loginBean);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        loginContract.loginFail(msg);
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
                        loginContract.loginSuccess(loginBean);
                    }
                    @Override
                    public void onErr(String msg, String status) {
                        loginContract.loginFail(msg);
                    }
                    @Override
                    public void onNext(ResultBean<LoginBean,Object> ResultBean) {
                        super.onNext(ResultBean);
                    }
                })
        );
    }

}
