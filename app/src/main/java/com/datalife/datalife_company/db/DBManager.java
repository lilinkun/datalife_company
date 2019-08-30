package com.datalife.datalife_company.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.datalife.datalife_company.bean.DaoMaster;
import com.datalife.datalife_company.bean.DaoSession;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.MachineBindMemberBeanDao;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfo;
import com.datalife.datalife_company.bean.MeasureFamilyUserInfoDao;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.dao.FamilyUserInfoDao;
import com.datalife.datalife_company.dao.MachineBean;
import com.datalife.datalife_company.dao.MachineBeanDao;
import com.datalife.datalife_company.dao.WxUserInfo;
import com.datalife.datalife_company.dao.WxUserInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by LG on 2018/2/10.
 */

public class DBManager {
    private final static String dbName = "health_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入微信用户
     * @param wxUserInfo
     */
    public void insertWxInfo(WxUserInfo wxUserInfo){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WxUserInfoDao wxUserInfoDao = daoSession.getWxUserInfoDao();
        wxUserInfoDao.insert(wxUserInfo);
    }

    /**
     * 删除记录
     */
    public void deleteWxInfo(){
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WxUserInfoDao wxUserInfoDao = daoSession.getWxUserInfoDao();
        wxUserInfoDao.deleteAll();
    }

    public WxUserInfo queryWxInfo(){
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        WxUserInfoDao wxUserInfoDao = daoSession.getWxUserInfoDao();
        QueryBuilder<WxUserInfo> qb = wxUserInfoDao.queryBuilder();
        WxUserInfo list = qb.unique();
        return list;
    }

    /**
     * 插入一条记录
     *
     * @param familyUserInfo
     */
    public void insertMember(FamilyUserInfo familyUserInfo) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FamilyUserInfoDao familyUserInfoDao = daoSession.getFamilyUserInfoDao();
        familyUserInfoDao.insert(familyUserInfo);
    }

    /**
     * 查询用户列表
     */
    public List<FamilyUserInfo> queryFamilyUserInfoList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FamilyUserInfoDao familyUserInfoDao = daoSession.getFamilyUserInfoDao();
        QueryBuilder<FamilyUserInfo> qb = familyUserInfoDao.queryBuilder();
        List<FamilyUserInfo> list = qb.list();
        return list;
    }


    /**
     * 删除所有记录
     */
    public void deleteAllFamilyUserInfoBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        FamilyUserInfoDao familyUserInfoDao = daoSession.getFamilyUserInfoDao();
        familyUserInfoDao.deleteAll();
    }

    /**
     * 插入一条记录
     *
     * @param familyUserInfo
     */
    public void insertMeasureMember(MeasureFamilyUserInfo familyUserInfo) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MeasureFamilyUserInfoDao familyUserInfoDao = daoSession.getMeasureFamilyUserInfoDao();
        familyUserInfoDao.insert(familyUserInfo);
    }

    /**
     * 查询用户列表
     */
    public List<MeasureFamilyUserInfo> queryMeasureFamilyUserInfoList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MeasureFamilyUserInfoDao familyUserInfoDao = daoSession.getMeasureFamilyUserInfoDao();
        QueryBuilder<MeasureFamilyUserInfo> qb = familyUserInfoDao.queryBuilder();
        List<MeasureFamilyUserInfo> list = qb.list();
        return list;
    }


    /**
     * 删除所有记录
     */
    public void deleteAllMeasureFamilyUserInfoBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MeasureFamilyUserInfoDao familyUserInfoDao = daoSession.getMeasureFamilyUserInfoDao();
        familyUserInfoDao.deleteAll();
    }

    /**
     * 插入一条记录
     *
     * @param machineBean
     */
    public void insertMachineBindMember(MachineBindMemberBean machineBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao userDao = daoSession.getMachineBindMemberBeanDao();
        userDao.insert(machineBean);
    }

    /**
     * 查询用户列表
     */
    public List<MachineBindMemberBean> queryMachineBindMemberBeanList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao machineBindMemberBeanDao = daoSession.getMachineBindMemberBeanDao();
        QueryBuilder<MachineBindMemberBean> qb = machineBindMemberBeanDao.queryBuilder();
        List<MachineBindMemberBean> list = qb.list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<MachineBindMemberBean> queryMachineBindMemberBeanList(String bindId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao machineBindMemberBeanDao = daoSession.getMachineBindMemberBeanDao();
        QueryBuilder<MachineBindMemberBean> qb = machineBindMemberBeanDao.queryBuilder();
        qb.where(MachineBindMemberBeanDao.Properties.MachineBindId.eq(bindId)).orderAsc(MachineBindMemberBeanDao.Properties.MachineBindId);
        List<MachineBindMemberBean> list = qb.list();
        return list;
    }

    /**
     * 删除所有记录
     */
    public void deleteAllMachineBindBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBindMemberBeanDao machineBindMemberBeanDao = daoSession.getMachineBindMemberBeanDao();
        machineBindMemberBeanDao.deleteAll();
    }

    /**
     * 查询用户列表
     */
    public List<MachineBean> queryMachineBeanList(String name) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        QueryBuilder<MachineBean> qb = machineBeanDao.queryBuilder();
        qb.where(MachineBeanDao.Properties.User_name.eq(name)).orderAsc(MachineBeanDao.Properties.User_name);
        List<MachineBean> list = qb.list();
        return list;
    }

    /**
     * 删除所有记录
     */
    public void deleteAllMachineBean() {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        machineBeanDao.deleteAll();
    }

    /**
     * 插入一条记录
     *
     * @param machineBean
     */
    public void insertMachine(MachineBean machineBean) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao userDao = daoSession.getMachineBeanDao();
        userDao.insert(machineBean);
    }

    /**
     * 查询机器列表
     */
    public List<MachineBean> queryMachineList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        QueryBuilder<MachineBean> qb = machineBeanDao.queryBuilder();
        List<MachineBean> list = qb.list();
        return list;
    }

    /**
     * 查询机器
     */
    public MachineBean queryMachineBean(String machineSn) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MachineBeanDao machineBeanDao = daoSession.getMachineBeanDao();
        QueryBuilder<MachineBean> qb = machineBeanDao.queryBuilder();
        qb.where(MachineBeanDao.Properties.MachineSn.eq(machineSn)).orderAsc(MachineBeanDao.Properties.MachineSn);
        MachineBean list = qb.unique();
        return list;
    }


}