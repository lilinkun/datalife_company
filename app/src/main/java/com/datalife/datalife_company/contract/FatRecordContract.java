package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.MaxMinValueBean;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/25.
 */
public interface FatRecordContract extends IView {
    //获取体脂称测量数据
    public void getDataSuccess(ArrayList<MeasureRecordBean> measureRecordBeans);
    public void getDataFail(String msg);

    //获取体脂称测量信息的平均值
    public void onSuccessMaxMinValue(MaxMinValueBean maxMinValueBean);
    public void onFailMaxMinValue(String value);
}
