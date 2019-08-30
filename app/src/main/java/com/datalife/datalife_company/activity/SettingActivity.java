package com.datalife.datalife_company.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.DownloadBean;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.contract.SettingContract;
import com.datalife.datalife_company.dao.WxUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.presenter.SettingPresenter;
import com.datalife.datalife_company.util.DataClean;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UToast;
import com.datalife.datalife_company.util.UpdateManager;
import com.datalife.datalife_company.widget.DownloadingDialog;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.update.BGADownloadProgressEvent;
import cn.bingoogolapple.update.BGAUpgradeUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by LG on 2019/7/17.
 */

public class SettingActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,SettingContract {

    @BindView(R.id.tv_clear)
    TextView tv_clear;
    @BindView(R.id.tv_version)
    TextView tv_version;

    /**
     * 下载文件权限请求码
     */
    private static final int RC_PERMISSION_DOWNLOAD = 1;
    /**
     * 删除文件权限请求码
     */
    private static final int RC_PERMISSION_DELETE = 2;

    private DownloadingDialog mDownloadingDialog;

    private String mNewVersion = "2";
    private String mApkUrl = "https://appapi.100zt.com/update/datalife.apk";
    //    private String mApkUrl = "http://192.168.0.168:81/update/datalife.apk";
    IWXAPI iwxapi = null;
    private WxUserInfo wxUserInfo = null;
    private boolean isBindWx = false;

    private SettingPresenter mSettingPresenter = new SettingPresenter();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(SettingActivity.this,msg.getData().getString("msg"),Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingPresenter.onStop();
    }

