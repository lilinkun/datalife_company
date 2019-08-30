package com.datalife.datalife_company.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.BlueToothDevActivity;
import com.datalife.datalife_company.activity.LoginActivity;
import com.datalife.datalife_company.activity.RecordActivity;
import com.datalife.datalife_company.activity.RecordErrorActivity;
import com.datalife.datalife_company.activity.SettingActivity;
import com.datalife.datalife_company.activity.SimplebackActivity;
import com.datalife.datalife_company.activity.WebViewActivity;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseFragment;
import com.datalife.datalife_company.bean.ImageUploadResultBean;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.contract.MeContract;
import com.datalife.datalife_company.http.RetrofitHelper;
import com.datalife.datalife_company.interf.OnBackListener;
import com.datalife.datalife_company.presenter.MePresenter;
import com.datalife.datalife_company.util.FileImageUpload;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.widget.Eyes;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by LG on 2019/7/16.
 */

public class MeFragment extends BaseFragment implements MeContract {
    @BindView(R.id.tv_user_name)
    TextView mUserNameTv;
    @BindView(R.id.tv_user_tel)
    TextView mTelTv;
    @BindView(R.id.ic_user)
    ImageView roundImageView;
    @BindView(R.id.ll_unbind_wx)
    LinearLayout ll_unbind_wx;
    @BindView(R.id.view_pop)
    View view_pop;

    OnBackListener onBackListener = null;
    private MePresenter mePresenter = new MePresenter();
    private FragmentInteraction fragmentInteraction;
    private String mFilePath;
    private Uri cropImageUri;
    private String headImageUrl = "";

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_me;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123){
                String str = msg.getData().getString("str");
                if (str!= null && str.length() > 0) {
                    Gson gson = new Gson();
                    ImageUploadResultBean imageUploadResultBean = gson.fromJson(str, ImageUploadResultBean.class);
                    headImageUrl = imageUploadResultBean.getUrl().get(0);
                    mePresenter.uploadImage(imageUploadResultBean.getUrl().get(0), ProApplication.SESSIONID);
                }
            }
        }
    };

    @Override
    protected void initEventAndData() {
        Eyes.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.bg_toolbar_title));
//        Eyes.translucentStatusBar(getActivity());
        onBackListener = (OnBackListener) getActivity();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(IDatalifeConstant.LOGIN,Context.MODE_PRIVATE);

        mePresenter.onCreate(getActivity(),this);

        mFilePath = Environment.getExternalStorageDirectory()+ "/test/" + "temp1.jpg";// 获取SD卡路径

        if (sharedPreferences != null) {
            String str = sharedPreferences.getString("logininfo", "");
            try {
                LoginBean loginBean = (LoginBean)IDatalifeConstant.deSerialization(str);

                if (loginBean.getUnionid() != null && loginBean.getUnionid().trim().toString().length() > 0){
                    ll_unbind_wx.setVisibility(View.GONE);
                }

                mUserNameTv.setText(loginBean.getUser_name());
                mTelTv.setText(loginBean.getMobile());
                if (loginBean != null && loginBean.getHeadPic() != null){
                    if (loginBean.getHeadPic() != ""){
                        Glide.with(roundImageView.getContext())
                                .load(loginBean.getHeadPic())
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL) //设置缓存
                                .into(new BitmapImageViewTarget(roundImageView){
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        super.setResource(resource);
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                                        circularBitmapDrawable.setCornerRadius(10); //设置圆角弧度
                                        roundImageView.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFragmentInteraction(FragmentInteraction fragmentInteraction){
        this.fragmentInteraction = fragmentInteraction;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick({R.id.ll_unbind_wx,R.id.ic_user,R.id.rl_shoppingcar,R.id.rl_history,R.id.rl_equipmanager,R.id.rl_famalymanager,R.id.me_setting,R.id.rl_datetest,R.id.rl_help})
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ic_user:

//                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_USERINFO,SimpleBackPage.USERINFO);
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.pop_head_info,null);

                final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupWindow.showAsDropDown(view_pop);
//                }else {
//                    popupWindow.showAsDropDown(view_line);
//                }

                TextView tvExit = (TextView) view.findViewById(R.id.tv_pop_exit);
                TextView tvTakePhoto = (TextView) view.findViewById(R.id.tv_takephoto);
                TextView tvSystemPhoto = (TextView) view.findViewById(R.id.tv_systemphoto);

                tvExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                tvTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();

                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);} else {

                            File outputImage  = new File(mFilePath);
                            File str = outputImage.getParentFile();
                            if (!outputImage.getParentFile().exists()){
                                outputImage.getParentFile().mkdirs();
                            }

                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0及以上
                                //改变Uri  com.lzyyd.lyb.fileprovider注意和xml中的一致
                                Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.datalife.datalife_company.fileprovider", outputImage);
                                //添加权限
                                intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
                                intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);//增加读写权限
//                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);// 更改系统默认存储路径  
                            }else {
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
                            }
                            startActivityForResult(intent, IDatalifeConstant.REQUEST_CAMERA);
                        }
                    }
                });
                tvSystemPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        //调用相册
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, IDatalifeConstant.IMAGEBUNDLE);
                    }
                });

                break;

            case R.id.rl_equipmanager:

                Intent intent = new Intent();
                intent.setClass(getActivity(), BlueToothDevActivity.class);
                startActivity(intent);

                break;


            case R.id.me_setting:

