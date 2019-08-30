package com.datalife.datalife_company.presenter;

import android.app.ProgressDialog;
import android.content.Context;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.DownloadBean;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.MainContract;
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
public class MainPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription compositeSubscription;
    private Context context;
    private MainContract mainContract;


    @Override
    public void onCreate(Context context,IView iView) {
        manager = new DataManager(context);
        this.context = context;
        compositeSubscription = new CompositeSubscription();
        mainContract = (MainContract)iView;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if (compositeSubscription.hasSubscriptions()){
            compositeSubscription.unsubscribe();
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

        compositeSubscription.add(manager.getFamilyList(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<FamilyUserInfo>, PageBean>() {
                    @Override
                    public void onResponse(ArrayList<FamilyUserInfo> familyUserInfos, String status,PageBean pageBean) {
                        mainContract.onBackFamilyListDataSuccess(familyUserInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mainContract.onBackFamilyListDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<FamilyUserInfo>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }

    public void getMachineInfo(String PageIndex,String PageCount,String SessionId){

        final ProgressDialog progressDialog = ProgressDialog.show(context,context.getResources().getString(R.string.wait),context.getResources().getString(R.string.get_data),true);
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("cls","MachineBind");
        mHashMap.put("fun","MachineBindList");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("SessionId",SessionId);
        compositeSubscription.add(manager.getMachineInfo(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean>() {
                    @Override
                    public void onResponse(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans, String status,PageBean pageBean) {
                        mainContract.onSuccess(machineBeans);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mainContract.onfail(msg);
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>, PageBean> arrayListPageBeanResultFamily) {
                        super.onNext(arrayListPageBeanResultFamily);
                    }
                }));
    }

    public void update(String sessionid){
        HashMap<String, String> params = new HashMap<>();
        params.put("fun", "Update");
        params.put("cls", "Home");
        params.put("SessionId", sessionid);
        compositeSubscription.add(manager.update(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<DownloadBean, Object>() {

                    @Override
                    public void onResponse(DownloadBean downloadBean, String status, Object pageBean) {
                        mainContract.updateSuccess(downloadBean);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        mainContract.updateFail(msg);
                    }


                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }
                })
        );

    }
}
