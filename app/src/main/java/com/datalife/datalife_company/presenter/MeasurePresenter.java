package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BasePresenter;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.contract.MeasureContract;
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
 * 上传测试数据Presenter
 * Created by LG on 2019/7/22.
 */
public class MeasurePresenter extends BasePresenter{
    private DataManager manager;
    private CompositeSubscription mCompositeSubscription;
    private Context mContext;
    private MeasureContract measureContract;

    @Override
    public void onCreate(Context context, IView view) {
        this.mContext = context;
        this.mCompositeSubscription = new CompositeSubscription();
        this.manager = new DataManager(context);
        measureContract = (MeasureContract) view;
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
     * 上传血压
     * @param Member_Id
     * @param Project_Id
     * @param Machine_Sn
     * @param SessionId
     * @param CheckValue1
     * @param CheckValue2
     * @param CheckValue3
     */
    public void putBpValue(String Member_Id ,String MachineBindId, String Project_Id,String Machine_Sn,String SessionId,String CheckValue1,String CheckValue2,String CheckValue3,String CheckValue4,String CheckValue5,String CheckValue6) {

        HashMap<String, String> params = new HashMap<>();
        params.put("cls", "ProjectCheck");
        params.put("fun", "ProjectCheckCreate");
        params.put("Member_Id", Member_Id);
        params.put("MachineBindId", MachineBindId);
        params.put("Machine_Id", IDatalifeConstant.MACHINE_HEALTH);
        params.put("Project_Id", Project_Id);
        params.put("Machine_Sn", Machine_Sn);
        params.put("CheckValue1", CheckValue1);
        params.put("CheckValue2", CheckValue2);
        params.put("CheckValue3", CheckValue3);
        params.put("CheckValue4",CheckValue4);
        params.put("CheckValue5",CheckValue5);
        params.put("CheckValue6",CheckValue6);
        params.put("SessionId", SessionId);
        mCompositeSubscription.add(manager.uploadMeasureData(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new HttpResultCallBack() {

                    @Override
                    public void onResponse(Object o, String status, Object page) {
                        measureContract.onUploadSuccess();
                    }

                    @Override
                    public void onErr(String msg, String status) {
                        measureContract.onUploadFail(msg);
                    }

                    @Override
                    public void onNext(ResultBean resultBean) {
                        super.onNext(resultBean);
                    }

                })
        );
    }
}
