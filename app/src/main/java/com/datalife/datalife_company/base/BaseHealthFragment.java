package com.datalife.datalife_company.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import com.datalife.datalife_company.activity.HealthMonitorActivity;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.interf.OnDataListener;
import com.datalife.datalife_company.service.HcService;
import com.datalife.datalife_company.util.UToast;

/**
 * Created by LG on 2019/7/19.
 */

public abstract class BaseHealthFragment extends BaseFragment {
    protected HealthMonitorActivity mActivity;
    protected OnDataListener onDataListener;
    protected HcService mHcService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mActivity = (HealthHomeActivity) getActivity();
        mActivity = (HealthMonitorActivity) getActivity();
        if (ProApplication.isUseCustomBleDevService){
            mHcService = mActivity.mHcService;
        }
        onDataListener = (OnDataListener) mActivity;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.scaledDensity = displayMetrics.density;
    }


    protected void toast(@NonNull String text) {
        UToast.show(getActivity(), text);
    }

    protected void toast(@NonNull int intres) {
        UToast.show(getActivity(), getString(intres));
    }

    public void uploadData() {

    }
}