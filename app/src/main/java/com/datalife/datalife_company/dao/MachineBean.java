package com.datalife.datalife_company.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by LG on 2019/7/17.
 */
@Entity
public class MachineBean implements Serializable {
    private String MachineBindId;
    private String MachineId;
    private String MachineName;
    private String user_id;
    private String user_name;
    private String MachineSn;
    private String MachineStatus;
    private String CreateDate;

    public static final long serialVersionUID = 12324;

    @Generated(hash = 102326396)
    public MachineBean(String MachineBindId, String MachineId, String MachineName,
            String user_id, String user_name, String MachineSn,
            String MachineStatus, String CreateDate) {
        this.MachineBindId = MachineBindId;
        this.MachineId = MachineId;
        this.MachineName = MachineName;
        this.user_id = user_id;
        this.user_name = user_name;
        this.MachineSn = MachineSn;
        this.MachineStatus = MachineStatus;
        this.CreateDate = CreateDate;
    }

    @Generated(hash = 786892830)
    public MachineBean() {
    }

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

}

