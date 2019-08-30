package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.MeContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/16.
 */

public class MePresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private MeContract meContract;

    @Override
    public void onCreate(Context context,IView iView) {
        this.mContext = context;
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
        meContract = (MeContract) iView;
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

    public void unBindUserWx(String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "userbase");
        params.put("fun", "UnBindUserWx");
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.UnBindUserWx(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LoginBean,Object>() {
                    @Override
                    public void onResponse(LoginBean newsInfos, String status,Object pageBean) {
                        meContract.getUnbindWxSuccess(newsInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        //  redEnvelopeView.failRedEnvelope(status);
                        meContract.getUnbindWxFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<LoginBean, Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }

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
                        meContract.uploadImageSuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        meContract.uploadImageFail(msg);
                    }
                }));
    }
}
