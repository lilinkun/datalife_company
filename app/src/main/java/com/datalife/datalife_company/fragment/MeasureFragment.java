package com.datalife.datalife_company.fragment;

import android.view.View;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseHealthFragment;
import com.linktop.MonitorDataTransmissionManager;

/**
 * Created by LG on 2019/7/19.
 */
public abstract class MeasureFragment extends BaseHealthFragment {

    public MeasureFragment() {
    }

    public abstract boolean startMeasure();

    public abstract void stopMeasure();

    protected abstract void getchange(String str);

    /**
     * 点击开始测量
     * @param v
     */
    public void clickMeasure(View v) {
        if (ProApplication.isUseCustomBleDevService) {
            if (!mHcService.isConnected) {
                toast(R.string.health_uncontact);
                return;
            }
            //判断设备是否在充电，充电时不可测量
            if (mHcService.getBleDevManager().getBatteryTask().isCharging()) {
                toast(R.string.health_charging);
                return;
            }
            if (mHcService.getBleDevManager().isMeasuring()) {
                stopMeasure();
                //设置ViewPager可滑动
                mActivity.viewPager.setCanScroll(true);
                getchange(getString(R.string.start));
            } else {
                if (startMeasure()) {
                /*
                 * 请注意了：为了代码逻辑不会混乱，每一单项在测量过程中请确保用户无法通过任何途径
                 * (当然，如果用户强制关闭页面就不管了)切换至其他测量单项的界面，直到本项一次测量结束。
                 */
                    //设置ViewPager不可滑动
                    mActivity.viewPager.setCanScroll(false);
                    getchange(getString(R.string.stopmeasure));
                }
            }
        }
    }

    public abstract void clickUploadData(View v);

}

