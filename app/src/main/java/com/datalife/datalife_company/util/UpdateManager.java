package com.datalife.datalife_company.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.BuildConfig;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.widget.BaseDialog;

import java.io.File;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by LG on 2019/7/17.
 */

public class UpdateManager {

    private String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
    private int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
    public String url = "";//apk下载地址
    private String updateMessage = "";//更新内容
    private String fileName = null;//文件名
    private boolean isDownload = false;//是否下载
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private BaseDialog dialog;
    private ProgressDialog progressDialog;

    public static UpdateManager updateManager;
    private Handler mHandler;

    public static UpdateManager getInstance() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }
        return updateManager;
    }

    private UpdateManager() {

    }

    /**
     * 版本更新
     *
     * @param context
     * @param versionCode   版本号
     * @param url           apk下载地址
     * @param updateMessage 更新内容
     * @param isForced      是否强制更新
     */
    public void checkUpdate(Context context, int versionCode, String url, String updateMessage, boolean isForced, Handler handler) {
        this.mHandler = handler;
        this.url = url;
        if (versionCode > getVersionCode(context)) {
            int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
            if (UpdateManager.getInstance().isWifi(context)) {
                type = 1;
            }
            if (isForced) {
                type = 2;
            }

            //检测是否已下载
            String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/";
            File dir = new File(downLoadPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
            if (fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
                fileName = context.getPackageName() + ".apk";
            }
            File file = new File(downLoadPath + fileName);

            //设置参数
            UpdateManager.getInstance().setType(type).setUrl(url).setUpdateMessage(updateMessage).setFileName(fileName).setIsDownload(file.exists());
            if (type == 1 && !file.exists()) {
                UpdateManager.getInstance().downloadFile(context,url);
            } else {
                UpdateManager.getInstance().showDialog(context,url);
            }
        }else{
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("msg","已经是最新版本 ");
            message.setData(bundle);
            handler.sendMessage(message);
        }

    }

    /**
     * 弹出版本更新提示框
     */
    public void showDialog(final Context context,final String url) {
        String title = "";
        String left = "";
        boolean cancelable = true;
        if (type == 1 | isDownload) {
            title = "安装新版本";
            left = "立即安装";
        } else {
            title = "发现新版本";
            left = "立即更新";
        }
        if (type == 2) {
            cancelable = false;
        }
        dialog = new BaseDialog.Builder(context).setTitle(title).setMessage(updateMessage).setCancelable(cancelable)
                .setLeftClick(left, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (type == 1 | isDownload) {
                            installApk(context, new File(downLoadPath, fileName));
                        } else {
                            if (url != null && !TextUtils.isEmpty(url)) {
                                if (type == 2) {
                                    createProgress(context);
                                } else {
                                    createNotification(context);
                                }
                                downloadFile(context,url);
                            } else {
                                Message message = new Message();
                                Bundle bundle = new Bundle();
                                bundle.putString("msg","下载地址错误");
                                message.setData(bundle);
                                mHandler.sendMessage(message);
                            }
                        }
                    }
                })
                .setRightClick("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (type == 2) {
                            System.exit(0);
                        }
                    }
                })
                .create();
        dialog.show();
    }


    private void downloadFile(final Context context,final String url){
        //下载路径，如果路径无效了，可换成你的下载路径
//        final String url = "http://c.qijingonline.com/test.mkv";
        DownloadUtil.get().download(url, "download", new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("msg","下载完成");
                message.setData(bundle);
                mHandler.sendMessage(message);
                installApk(context, new File(downLoadPath, fileName));
            }
            @Override
            public void onDownloading(int progress,long total) {

                //实时更新通知栏进度条
                if (type == 0) {
                    notifyNotification(progress, total);
                } else if (type == 2) {
                    progressDialog.setProgress(progress);
                }
                if (total == (long)progress) {
                    if (type == 0) {
                        mBuilder.setContentText("下载完成");
                        mNotifyManager.notify(10086, mBuilder.build());
                    } else if (type == 2) {
                        progressDialog.setMessage("下载完成");
                    }
                    if (type == 1) {
                        showDialog(context,url);
                    } else {
                        installApk(context, new File(downLoadPath, fileName));
                    }
                }

            }
            @Override
            public void onDownloadFailed(String str) {
//               UToast.show(context, "下载失败");
                progressDialog.dismiss();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("msg",str);
                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        });
    }


    /**
     * 强制更新时显示在屏幕的进度条
     *
     */
    private void createProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在下载...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    /**
     * 创建通知栏进度条
     *
     */
    private void createNotification(Context context) {
        mNotifyManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher2);
        mBuilder.setContentTitle("版本更新");
        mBuilder.setContentText("正在下载...");
        mBuilder.setProgress(0, 0, false);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        mNotifyManager.notify(10086, notification);
    }

    /**
     * 更新通知栏进度条
     *
     */
    private void notifyNotification(int percent, long length) {
        mBuilder.setProgress((int) length, (int) percent, false);
        mNotifyManager.notify(10086, mBuilder.build());
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    private void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);

//        intent.setAction(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        context.startActivity(intent);
    }

    /**
     * @return 当前应用的版本号
     */
    public int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @return 当前应用的版本名
     */
    public double getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            double version = Double.parseDouble(info.versionName);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断当前网络是否wifi
     */
    public boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public UpdateManager setUrl(String url) {
        this.url = url;
        return this;
    }

    public UpdateManager setType(int type) {
        this.type = type;
        return this;
    }

    public UpdateManager setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
        return this;
    }

    public UpdateManager setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public UpdateManager setIsDownload(boolean download) {
        isDownload = download;
        return this;
    }

}
