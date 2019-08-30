package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.DownloadBean;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/16.
 */

public interface MainContract extends IView{
    //用户拥有的成员
    public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> listResultBean);
    public void onBackFamilyListDataFail(String str);

    //服务器返回用户设备
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews);
    public void onfail(String str);

    //检查是否有新版本
    public void updateSuccess(DownloadBean downloadBean);
    public void updateFail(String str);

}
