package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.LastFatMeasureDataBean;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.mvp.IView;

/**
 * Created by LG on 2019/7/24.
 */

public interface FatContract extends IView{
    //获取最后一次检测结果
    public void onLastSuccess(LastFatMeasureDataBean<MeasureRecordBean> measureRecordBean);
    public void onLastFail(String msg);
}
