package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/24.
 */
public interface RecordContract extends IView{
    public void onSuccess(ArrayList<MeasureRecordBean> newsInfos);

    public void onFail(String str);
}
