package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.mvp.IView;

/**
 * Created by LG on 2019/7/16.
 */

public interface RegisterContract extends IView{
    //注册成功
    void registerCompanySuccess(LoginBean msg);
    void registerCompanyFail(String msg);
    //显示错误信息
    void showPromptMessage(int msg);
    //上传商户注册头像
    void uploadImageSuccess(String uploadImageBean);
    void uploadImageFail(String msg);
}
