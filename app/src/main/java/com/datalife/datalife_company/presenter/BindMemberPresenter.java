package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.bean.ResultFamily;
import com.datalife.datalife_company.contract.BindMemberContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.HashMap;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/18.
 */

public class BindMemberPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private BindMemberContract bindMemberView;
    private ResultFamily<List<MachineBindMemberBean>,PageBean> arrayListResultBean;
    private ResultBean resultBean;

    @Override
    public void onCreate(Context context, IView view) {
        this.mContext = context;
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
        bindMemberView = (BindMemberContract) view;
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
     * 获取用户成员列表
     * @param PageIndex
     * @param PageCount
     * @param MachineBindId
     * @param SessionId
     */
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
                    public void onResponse(List<MachineBindMemberBean> machineBindMemberBeans, String status,PageBean pageBean) {
                        bindMemberView.getBindMemberSuccess(machineBindMemberBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        bindMemberView.getBindMemberFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<List<MachineBindMemberBean>,PageBean> resultBean) {
                        super.onNext(resultBean);
                    }
                }));
    }

    /**
     * 绑定成员
     * @param MachineBindId
     * @param Machine_Id
     * @param Member_Id
     * @param SessionId
     */
    public void putBindMember(String MachineBindId,String Machine_Id,String Member_Id,String SessionId){
        HashMap<String,String> mHashMap = new HashMap<>();
        mHashMap.put("cls","MachineMemberBind");
        mHashMap.put("fun","MachineMemberBindCreate");
        mHashMap.put("MachineBindId",MachineBindId);
        mHashMap.put("Machine_Id",Machine_Id);
        mHashMap.put("Member_Id",Member_Id);
        mHashMap.put("SessionId",SessionId);
        mCompositeSubscription.add(manager.putMachineMember(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {
                    @Override
                    public void onResponse(Object o, String status,Object pageBean) {
                        bindMemberView.putBindSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        bindMemberView.putBindFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBeans) {
                        super.onNext(resultBeans);
                    }
                }));
    }
}
