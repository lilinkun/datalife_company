package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.mvp.IView;

/**
 * Created by LG on 2019/7/16.
 */

public interface LoginContract extends IView{
    //帐号密码登陆
    void loginSuccess(LoginBean mRegisterBean);
    void loginFail(String failMsg);
    //帐号密码为空信息
    void loginMsg(int msg);
}
