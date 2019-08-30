package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/17.
 */

public interface MeasureDevContract extends IView {

    //登陆成功与否
    public void loginSuccess(LoginBean loginBean);
    public void loginFail(String msg);

    public void loginMsg(int msg);

    //返回测试账号的人员
    public void getFamilyListDataSuccess(ArrayList<MeasureFamilyUserInfo> measureFamilyUserInfos);
    public void getFamilyListDataFail(String msg);

}
