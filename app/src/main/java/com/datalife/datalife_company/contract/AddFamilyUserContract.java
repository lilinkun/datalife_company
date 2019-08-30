package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.ProvinceBean;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2019/7/17.
 */

public interface AddFamilyUserContract extends IView {
    //添加成员是否成功
    public void onsuccess();
    public void onfail(String failMsg);
    //获取个人数据是否成功
    public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> familyUserInfos);
    public void onBackFamilyListDataFail(String str);
    //获取绑定机器是否成功
    public void onSuccess(List<MachineBindMemberBean> machineBindMemberBeans);
    public void onFail(String failMsg);
    //获取省市区数据
    public void getDataSuccess(ArrayList<ProvinceBean> provinceBeans, int id);
    public void getDataFail(String msg);

}
