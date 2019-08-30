package com.datalife.datalife_company.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.app.AppManager;
import com.datalife.datalife_company.util.UToast;
import com.datalife.datalife_company.widget.Eyes;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 所有activity的基类
 * Created by LG on 2019/7/16.
 */

public abstract class BaseActivity  extends RxAppCompatActivity {

    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onBefore();
        setContentView(getLayoutId());
//        StatusBarUtil.setStatusBarColor(this, R.color.bg_toolbar_title);
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
        unbinder = ButterKnife.bind(this);
        myPermission();
        //初始化事件跟获取数据以及一些准备工作, 由于使用了ButterKnife, findViewById和基本的Click事件都不会在这里
        initEventAndData();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.scaledDensity = displayMetrics.density;
//        getSupportActionBar().hide();
        //集中管理Activity
        AppManager.getAppManager().addActivity(this);

    }

    protected void onBefore(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
//        AppManager.getAppManager().finishActivity(this);
    }

    protected abstract int getLayoutId();

    protected  abstract void initEventAndData();

    protected void toast(@StringRes int resId) {
        UToast.show(this, resId);
    }

    protected void toast(@StringRes int resId, int duration) {
        UToast.show(this, resId, duration);
    }

    protected void toast(@NonNull String text) {
        UToast.show(this, text);
    }

    protected void toast(@NonNull String text, int duration) {
        UToast.show(this, text, duration);
    }


    /**
     * 跳转到其他界面
     * @param cls 目标Activity
     */
    protected void startOtherActivity(Class cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

    /**
     * 跳转到其他界面，有返回结果
     * @param cls 目标Activity
     * @param requestCode 请求码
     */
    protected void startOtherActivityForResult(Class cls,int requestCode){
        Intent intent = new Intent(this,cls);
        startActivityForResult(intent,requestCode);
    }


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public void myPermission() {
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
