package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.MaxMinValueBean;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.FatRecordContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/25.
 */
public class FatRecordPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private FatRecordContract fatRecordContract = null;

    @Override
    public void onCreate(Context context, IView view) {
        this.mContext = context;
        this.mCompositeSubscription = new CompositeSubscription();
        this.manager = new DataManager(context);
        fatRecordContract = (FatRecordContract) view;
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
     * 获取列表值
     * @param PageIndex
     * @param PageCount
     * @param Member_Id
     * @param Machine_Id
     * @param Project_Id
     * @param BeginDate
     * @param EndDate
     * @param SessionId
     */
    public void onGetListValue(String PageIndex,String PageCount,String Member_Id,String Machine_Id,String Project_Id,String BeginDate,String EndDate,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("fun","ProjectCheckList");
        mHashMap.put("cls","ProjectCheck");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("Member_Id",Member_Id);
        mHashMap.put("Machine_Id",Machine_Id);
        mHashMap.put("Project_Id",Project_Id);
        mHashMap.put("BeginDate",BeginDate);
        mHashMap.put("EndDate",EndDate);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getFatListRecord(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MeasureRecordBean>,PageBean>() {
                    @Override
                    public void onResponse(ArrayList<MeasureRecordBean> measureRecordBeans, String status,PageBean pageBean) {
                        fatRecordContract.getDataSuccess(measureRecordBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        fatRecordContract.getDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MeasureRecordBean>,PageBean> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }


    public void onMaxMinValue(String MemberId,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("fun","ProjectCheck_WeightValue");
        mHashMap.put("cls","ProjectCheck");
        mHashMap.put("MemberId",MemberId);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getMaxMinValue(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<MaxMinValueBean, Object>() {
                    @Override
                    public void onResponse(MaxMinValueBean s, String status,Object pageBean) {
                        fatRecordContract.onSuccessMaxMinValue(s);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        fatRecordContract.onFailMaxMinValue(msg);
                    }
                }));
    }
}
