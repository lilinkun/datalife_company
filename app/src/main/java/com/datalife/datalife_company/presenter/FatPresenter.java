package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.LastFatMeasureDataBean;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.FatContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.util.HashMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/24.
 */

public class FatPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private FatContract fatContract;

    @Override
    public void onCreate(Context context, IView view) {
        this.mContext = context;
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
        fatContract = (FatContract) view;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void pause() {

    }

    /**
     * 上传体脂称的值
     *
     * @param Member_Id
     * @param Machine_Sn
     * @param SessionId
     * @param CheckValue1
     * @param CheckValue2
     * @param CheckValue3
     * @param CheckValue4
     * @param CheckValue5
     * @param CheckValue6
     * @param CheckValue7
     * @param CheckValue8
     * @param CheckValue9
     * @param CheckValue10
     * @param CheckValue11
     * @param CheckValue12
     */
    public void putfattest(String Member_Id,
                           String MachineBindId, String Machine_Sn, String SessionId, String CheckValue1, String CheckValue2, String CheckValue3, String CheckValue4,
                           String CheckValue5, String CheckValue6, String CheckValue7, String CheckValue8, String CheckValue9, String CheckValue10, String CheckValue11, String CheckValue12) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("Member_Id", Member_Id);
        params.put("Machine_Id", IDatalifeConstant.MACHINE_FAT);
        params.put("Project_Id", "6");
        params.put("Machine_Sn", Machine_Sn);
        params.put("MachineBindId", MachineBindId);
        params.put("CheckValue1", CheckValue1);
        params.put("CheckValue2", CheckValue2);
        params.put("CheckValue3", CheckValue3);
        params.put("CheckValue4", CheckValue4);
        params.put("CheckValue5", CheckValue5);
        params.put("CheckValue6", CheckValue6);
        params.put("CheckValue7", CheckValue7);
        params.put("CheckValue8", CheckValue8);
        params.put("CheckValue9", CheckValue9);
        params.put("CheckValue10", CheckValue10);
        params.put("CheckValue11", CheckValue11);
        params.put("CheckValue12", CheckValue12);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {

                    @Override
                    public void onResponse(Object o, String status, Object page) {

                    }

                    @Override
                    public void onErr(String msg, String status) {

                    }

                    @Override
                    public void onNext(ResultBean result) {
                        super.onNext(result);
                    }
                })
        );
    }

    /**
     * 机器项目检测最后一次
     */
    public void getNewMeasureInfo(String Member_Id, String Machine_Id, String SessionId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("cls", "ProjectCheck");
        hashMap.put("fun", "ProjectCheckLast");
        hashMap.put("Member_Id", Member_Id);
        hashMap.put("Machine_Id", Machine_Id);
        hashMap.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getFatLastInfo(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<LastFatMeasureDataBean<MeasureRecordBean>, Object>() {
                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                    @Override
                    public void onResponse(LastFatMeasureDataBean<MeasureRecordBean> o, String status, Object pageBean) {
                        fatContract.onLastSuccess(o);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        fatContract.onLastFail(msg);
                    }
                })
        );
    }
}
