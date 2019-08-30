package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/16.
 */

public interface HealthMonitorContract extends IView {
    //获取用户绑定所有的机器
    public void onSuccess(ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>> resultNews);
    public void onfail(String str);

}
