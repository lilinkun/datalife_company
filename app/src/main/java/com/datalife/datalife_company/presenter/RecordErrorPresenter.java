package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.MeasureErrorListBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.bean.TypeBean;
import com.datalife.datalife_company.contract.RecordErrorContract;
import com.datalife.datalife_company.http.callback.HttpResultCallBack;
import com.datalife.datalife_company.manager.DataManager;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;
import java.util.HashMap;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by LG on 2019/8/28.
 */
public class RecordErrorPresenter extends BasePresenter {
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private RecordErrorContract recordErrorContract;

    @Override
    public void onCreate(Context context, IView view) {
        this.mContext = context;
        manager = new DataManager(mContext);
        mCompositeSubscription = new CompositeSubscription();
        recordErrorContract = (RecordErrorContract) view;
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

    public void getMeasureInfo(String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectClass");
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getMeasureInfo(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<TypeBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<TypeBean> newsInfos, String status, Object pageBean) {
                        recordErrorContract.getDataSuccess(newsInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        //  redEnvelopeView.failRedEnvelope(status);
                        recordErrorContract.getDataFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<TypeBean>,Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }

    /**
     *
     * @param Project_Id
     * @param PageCount
     * @param PageIndex
     * @param Search_UserName
     * @param Search_Phone
     * @param ProjectCheck_Status 正常是0异常是1
     * @param SessionId
     */
    public void getMeasureList(String Project_Id,String PageCount,String PageIndex,String Search_UserName,String Search_Phone,String ProjectCheck_Status,String SessionId){
        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectChecks");
        params.put("PageIndex",PageIndex);
        params.put("PageCount",PageCount);
        if (Project_Id!=null && !Project_Id.equals("0")) {
            params.put("Project_Id", Project_Id);
        }
        if (Search_UserName != null) {
            params.put("Search_UserName", Search_UserName);
        }
        if (Search_Phone != null) {
            params.put("Search_Phone", Search_Phone);
        }
        if (ProjectCheck_Status != null && !ProjectCheck_Status.equals("2")) {
            params.put("ProjectCheck_Status", ProjectCheck_Status);
        }
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.getMeasureList(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack<ArrayList<MeasureErrorListBean>,Object>() {
                    @Override
                    public void onResponse(ArrayList<MeasureErrorListBean> newsInfos, String status, Object pageBean) {
                        recordErrorContract.getDataListSuccess(newsInfos);
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        //  redEnvelopeView.failRedEnvelope(status);
                        recordErrorContract.getDataListFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean<ArrayList<MeasureErrorListBean>,Object> resultNews) {
                        super.onNext(resultNews);
                    }
                })
        );
    }
}
