package com.datalife.datalife_company.bean;

/**
 * 用户绑定机器的信息
 * Created by LG on 2019/7/16.
 */
public class MachineBindBean<T> {
    private String MachineBindId;
    private String MachineId;
    private String MachineName;
    private String user_id;
    private String user_name;
    private String MachineSn;
    private String MachineStatus;
    private String CreateDate;
    private T MachineMemberBind;

    public String getMachineBindId() {
        return MachineBindId;
    }

    public void setMachineBindId(String machineBindId) {
        MachineBindId = machineBindId;
    }

    public String getMachineId() {
        return MachineId;
    }

    public void setMachineId(String machineId) {
        MachineId = machineId;
    }

    public String getMachineName() {
        return MachineName;
    }

    public void setMachineName(String machineName) {
        MachineName = machineName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getMachineSn() {
        return MachineSn;
    }

    public void setMachineSn(String machineSn) {
        MachineSn = machineSn;
    }

    public String getMachineStatus() {
        return MachineStatus;
    }

    public void setMachineStatus(String machineStatus) {
        MachineStatus = machineStatus;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public T getMachineMemberBind() {
        return MachineMemberBind;
    }

    public void setMachineMemberBind(T machineMemberBind) {
        MachineMemberBind = machineMemberBind;
    }
}
