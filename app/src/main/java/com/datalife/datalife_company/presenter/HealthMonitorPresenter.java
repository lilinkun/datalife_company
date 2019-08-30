package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.HealthMonitorContract;
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

public class HealthMonitorPresenter extends BasePresenter {
    public DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private HealthMonitorContract mHealthMonitorView;

    @Override
    public void onCreate(Context context,IView iView) {
        manager = new DataManager(context);
        mCompositeSubscription = new CompositeSubscription();
        mHealthMonitorView = (HealthMonitorContract) iView;
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

    public void getMachineInfo(String PageIndex,String PageCount,String SessionId){

        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("cls","MachineBind");
        mHashMap.put("fun","MachineBindList");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getMachineInfo(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean>() {
                    @Override
                    public void onResponse(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans, String status, PageBean pageBean) {
                        mHealthMonitorView.onSuccess(machineBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mHealthMonitorView.onfail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean> arrayListPageBeanResultFamily) {
                        super.onNext(arrayListPageBeanResultFamily);
                    }
                }));
    }
}