//              UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_SETTING,SimpleBackPage.SYSTEMSETTING);

                UIHelper.launcherForResult(getActivity(), SettingActivity.class, IDatalifeConstant.SETTINGREQUESTCODE);

                break;

            case R.id.rl_famalymanager:

                UIHelper.showSimpleBackForResult(getActivity(), SimplebackActivity.RESULT_MEMBER, SimpleBackPage.FAMILYMANAGER);
                break;

            case R.id.rl_bindmember:

//                UIHelper.showSimpleBackForResult(getActivity(),SimplebackActivity.RESULT_BINDMEMBER,SimpleBackPage.BINDMEMBER);
                break;

            case R.id.rl_history:

//                UIHelper.launcherForResult(getActivity(), ToothDevActivity.class, 333);
                Bundle bundle = new Bundle();
//                bundle.putString(IDatalifeConstant.BUNDLEMEMBERID,mMemberId);
                UIHelper.launcherForResultBundle(getActivity(),RecordErrorActivity.class,22331,bundle);
//                UIHelper.launcherForResultBundle(getActivity(),RecordActivity.class,22331,bundle);
                break;

            case R.id.rl_datetest:
//                UIHelper.launcherForResult(getActivity(), DataTestActivity.class, 444);
                break;

            case R.id.rl_help:

                Intent intent1 = new Intent();
                intent1.setClass(getActivity(), WebViewActivity.class);
                intent1.putExtra("url",IDatalifeConstant.HELP_URL);
                intent1.putExtra("type","help");
                startActivity(intent1);

                break;

            case R.id.ll_unbind_wx:

                mePresenter.unBindUserWx(ProApplication.SESSIONID);

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == SimplebackActivity.RESULT_SETTING){
                getActivity().finish();
            }

            if (requestCode == IDatalifeConstant.INTENT_LOGIN){
                onBackListener.onBack();
            }

            //获取图片路径
            if (requestCode == IDatalifeConstant.IMAGEBUNDLE && resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
//            showImage(imagePath);
                c.close();

                startPhotoZoom(selectedImage);
            }else if(requestCode == IDatalifeConstant.REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri selectedImage = Uri.fromFile(new File(mFilePath));
                    Bitmap bm = BitmapFactory.decodeFile(new File(mFilePath).getAbsolutePath());
                    Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.datalife.datalife_company.fileprovider", new File(mFilePath));
                    startPhotoZoom(photoUri);
                }else {
                    Uri inputUri = Uri.fromFile(new File(mFilePath));
                    startPhotoZoom(inputUri);
                }
            }else if (requestCode == 4 && resultCode == Activity.RESULT_OK){
                final File file = new File(getActivity().getExternalCacheDir(), "crop.jpg");
                Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
                roundImageView.setImageBitmap(bm);

                new Thread(
                        new Runnable() {
                            @Override
                            public void run() {

                                String str = FileImageUpload.uploadFile(file, RetrofitHelper.ImageUrl);
                                Bundle bundle = new Bundle();
                                bundle.putString("str",str);
                                Message message = new Message();
                                message.setData(bundle);
                                message.what = 123;
                                handler.sendMessage(message);
                            }
                        }
                ).start();

            }

        }
    }

    public void startPhotoZoom(Uri paramUri)
    {
        File headFile = new File(getActivity().getExternalCacheDir(), "crop.jpg");
        try
        {
            if ((headFile).exists()) {
                (headFile).delete();
            }
            (headFile).createNewFile();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        this.cropImageUri = Uri.fromFile(headFile);

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(paramUri, "image/*");
        intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);

        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("output", this.cropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, 4);
    }

    @Override
    public void getDataSuccess(String info) {
        if (Integer.valueOf(info) > 0){
            ProApplication.REDCOUNT = true;
        }
    }

    @Override
    public void getDataFail(String msg) {
        ProApplication.REDCOUNT = false;
    }

    @Override
    public void getUnbindWxSuccess(LoginBean loginBean) {
        ll_unbind_wx.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(IDatalifeConstant.LOGIN, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("login",false).putString("account","").putString("password","").commit();

        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void getUnbindWxFail(String msg) {

    }

    @Override
    public void uploadImageSuccess(String uploadImageBean) {
        toast(uploadImageBean);
    }

    @Override
    public void uploadImageFail(String msg) {
        toast(msg);
    }


    public interface FragmentInteraction{
        void process(String str);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("LG","MeFragment");
    }
}
