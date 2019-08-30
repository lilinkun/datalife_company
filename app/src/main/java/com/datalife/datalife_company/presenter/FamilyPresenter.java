package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.FamilyContract;
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
 * Created by LG on 2019/7/16.
 */

public class FamilyPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private FamilyContract familyContract;

    @Override
    public void onCreate(Context context,IView iView) {
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        manager = new DataManager(context);
        familyContract = (FamilyContract) iView;
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

    public void getFamilyDataList(String sessionId,String PageCount,String PageIndex){

        HashMap<String ,String> hashMap = new HashMap<>();
        hashMap.put("SessionId",sessionId);
        hashMap.put("cls","BigDataMember");
        hashMap.put("fun","BigDataMemberList");
        hashMap.put("PageCount",PageCount);
        hashMap.put("PageIndex",PageIndex);

        mCompositeSubscription.add(manager.getFamilyList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<FamilyUserInfo>, PageBean>() {
                    @Override
                    public void onResponse(ArrayList<FamilyUserInfo> familyUserInfos, String status,PageBean pageBean) {
                        familyContract.onBackFamilyListDataSuccess(familyUserInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        familyContract.onBackFamilyListDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<FamilyUserInfo>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }

}
