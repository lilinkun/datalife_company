package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.RecordContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/7/24.
 */

public class RecordPresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RecordContract recordContract;

    @Override
    public void onCreate(Context context, IView view) {
        this.mContext = context;
        mCompositeSubscription = new CompositeSubscription();
        this.manager = new DataManager(mContext);
        recordContract = (RecordContract) view;
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
    public void onGetListValue(String PageIndex,String PageCount,String Member_Id,String Machine_Id,final String Project_Id,String BeginDate,String EndDate,String SessionId){
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
        mCompositeSubscription.add(manager.getMeasureListRecord(mHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MeasureRecordBean>,PageBean>() {
                    @Override
                    public void onResponse(ArrayList<MeasureRecordBean> measureRecordBeans, String status,PageBean pageBean) {
                        recordContract.onSuccess(measureRecordBeans);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        /*if (Project_Id.equals(IDatalifeConstant.BTINT + "")) {
                            recordContract.onBtFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.BPINT + "")){
                            recordContract.onBpFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.SPO2HINT + "")){
                            recordContract.onSpo2hFail(msg);
                        }else if (Project_Id.equals(IDatalifeConstant.ECGINT + "")){
                            recordContract.onEcgFail(msg);
                            recordContract.onHrFail(msg);
                        }*/
                        recordContract.onFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MeasureRecordBean>,PageBean> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }
}
