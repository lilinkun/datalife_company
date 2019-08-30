package com.datalife.datalife_company.contract;

import com.datalife.datalife_company.mvp.IView;

/**
 * Created by LG on 2019/7/22.
 */

public interface MeasureContract extends IView{
    //上传数据是否成功
    public void onUploadSuccess();
    public void onUploadFail(String str);
}
