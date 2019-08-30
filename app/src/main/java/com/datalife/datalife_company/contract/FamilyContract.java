package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.mvp.IView;

import java.util.ArrayList;

/**
 * Created by LG on 2019/7/16.
 */

public interface FamilyContract extends IView {
    public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> listResultBean);
    public void onBackFamilyListDataFail(String str);

    public void onBindDataSuccess(String str);
    public void onBindDataFail(String str);
}
