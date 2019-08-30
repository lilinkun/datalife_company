package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.mvp.IView;

import java.util.List;

/**
 * Created by LG on 2019/7/18.
 */

public interface BindMemberContract extends IView{
    //获取绑定成员列表
    public void getBindMemberSuccess(List<MachineBindMemberBean> machineBindMemberBeans);
    public void getBindMemberFail(String msg);
    //是否绑定成功
    public void putBindSuccess();
    public void putBindFail(String msg);
}
