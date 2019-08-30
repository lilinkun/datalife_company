package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/18.
 */

public interface BlueToothDevContract extends IView {
    //绑定是否成功
    public void bindSuccess();
    public void bindFail(String msg);
    //获取用户下的机器是否成功
    public void getMachineSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> machineBeans);
    public void getMachineFail(String msg);
}
