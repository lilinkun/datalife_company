package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.DownloadBean;
import com.datalife.datalife_company.mvp.IView;

/**
 * Created by LG on 2019/7/17.
 */

public interface SettingContract extends IView {
    //退出登录
    void loginoutSuccess();
    void loginoutFail(String failMsg);
    //更新版本
    void updateSuccess(DownloadBean downloadBean);
    void updateFail(String failMsg);
    //绑定微信
    void bindSuccess();
    void bindFail(String failMsg);
}
