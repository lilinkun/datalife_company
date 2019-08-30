package com.datalife.datalife_company.http.callback;

import android.util.Log;

import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.http.factory.ResultException;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.google.gson.Gson;

import rx.Subscriber;

public abstract class HttpResultCallBack<M,T> extends Subscriber<ResultBean<M,T>> {

    /**
     * 请求返回
     */
    public abstract void onResponse(M m, String status,T page);
    public abstract void onErr(String msg, String status);

    /**
     * 请求完成
     */
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if(e != null){
            if(e instanceof ResultException){
                ResultException err = (ResultException) e;
                onErr(err.getErrMsg(), err.getErrCode());
            }else{
                onErr("异常，请检查", IDatalifeConstant.RESULT_FAIL);
                Log.d("HttpResultCallBack","解析失败==：" + e.getMessage());
            }
        }
        onCompleted();
    }

    /**
     * Http请求失败
     */
    private void onHttpFail(String msg, String status){
        onErr(msg, status);
    }

    @Override
    public void onNext(ResultBean<M,T> result) {
        String jsonResponse = new Gson().toJson(result);
        Log.d("HttpResultCallBack", "返回ok==：" + jsonResponse);
        if (result.getStatus().equals(IDatalifeConstant.RESULT_SUCCESS)) {
            onResponse(result.getData(), IDatalifeConstant.RESULT_SUCCESS,result.getPage());
        } else {
            onHttpFail(result.getDesc(), "" + result.getCode());
        }
    }
}
