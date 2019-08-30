package com.datalife.datalife_company.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.util.IDatalifeConstant;

import java.util.concurrent.TimeUnit;

import lib.linktop.sev.CssServerApi;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Liguo on 2018/1/12.
 * <p>
 * APP启动页
 * <p>
 * 获取服务器动态IP和端口。
 * <p>
 * 测试服务器不需要调用{@link CssServerApi#checkHostPorts}。
 * <p>
 * 不打算使用Linktop服務器作為數據同步手段的直接忽視就行。
 */
public class LaunchActivity extends BaseActivity {

    /**
     * 是否正式服务器 ，true：需要checkHostPorts，否：不需要
     * 切換正式測試服務器IP時記得在此切換Boolean值。
     */
    private final static boolean isReleaseServer = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initEventAndData() {
        doJump();
    }

    /**
     * 执行跳转页面动作
     * */
    private void doJump() {
        final Observable<Boolean> observable;
        final Observable<Long> timer = Observable.timer(200L, TimeUnit.MILLISECONDS);
        if (isReleaseServer) {
            //2秒计时的同时确认服务器IP和端口，若2秒内确认服务器IP和端口完成，则等待到2秒时执行下一步。成功跳转界面并关掉本界面，失败则提示
            //若无法2秒确认服务器IP和端口完成，则等到何时确认完成，何时执行下一步。
            //10秒无法响应任何数据，认为是超时。
            final Observable<Boolean> checkHostPorts = CssServerApi.checkHostPorts(getApplication());
            final Observable<Boolean> error = Observable.error(new Throwable("Time out"));
            observable = Observable.zip(timer, checkHostPorts,
                    new Func2<Long, Boolean, Boolean>() {
                        @Override
                        public Boolean call(Long aLong, Boolean aBoolean) {
                            return aLong == 0L && aBoolean;
                        }
                    })
                    .timeout(10000L, TimeUnit.MILLISECONDS, error);
        } else {
            observable = timer.map(new Func1<Long, Boolean>() {
                @Override
                public Boolean call(Long aLong) {
                    return aLong == 0;
                }
            });
        }
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    private String message;
                    private boolean isSuccess = false;

                    @Override
                    public void onCompleted() {
                        if (isSuccess) {
                            Intent intent = null;
                            SharedPreferences sharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, MODE_PRIVATE);
                            if (sharedPreferences.getBoolean(IDatalifeConstant.LOGIN,false) == true){
                               intent = new Intent(getBaseContext(), MainActivity.class);
                            }else{
//                               intent = new Intent(getBaseContext(), MainActivity.class);
                               intent = new Intent(getBaseContext(), LoginActivity.class);
                            }

                            startActivity(intent);
                            finish();
                        } else {
//                            toast(TextUtils.isEmpty(message) ? "IP确认失败" : message);
                            Toast.makeText(getApplicationContext(),"IP确认失败",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        message = e.getMessage();
                        onCompleted();
                    }

                    @Override
                    public void onNext(Boolean o) {
                        isSuccess = o;
                    }
                });
    }

}
