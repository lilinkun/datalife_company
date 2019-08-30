package com.datalife.datalife_company.manager;

import android.content.Context;

import com.datalife.datalife_company.bean.DownloadBean;
import com.datalife.datalife_company.bean.LastFatMeasureDataBean;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.MachineBindBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MaxMinValueBean;
import com.datalife.datalife_company.bean.MeasureErrorListBean;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.bean.PageBean;
import com.datalife.datalife_company.bean.ProvinceBean;
import com.datalife.datalife_company.bean.ResultBean;
import com.datalife.datalife_company.bean.TypeBean;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.http.RetrofitHelper;
import com.datalife.datalife_company.http.RetrofitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;

/**
 * Created by LG on 2019/7/16.
 */

public class DataManager {
    private RetrofitService mRetrofitService;
    public DataManager(Context context){
        mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }

    //帐号密码登陆
    public Observable<ResultBean<LoginBean,Object>> login(HashMap<String, String> mHashMap){
        return mRetrofitService.login(mHashMap);
    }

    //获取用户绑定的机器
    public Observable<ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>,PageBean>> getMachineInfo(HashMap<String ,String> mHashMap){
        return mRetrofitService.getMachineInfo(mHashMap);
    }

    //用户绑定微信
    public Observable<ResultBean<LoginBean,Object>> bindUser(HashMap<String, String> mHashMap){
        return mRetrofitService.bindUser(mHashMap);
    }

    //商户注册
    public Observable<ResultBean<LoginBean,Object>> registerCompany(HashMap<String,String> mHashMap){
        return mRetrofitService.registerCompany(mHashMap);
    }

    /**
     * 上传用户头像
     */
    public Observable<ResultBean<String,Object>> getUserHeadInfo(HashMap<String,String> mHashMap){
        return mRetrofitService.getUserHeadInfo(mHashMap);
    }

    /**
     * 服务器获取成员
     */
    public Observable<ResultBean<ArrayList<FamilyUserInfo>,PageBean>> getFamilyList(HashMap<String,String> mHashMap){
        return mRetrofitService.getfamilylist(mHashMap);
    }

    /**
     * 服务器获取成员
     */
    public Observable<ResultBean<ArrayList<MeasureFamilyUserInfo>,PageBean>> getMeasureFamilyList(HashMap<String,String> mHashMap){
        return mRetrofitService.getMeasureFamilyList(mHashMap);
    }

    /**
     * 检查ａｐｐ是否有新版本
     */
    public Observable<ResultBean<DownloadBean,Object>> update(HashMap<String,String> mHashMap){
        return mRetrofitService.update(mHashMap);
    }

    /**
     * 是否用户绑定了微信
     */
    public Observable<ResultBean<LoginBean,Object>> UnBindUserWx(HashMap<String,String> mHashMap){
        return mRetrofitService.UnBindUserWx(mHashMap);
    }

    /**
     * 添加成员
     */
    public Observable<ResultBean> addFamilyUser(HashMap<String, String> mHashMap){
        return mRetrofitService.addFamilyUser(mHashMap);
    }

    /**
     * 更新成员
     */
    public Observable<ResultBean> updateFamilyUser(HashMap<String, String> mHashMap){
        return mRetrofitService.updateFamilyUser(mHashMap);
    }

    /**
     * 获取设备成员信息
     */
    public Observable<ResultBean<List<MachineBindMemberBean>,PageBean>> getMachineMemberInfo(HashMap<String ,String> mHashMap){
        return mRetrofitService.getMachineMemberInfo(mHashMap);
    }

    /**
     * 获取收货区域
     */
    public Observable<ResultBean<ArrayList<ProvinceBean>,Object>> getLocalData(HashMap<String,String> mHashMap){
        return mRetrofitService.getLocalData(mHashMap);
    }

    /**
     * 登出
     */
    public Observable<ResultBean> loginout(HashMap<String, String> mHashMap){
        return mRetrofitService.loginout(mHashMap);
    }

    /**
     *  绑定设备
     */
    public Observable<ResultBean> bindToothMachine(HashMap<String,String> mHashMap){
        return mRetrofitService.bindToothMachine(mHashMap);
    }

    /**
     * 绑定成员
     */
    public Observable<ResultBean> putMachineMember(HashMap<String,String> mHashMap){
        return mRetrofitService.putMachineMember(mHashMap);
    }

    /**
     * 上传测试数据
     */
    public Observable<ResultBean> uploadMeasureData(HashMap<String, String> mHashMap){
        return mRetrofitService.uploadMeasureData(mHashMap);
    }

    /**
     * 获取检测列表值
     */
    public Observable<ResultBean<ArrayList<MeasureRecordBean>,PageBean>> getMeasureListRecord(HashMap<String,String> mHashMap){
        return mRetrofitService.getMeasureListRecord(mHashMap);
    }

    /**
     * 获取体脂称最后一次检测结果
     * @param mHashMap
     * @return
     */
    public Observable<ResultBean<LastFatMeasureDataBean<MeasureRecordBean>,Object>> getFatLastInfo(HashMap<String,String> mHashMap){
        return mRetrofitService.getFatLastInfo(mHashMap);
    }

    /**
     * 获取体脂称测量数值列表
     * @param mHashMap
     * @return
     */
    public Observable<ResultBean<ArrayList<MeasureRecordBean>,PageBean>> getFatListRecord(HashMap<String,String> mHashMap){
        return mRetrofitService.getFatListRecord(mHashMap);
    }

    //获取体脂称测量的平均值
    public Observable<ResultBean<MaxMinValueBean,Object>> getMaxMinValue(HashMap<String,String> mHashMap){
        return mRetrofitService.getMaxMinValue(mHashMap);
    }

    //获取用户检测状态
    public Observable<ResultBean<ArrayList<TypeBean>,Object>> getMeasureInfo(HashMap<String,String> mHashMap){
        return mRetrofitService.getMeasureInfo(mHashMap);
    }

    //获取用户检测记录
    public Observable<ResultBean<ArrayList<MeasureErrorListBean>,Object>> getMeasureList(HashMap<String,String> mHashMap){
        return mRetrofitService.getMeasureList(mHashMap);
    }


}
