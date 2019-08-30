package com.datalife.datalife_company.interf;

import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.dao.MachineBean;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/19.
 */

public interface OnDataListener {

    public void onBack();

    public void onPage(int page);

//    public void onMember(ArrayList<MachineBindMemberBean> machineBindMemberBean, String machinebindid, String machineid);

    public void onMachine(MachineBean machineBean);

    public void onChageMember();

    public void onBattery(int batteryValue);
}