    @Override
    protected void initEventAndData() {
        mSettingPresenter.onCreate(this,this);
        iwxapi = WXAPIFactory.createWXAPI(this, IDatalifeConstant.APP_ID,true);
        iwxapi.registerApp(IDatalifeConstant.APP_ID);

        try {
            String cacheSize = DataClean.getTotalCacheSize(this);
            if (!cacheSize.equals("0K")) {
                tv_clear.setText(cacheSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_version.setText("v" + DeviceData.getVersionName("com.datalife.datalife_company"));

        // 监听下载进度
        BGAUpgradeUtil.getDownloadProgressEventObservable()
                .compose(this.<BGADownloadProgressEvent>bindToLifecycle())
                .subscribe(new Action1<BGADownloadProgressEvent>() {
                    @Override
                    public void call(BGADownloadProgressEvent downloadProgressEvent) {
                        if (mDownloadingDialog != null && mDownloadingDialog.isShowing() && downloadProgressEvent.isNotDownloadFinished()) {
                            mDownloadingDialog.setProgress(downloadProgressEvent.getProgress(), downloadProgressEvent.getTotal());
                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isBindWx) {
            wxUserInfo = DBManager.getInstance(this).queryWxInfo();
            SharedPreferences sharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, Context.MODE_PRIVATE);
            if (sharedPreferences != null && !sharedPreferences.getString("account", "").equals("")) {
                if (wxUserInfo != null && wxUserInfo.getOpenid() != null && wxUserInfo.getOpenid().trim().length() > 0) {
                    mSettingPresenter.bindUser(sharedPreferences.getString("account", ""), sharedPreferences.getString("password", ""), wxUserInfo.getOpenid(), wxUserInfo.getUnionid(), ProApplication.SESSIONID);
                }
            }
            isBindWx = false;
        }
    }

    @OnClick({R.id.rl_evaluate,R.id.rl_feedback,R.id.rl_about_app,R.id.rl_scavenging_caching,R.id.btn_exit,R.id.rl_update_app,R.id.iv_head_left,R.id.rl_wx_login})
    public void onClick(View view){

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        switch (view.getId()){
            case R.id.rl_evaluate:

                break;

            case R.id.rl_feedback:

                break;

            case R.id.rl_about_app:

                break;

            case R.id.rl_scavenging_caching:

                DataClean.clearAllCache(this);
                if (tv_clear.getText().toString().length() > 0) {
                    toast("清除缓存成功");
                }
                tv_clear.setText("");

                break;

            case R.id.btn_exit:

                sharedPreferences.edit().putBoolean("login",false).putString("account","").putString("password","").commit();
                String sessionid = "";
                if (sharedPreferences.getString("sessionid","") != null && sharedPreferences.getString("sessionid","").trim().toString().length() != 0){
                    sessionid = sharedPreferences.getString("sessionid","");
                }else{
                    sessionid = IDatalifeConstant.getUniqueId(this);
                }

                mSettingPresenter.loginout(sessionid);

                break;

            case R.id.rl_update_app:
                myPermission();
                mSettingPresenter.update(ProApplication.SESSIONID);
                break;

            case R.id.iv_head_left:
                finish();
                break;

            case R.id.rl_wx_login:
                LoginBean loginBean = null;
                try {
                    loginBean = (LoginBean) IDatalifeConstant.deSerialization(sharedPreferences.getString("logininfo",""));
                }catch (Exception e){

                }

                WxUserInfo wxUserInfo = DBManager.getInstance(this).queryWxInfo();
                if (wxUserInfo == null) {
                    isBindWx = true;
                    if (!iwxapi.isWXAppInstalled()) {
                        toast("您没有安装微信");
                    } else {
                        DBManager.getInstance(this).deleteWxInfo();
                        final SendAuth.Req req = new SendAuth.Req();
                        req.scope = "snsapi_userinfo";
                        req.state = "wechat_sdk_微信登录";
                        iwxapi.sendReq(req);
                    }
                }

                break;

        }
    }

    /**
     * 删除之前升级时下载的老的 apk 文件
     */
    @AfterPermissionGranted(RC_PERMISSION_DELETE)
    public void deleteApkFile() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 删除之前升级时下载的老的 apk 文件
            BGAUpgradeUtil.deleteOldApk();
        } else {
            EasyPermissions.requestPermissions(this, "使用 BGAUpdateDemo 需要授权读写外部存储权限!", RC_PERMISSION_DELETE, perms);
        }
    }

    /**
     * 下载新版 apk 文件
     */
    @AfterPermissionGranted(RC_PERMISSION_DOWNLOAD)
    public void downloadApkFile() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 如果新版 apk 文件已经下载过了，直接 return，此时不需要开发者调用安装 apk 文件的方法，在 isApkFileDownloaded 里已经调用了安装」
            if (BGAUpgradeUtil.isApkFileDownloaded(mNewVersion)) {
                return;
            }

            // 下载新版 apk 文件
            BGAUpgradeUtil.downloadApkFile(mApkUrl, mNewVersion)
                    .subscribe(new Subscriber<File>() {
                        @Override
                        public void onStart() {
                            showDownloadingDialog();
                        }

                        @Override
                        public void onCompleted() {
                            dismissDownloadingDialog();
                        }

                        @Override
                        public void onError(Throwable e) {
                            dismissDownloadingDialog();
                        }

                        @Override
                        public void onNext(File apkFile) {
                            if (apkFile != null) {
                                BGAUpgradeUtil.installApk(apkFile);
                            }
                        }
                    });
        } else {
            EasyPermissions.requestPermissions(this, "使用 BGAUpdateDemo 需要授权读写外部存储权限!", RC_PERMISSION_DOWNLOAD, perms);
        }
    }

    /**
     * 显示下载对话框
     */
    private void showDownloadingDialog() {
        if (mDownloadingDialog == null) {
            mDownloadingDialog = new DownloadingDialog(this);
        }
        mDownloadingDialog.show();
    }

    /**
     * 隐藏下载对话框
     */
    private void dismissDownloadingDialog() {
        if (mDownloadingDialog != null) {
            mDownloadingDialog.dismiss();
        }
    }

    @Override
    public void loginoutSuccess() {

        DBManager.getInstance(this).deleteAllFamilyUserInfoBean();
        DBManager.getInstance(this).deleteAllMachineBean();

//        AppManager.getAppManager().AppExit(this);
//        setResult(RESULT_OK);
//        finish();
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void loginoutFail(String failMsg) {

    }

    @Override
    public void updateSuccess(DownloadBean bean) {
        double code = UpdateManager.getInstance().getVersionName(this);
        double as = Double.parseDouble(bean.getVer());
        if (as > code) {
            mApkUrl = bean.getUrl();
            deleteApkFile();
            downloadApkFile();
        }else{
            toast("已是最新版本");
        }
    }

    @Override
    public void updateFail(String failMsg) {
        toast(failMsg);
    }

    @Override
    public void bindSuccess() {

    }

    @Override
    public void bindFail(String failMsg) {
        UToast.show(this,failMsg);
    }

}

