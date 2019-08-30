package com.datalife.datalife_company.base;

import android.content.Context;
import android.graphics.Typeface;

import com.datalife.datalife_company.activity.RecordActivity;
import com.datalife.datalife_company.interf.OnMeasureRecordLisener;

/**
 * Created by LG on 2019/7/24.
 */

public abstract class BaseRecordFragment extends BaseFragment{


    protected RecordActivity mActivity;
    protected Typeface mTfRegular;
    protected Typeface mTfLight;

    //    protected RecordPresenter recordPresenter = new RecordPresenter(getActivity());
    protected OnMeasureRecordLisener onBtRecordLisener = null;

    @Override
    public void onAttach(Context context) {
        mActivity = (RecordActivity) getActivity();
        onBtRecordLisener = (OnMeasureRecordLisener)getActivity();
        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
