package com.datalife.datalife_company.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.ProvinceBean;
import com.datalife.datalife_company.contract.AddressPickerContract;
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

public class AddressPickerPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private AddressPickerContract addressPickerContract;


    public AddressPickerPresenter() {
    }

    @Override
    public void onCreate(Context context,IView iView) {
        this.mContext = context;
        manager = new DataManager(context);
        mCompositeSubscription = new CompositeSubscription();
        addressPickerContract = (AddressPickerContract) iView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        mCompositeSubscription.unsubscribe();
    }

    @Override
    public void pause() {

    }

    public void getLocalData(String parentId,final int localType){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls","Dict_Region");
        params.put("fun","DictRegionList");
        params.put("parentId",parentId);
        mCompositeSubscription.add(manager.getLocalData(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<ProvinceBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<ProvinceBean> provinceBeans, String status,Object page) {
                        addressPickerContract.getDataSuccess(provinceBeans,localType);
                    }

                    @Override
                    public void onErr(String msg, String status) {

                    }
                }));
    }
}
