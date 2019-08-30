package com.datalife.datalife_company.bean;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.fragment.AddUserFragment;
import com.datalife.datalife_company.fragment.BindMemberFragment;
import com.datalife.datalife_company.fragment.FamilyFragment;
import com.datalife.datalife_company.fragment.MeFragment;

/**
 * Created by LG on 2019/7/16.
 */

public enum SimpleBackPage {
    MY_INFORMATION(1, R.string.apptab_me, MeFragment.class),
//    EQUIPMANAGER(2, R.string.equipmanager, EquipmanagerFragment.class),
//    USERINFO(4,R.string.user_info, UserinfoFragment.class),
    FAMILYMANAGER(5,R.string.familymanager, FamilyFragment.class),
    ADDUSER(6,R.string.add_user, AddUserFragment.class),
    BINDMEMBER(7,R.string.bindmember, BindMemberFragment.class);
//    REPORT(8, R.string.ele_report, ElectronicReportFragment.class);

    private int title;
    private Class<?> clz;
    private int value;

    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;

    }
}

