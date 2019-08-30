package com.datalife.datalife_company.presenter;

import android.content.Context;
import android.content.Intent;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ProvinceBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.AddFamilyUserContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/17.
 */

public class AddUserPresener extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private ResultBean mResultBean;
    private AddFamilyUserContract addFamilyView;


    public AddUserPresener(){

    }

    @Override
    public void onCreate(Context mContext,IView iView) {
        this.mContext = mContext;
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
        addFamilyView = (AddFamilyUserContract) iView;
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


    public void addFamilyUser(String Member_Name ,String Member_Portrait,String Member_Stature,String Member_Weight,String Member_DateOfBirth,String Member_Sex,String Member_Status,String Member_IsDefault,String SessionId,String Member_Phone,String Member_Province,String Member_City,String Member_District){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "BigDataMember");
        params.put("fun", "New_BigDataMemberCreate");
        params.put("Member_Name", Member_Name);
        params.put("Member_Portrait", Member_Portrait);
        params.put("Member_Stature", Member_Stature);
        params.put("Member_Weight",Member_Weight);
        params.put("Member_DateOfBirth",Member_DateOfBirth);
        params.put("Member_Status",Member_Status);
        params.put("Member_Sex",Member_Sex);
        params.put("Member_IsDefault",Member_IsDefault);
        params.put("Member_Phone",Member_Phone);
        params.put("Member_Province",Member_Province);
        params.put("Member_City",Member_City);
        params.put("Member_District",Member_District);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.addFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        addFamilyView.onsuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addFamilyView.onfail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }

                })
        );
    }

    public void updateFamilyUser(String Member_Id,String Member_Name ,String Member_Portrait,String Member_Stature,String Member_Weight,String Member_DateOfBirth,String Member_Sex,String Member_Status,String Member_IsDefault,String SessionId,String Member_Phone,String Member_Province,String Member_City,String Member_District){

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "BigDataMember");
        params.put("fun", "New_BigDataMemberUpdate");
        params.put("Member_Id",Member_Id);
        params.put("Member_Name", Member_Name);
        params.put("Member_Portrait", Member_Portrait);
        params.put("Member_Stature", Member_Stature);
        params.put("Member_Weight",Member_Weight);
        params.put("Member_DateOfBirth",Member_DateOfBirth);
        params.put("Member_Status",Member_Status);
        params.put("Member_Sex",Member_Sex);
        params.put("Member_IsDefault",Member_IsDefault);
        params.put("Member_Phone",Member_Phone);
        params.put("Member_Province",Member_Province);
        params.put("Member_City",Member_City);
        params.put("Member_District",Member_District);
        params.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.updateFamilyUser(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status,Object page) {
                        addFamilyView.onsuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addFamilyView.onfail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }

                })
        );
    }

    public void getFamilyDataList(String sessionId,String PageIndex,String PageCount){

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
                    public void onResponse(ArrayList<FamilyUserInfo> familyUserInfos, String status,PageBean page) {
                        addFamilyView.onBackFamilyListDataSuccess(familyUserInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addFamilyView.onBackFamilyListDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<FamilyUserInfo>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }

    public void getBindMemberList(String PageIndex,String PageCount,String MachineBindId,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("cls","MachineMemberBind");
        mHashMap.put("fun","MachineMemberBindList");
        mHashMap.put("PageIndex",PageIndex);
        mHashMap.put("PageCount",PageCount);
        mHashMap.put("MachineBindId",MachineBindId);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.getMachineMemberInfo(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<List<MachineBindMemberBean>, PageBean>() {
                    @Override
                    public void onResponse(List<MachineBindMemberBean> machineBindMemberBeans, String status,PageBean page) {
                        addFamilyView.onSuccess(machineBindMemberBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        addFamilyView.onFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<List<MachineBindMemberBean>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
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
                        addFamilyView.getDataSuccess(provinceBeans,localType);
                    }

                    @Override
                    public void onErr(String msg, String status) {

                    }
                }));
    }

}

