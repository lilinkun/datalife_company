package com.datalife.datalife_company.bean;

import com.datalife.datalife_company.dao.FamilyUserInfo;

/**
 * 成员列表对象
 * Created by LG on 2019/7/18.
 */
public class MemberListBean {
    private FamilyUserInfo familyUserInfo;
    private boolean isSelector ;

    public FamilyUserInfo getFamilyUserInfo() {
        return familyUserInfo;
    }

    public void setFamilyUserInfo(FamilyUserInfo familyUserInfo) {
        this.familyUserInfo = familyUserInfo;
    }

    public boolean isSelector() {
        return isSelector;
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }

    @Override
    public String toString() {
        return "MemberListBean{" +
                "familyUserInfo=" + familyUserInfo +
                ", isSelector=" + isSelector +
                '}';
    }
}