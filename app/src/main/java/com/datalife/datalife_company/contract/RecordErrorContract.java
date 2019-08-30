package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.MeasureErrorListBean;
import com.datalife.datalife_company.bean.TypeBean;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/8/28.
 */
public interface RecordErrorContract extends IView {


    public void getDataSuccess(ArrayList<TypeBean> typeBeans);
    public void getDataFail(String msg);

    public void getDataListSuccess(ArrayList<MeasureErrorListBean> measureErrorListBeans);
    public void getDataListFail(String msg);

}
