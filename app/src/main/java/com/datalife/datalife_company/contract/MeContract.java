package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.mvp.IView;

/**
 * Created by LG on 2019/7/16.
 */

public interface MeContract extends IView {

    public void getDataSuccess(String msg);
    public void getDataFail(String msg);

    public void getUnbindWxSuccess(LoginBean loginBean);
    public void getUnbindWxFail(String msg);

    public void uploadImageSuccess(String uploadImageBean);
    public void uploadImageFail(String msg);
}
