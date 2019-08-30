package com.datalife.datalife_company.http;


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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by LG on 2018/1/31.
 */

public interface RetrofitService {

    /**
     * 帐号密码登陆
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> login(@FieldMap Map<String, String> params);

    /**
     * 用户绑定微信
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> bindUser(@FieldMap Map<String, String> params);

    /**
     * 获取用户绑定的机器
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<MachineBindBean<ArrayList<MachineBindMemberBean>>>,PageBean>> getMachineInfo(@FieldMap Map<String, String> params);


    /**
     * 商户注册
     * @return
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> registerCompany(@FieldMap Map<String, String> params);

    /**
     * 上传用户图片
     * @return
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<String,Object>> getUserHeadInfo(@FieldMap Map<String, String> params);

    /**
     * 获取用户所有成员
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<FamilyUserInfo>,PageBean>> getfamilylist(@FieldMap Map<String, String> params);
    /**
     * 获取用户所有成员
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<MeasureFamilyUserInfo>,PageBean>> getMeasureFamilyList(@FieldMap Map<String, String> params);

    /**
     * 检查是否有更新
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<DownloadBean,Object>> update(@FieldMap Map<String, String> params);

    /**
     *  是否用户绑定了微信
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LoginBean,Object>> UnBindUserWx(@FieldMap Map<String, String> params);

    /**
     * 添加成员
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> addFamilyUser(@FieldMap Map<String, String> params);

    /**
     * 更新成员
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> updateFamilyUser(@FieldMap Map<String, String> params);

    /**
     * 获取设备成员信息
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<List<MachineBindMemberBean>,PageBean>> getMachineMemberInfo(@FieldMap Map<String, String> params);

    /**
     * 获取收货区域
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<ProvinceBean>,Object>> getLocalData(@FieldMap Map<String, String> params);

    /**
     * 退出登录
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> loginout(@FieldMap Map<String, String> params);

    /**
     * 绑定设备
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> bindToothMachine(@FieldMap Map<String, String> params);

    /**
     * 绑定成员
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> putMachineMember(@FieldMap Map<String, String> params);

    /**
     * 上传测试数据
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean> uploadMeasureData(@FieldMap Map<String, String> params);

    /**
     * 获取检测列表值
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<MeasureRecordBean>,PageBean>> getMeasureListRecord(@FieldMap Map<String,String> params);

    //获取体脂称最新一次测量数据
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<LastFatMeasureDataBean<MeasureRecordBean>,Object>> getFatLastInfo(@FieldMap Map<String, String> params);

    /**
     * 获取体脂称测量数值列表
     */
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<MeasureRecordBean>,PageBean>> getFatListRecord(@FieldMap Map<String, String> params);

    //获取体脂称测量的平均值
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<MaxMinValueBean,Object>> getMaxMinValue(@FieldMap Map<String, String> params);

    //获取用户检测状态
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<TypeBean>,Object>> getMeasureInfo(@FieldMap Map<String, String> params);

    //获取用户检测记录
    @FormUrlEncoded
    @POST("Api/")
    Observable<ResultBean<ArrayList<MeasureErrorListBean>,Object>> getMeasureList(@FieldMap Map<String, String> params);
}
