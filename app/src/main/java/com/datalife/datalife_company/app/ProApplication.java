package com.datalife.datalife_company.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.linktop.MonitorDataTransmissionManager;

import lib.linktop.sev.CssServerApi;

/**
 * Created by LG on 2019/7/16.
 */

public class ProApplication extends Application{
    private static Context mContext;
    private static ProApplication instance;

    @Override
    public void onCreate() {
        //是否打开日志，true 打开，false关闭，默认打开
        MonitorDataTransmissionManager.isDebug(true);

        super.onCreate();
        mContext = this;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.scaledDensity = displayMetrics.density;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static ProApplication getInstance() {
        return instance;
    }



    public static synchronized ProApplication context() {
        return (ProApplication) mContext;
    }

    /**
     * @return
     * 全局的上下文
     */
    public static Context getmContext() {
        return mContext;
    }

    public final static boolean isUseCustomBleDevService = true;
    public static String SESSIONID = "";
    public static String MEASURE_SESSIONID = "";
    public static boolean REDCOUNT = false;
}