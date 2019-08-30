package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.ProvinceBean;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/17.
 */

public interface AddressPickerContract extends IView{
    public void getDataSuccess(ArrayList<ProvinceBean> provinceBeans, int id);
    public void getDataFail(String msg);
}
