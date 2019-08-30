package com.datalife.datalife_company.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.contract.RegisterContract;
import com.datalife.datalife_company.http.RetrofitHelper;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;
import com.datalife.datalife_company.util.FileImageUpload;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/16.
 */

public class RegisterPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RegisterContract registerContract;

    @Override
    public void onCreate(Context context,IView iView) {
        manager = new DataManager(context);
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        registerContract = (RegisterContract) iView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void pause() {

    }

    public void register(String username , String Store_Name, String Store_Address, String Responsibility_Name, String Phone, String PassWord,String Certificates, String SessionId){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Store");
        params.put("fun","StoreCreate");
        params.put("Store_Name", Store_Name);
        params.put("Store_Address", Store_Address);
        params.put("Responsibility_Name", Responsibility_Name);
        params.put("Phone", Phone);
        params.put("PassWord", PassWord);
        params.put("User_Name",username);
        params.put("Certificates", Certificates);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.registerCompany(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean o, String status, Object pageBean) {
                        registerContract.registerCompanySuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        registerContract.registerCompanyFail(msg);
                    }
                }));
    }

    public void registerCompany(String Store_Name, String Store_Address, String Responsibility_Name, String Phone, String PassWord, ArrayList<String> Certificates, final ProgressDialog progressDialog, final Handler handler){

        if(Store_Name == null || Store_Name.isEmpty()){
            registerContract.showPromptMessage(R.string.register_company_name_isnull);
            return;
        }

        if(Store_Address == null || Store_Address.isEmpty()){
            registerContract.showPromptMessage(R.string.register_company_address_isnull);
            return;
        }

        if(Responsibility_Name == null || Responsibility_Name.isEmpty()){
            registerContract.showPromptMessage(R.string.register_name_isnull);
            return;
        }

        //!PhoneFormatCheckUtils.isChinaPhoneLegal(Phone)
        if(Phone == null || Phone.isEmpty() || Phone.length() < 7 ){
            registerContract.showPromptMessage(R.string.prompt_phone_number_invalid);
            return;
        }

        if(Certificates == null || Certificates.size() == 0){
            registerContract.showPromptMessage(R.string.register_cert_isnull);
            return;
        }

        if(PassWord == null || PassWord.isEmpty()){
            registerContract.showPromptMessage(R.string.prompt_login_passwrod_not_empty);
            return;
        }

        if(PassWord.toString().trim().length() > 20 || PassWord.toString().trim().length() < 6){
            registerContract.showPromptMessage(R.string.prompt_passwrod_not_allow);
            return;
        }

        progressDialog.show(mContext,"请稍等...","注册中...",true);

        for (final String pic : Certificates){
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            File file = new File(pic);
                            String str = FileImageUpload.uploadFile(file, RetrofitHelper.CertificatesImageUrl);
                            if (!str.equals("0")){
                                Bundle bundle = new Bundle();
                                bundle.putString("pics",str);
                                Message message = new Message();
                                message.setData(bundle);
                                message.what = 2332;
                                handler.sendMessage(message);
                            }else {
                                if (progressDialog!= null && progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                                registerContract.showPromptMessage(R.string.upload_fail);
                            }

                        }
                    }
            ).start();
        }

    }

    //上传图片
    public void uploadImage(String HeadPic,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","userbase");
        params.put("fun","UpdateUserHeadPic");
        params.put("HeadPic", HeadPic);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getUserHeadInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<String,Object>() {
                    @Override
                    public void onResponse(String o, String status,Object pageBean) {
                        registerContract.uploadImageSuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        registerContract.uploadImageFail(msg);
                    }
                }));
    }
}
