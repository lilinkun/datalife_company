package com.datalife.datalife_company.presenter;

import android.content.Context;
import android.util.Log;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.BlueToothDevContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 搜索蓝牙页面
 * Created by LG on 2019/7/18.
 */
public class BlueToothDevPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BlueToothDevContract blueToothDevContract;

    @Override
    public void onCreate(Context context, IView view) {
        mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        manager = new DataManager(context);
        blueToothDevContract = (BlueToothDevContract) view;
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
     * 获取绑定的设备
     * @param PageIndex
     * @param PageCount
     * @param SessionId
     */
    public void getDevMachineInfo(String PageIndex,String PageCount,String SessionId){

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
                    public void onResponse(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans, String status,PageBean pageBean) {
                        blueToothDevContract.getMachineSuccess(machineBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        blueToothDevContract.getMachineFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean> arrayListPageBeanResultFamily) {
                        super.onNext(arrayListPageBeanResultFamily);
                    }
                }));
    }


    /**
     * 绑定设备接口
     * @param MachineName
     * @param MachineSn
     * @param SessionId
     */
    public void bindToothMachine(String MachineName,String MachineSn,String SessionId){

        Log.v("HealthMonitorPresenter:" , "MachineName:" + MachineName + ",MachineSn:" + MachineSn + ",SessionId:"+SessionId);
        HashMap<String , String> hashMap = new HashMap<>();
        hashMap.put("cls","MachineBind");
        hashMap.put("fun","MachineBindCreate");
        hashMap.put("MachineName",MachineName);
        hashMap.put("MachineSn",MachineSn);
        hashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.bindToothMachine(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status,Object pageBean) {
                        blueToothDevContract.bindSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        blueToothDevContract.bindFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean o) {
                        super.onNext(o);
                    }

                })
        );

    }

}
