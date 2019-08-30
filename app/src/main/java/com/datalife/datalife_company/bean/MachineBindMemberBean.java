package com.datalife.datalife_company.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * Created by LG on 2019/7/16.
 */
@Entity
public class MachineBindMemberBean implements Serializable {
    private String MM_Id;
    private String MachineBindId;
    private String Machine_Id;
    private String Member_Id;
    private String user_id;
    private String user_name;
    private String bindType;
    private String MM_Status;
    private String Create_Date;

    public static final long serialVersionUID = 1221;

    @Generated(hash = 1415208865)
    public MachineBindMemberBean(String MM_Id, String MachineBindId, String Machine_Id, String Member_Id, String user_id, String user_name, String bindType, String MM_Status, String Create_Date) {
        this.MM_Id = MM_Id;
        this.MachineBindId = MachineBindId;
        this.Machine_Id = Machine_Id;
        this.Member_Id = Member_Id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.bindType = bindType;
        this.MM_Status = MM_Status;
        this.Create_Date = Create_Date;
    }

    @Generated(hash = 1020782047)
    public MachineBindMemberBean() {
    }

    public String getMM_Id() {
        return MM_Id;
    }

    public void setMM_Id(String MM_Id) {
        this.MM_Id = MM_Id;
    }

    public String getMachineBindId() {
        return MachineBindId;
    }

    public void setMachineBindId(String machineBindId) {
        MachineBindId = machineBindId;
    }

    public String getMachine_Id() {
        return Machine_Id;
    }

    public void setMachine_Id(String machine_Id) {
        Machine_Id = machine_Id;
    }

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
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

    public String getBindType() {
        return bindType;
    }

    public void setBindType(String bindType) {
        this.bindType = bindType;
    }

    public String getMM_Status() {
        return MM_Status;
    }

    public void setMM_Status(String MM_Status) {
        this.MM_Status = MM_Status;
    }

    public String getCreate_Date() {
        return Create_Date;
    }

    public void setCreate_Date(String create_Date) {
        Create_Date = create_Date;
    }
}
