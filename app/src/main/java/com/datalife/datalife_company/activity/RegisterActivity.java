package com.datalife.datalife_company.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.datalife.datalife_company.R;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.ImageUploadResultBean;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.contract.RegisterContract;
import com.datalife.datalife_company.http.RetrofitHelper;
import com.datalife.datalife_company.presenter.RegisterPresenter;
import com.datalife.datalife_company.util.FileImageUpload;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UToast;
import com.datalife.datalife_company.widget.Eyes;
import com.google.gson.Gson;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * Created by LG on 2018/1/16.
 */

public class RegisterActivity extends BaseActivity implements RegisterContract {

    @BindView(R.id.iv_leftback)
    ImageView mLeftBack;
    @BindView(R.id.et_register_account)
    EditText mRegisterAccount;
    @BindView(R.id.et_register_address)
    EditText mRegisterAddress;
    @BindView(R.id.et_register_phone)
    EditText mRegisterPhone;
    @BindView(R.id.et_register_name)
    EditText mRegisterName;
    @BindView(R.id.et_register_psw)
    EditText mRegisterPsw;
    @BindView(R.id.ic_psd)
    ImageView iv_psd;
    @BindView(R.id.view_pop)
    View view_pop;
    @BindView(R.id.iv_photo)
    ImageView iv_photo;
    @BindView(R.id.iv_photo1)
    ImageView iv_photo1;
    @BindView(R.id.iv_photo2)
    ImageView iv_photo2;
    @BindView(R.id.rl_take_photo1)
    RelativeLayout rl_take_photo1;
    @BindView(R.id.rl_take_photo2)
    RelativeLayout rl_take_photo2;
    @BindView(R.id.ll_pic_layout)
    LinearLayout ll_pic_layout;
    @BindView(R.id.ll_user)
    LinearLayout ll_user;
    @BindView(R.id.ll_company_info)
    LinearLayout ll_company_info;
    @BindView(R.id.et_register_username)
    EditText et_register_username;

    private boolean isClose = true;

    ProgressDialog progressDialog = null;

    private RegisterPresenter mRegisterPresenter = new RegisterPresenter();

    private String username,store_name,password,store_address,responsibility_name,phone;
    String sessionid = "";
    String openid = "";
    String unionid = "";
    private String mFilePath;
    private Uri cropImageUri;
    private int clickitem = 0;//1为上传第一张图片，2为上传第二张图片，3为上传第三张图片

    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;

    private static final int PIC_CODE = 0x1234;
    // 已选中的照片地址， 用于回显选中状态
    private ArrayList<String> imagePaths = new ArrayList<>();
    private String cert = "";
    private int picTimes = 0;
    ProgressDialog progressDialog1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123){
                String str = msg.getData().getString("str");
                if (str!= null && str.length() > 0) {
                    Gson gson = new Gson();
                    ImageUploadResultBean imageUploadResultBean = gson.fromJson(str, ImageUploadResultBean.class);
                    mRegisterPresenter.uploadImage(imageUploadResultBean.getUrl().get(0), ProApplication.SESSIONID);
                }
            }else if (msg.what == 2332){
                picTimes++;
                String str = msg.getData().getString("pics");
                if (str!= null && str.length() > 0) {
                    Gson gson = new Gson();
                    ImageUploadResultBean imageUploadResultBean = gson.fromJson(str, ImageUploadResultBean.class);
                    if (cert.isEmpty()){
                        cert =imageUploadResultBean.getUrl().get(0);
                    }else {
                        cert += "," + imageUploadResultBean.getUrl().get(0);
                    }
                }
                if (picTimes == imagePaths.size()){
                    picTimes = 0;
                    mRegisterPresenter.register(username,store_name,store_address,responsibility_name,phone,password,cert,IDatalifeConstant.getUniqueId(RegisterActivity.this));
                }
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initEventAndData() {

        Eyes.setStatusBarColor(this,getResources().getColor(R.color.bg_toolbar_title));

        if (getIntent() != null){
            sessionid  = getIntent().getStringExtra(IDatalifeConstant.SESSIONID);
            openid = getIntent().getStringExtra(IDatalifeConstant.OPENID);
            unionid = getIntent().getStringExtra(IDatalifeConstant.UNIONID);
        }else{
            sessionid = IDatalifeConstant.getUniqueId(this);
        }

        mRegisterPresenter.onCreate(this,this);

        mFilePath = Environment.getExternalStorageDirectory()+ "/test/" + "temp2.jpg";// 获取SD卡路径

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.login_loading));
        progressDialog.setCancelable(true);
    }

    @OnClick({R.id.tv_usage_protocol,R.id.iv_leftback,R.id.btn_register_over,R.id.ic_psd,R.id.rl_take_photo,R.id.rl_take_photo1,R.id.rl_take_photo2,R.id.btn_register_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_usage_protocol:
//                UToast.show(this,);
                Intent intent = new Intent();
                intent.setClass(this,WebViewActivity.class);
                intent.putExtra("url", IDatalifeConstant.USAGEPROTOCOL_URL);
                intent.putExtra("type","register");
                startActivity(intent);
                break;

            case R.id.iv_leftback:
                finish();
                break;


            case R.id.btn_register_over:

                store_name = mRegisterAccount.getText().toString();
                password = mRegisterPsw.getText().toString();
                responsibility_name = mRegisterName.getText().toString();
                phone = mRegisterPhone.getText().toString();
                store_address = mRegisterAddress.getText().toString();

//                mRegisterPresenter.register(username,address,psw,name,phone, sessionid,progressDialog,openid,unionid);

                progressDialog1 = new ProgressDialog(this);
//                        ProgressDialog.show(this,"请稍等...","注册中...",true);
                mRegisterPresenter.registerCompany(store_name,store_address,responsibility_name,phone,password,imagePaths,progressDialog1,handler);
//                (String Store_Name, String Store_Address, String Responsibility_Name, String Phone, String PassWord, ArrayList<String> Certificates, String SessionId,final Handler handler){
//                progressDialog.show();

                break;

            case R.id.ic_psd:

                if (isClose){
                    iv_psd.setImageResource(R.mipmap.ic_open_psd);
                    mRegisterPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mRegisterPsw.setSelection(mRegisterPsw.getText().toString().length());
                    isClose = false;
                }else{
                    iv_psd.setImageResource(R.mipmap.ic_close_psd);
                    mRegisterPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mRegisterPsw.setSelection(mRegisterPsw.getText().toString().length());
                    isClose = true;
                }
                break;

            case R.id.rl_take_photo:
                clickitem = 1;
                if (imagePaths != null && imagePaths.size() >= 1){
                    deletePic(0);
                }else {
                    takephoto();
                }
                break;

            case R.id.rl_take_photo1:
                clickitem = 2;
                if (imagePaths != null && imagePaths.size() >= 2){
                    deletePic(1);
                }else {
                    takephoto();
                }
                break;

            case R.id.rl_take_photo2:
                clickitem = 3;
                if (imagePaths != null && imagePaths.size() >= 3){
                    deletePic(2);
                }else {
                    takephoto();
                }
                break;

            case R.id.btn_register_next:

                if (et_register_username != null && et_register_username.getText().toString().isEmpty()){
                    toast(R.string.prompt_login_name_not_empty);
                    return;
                }

                if (mRegisterPsw != null && mRegisterPsw.getText().toString().isEmpty()){
                    toast(R.string.prompt_login_passwrod_not_empty);
                    return;
                }

                if (mRegisterPsw.getText().toString().length() < 6 || mRegisterPsw.getText().toString().length() > 20){
                    toast(R.string.prompt_passwrod_not_allow);
                    return;
                }

                if (et_register_username.getText().toString().length() < 3 || et_register_username.getText().toString().length() > 10){
                    toast(R.string.prompt_login_name_not_allow);
                    return;
                }

                username = et_register_username.getText().toString();
                password = mRegisterPsw.getText().toString();

                ll_user.setVisibility(View.GONE);
                ll_company_info.setVisibility(View.VISIBLE);

                break;
        }

    }

    private void deletePic(int position){
        PhotoPreviewIntent intent = new PhotoPreviewIntent(RegisterActivity.this);
        intent.setCurrentItem(position);
        intent.setPhotoPaths(imagePaths);
        startActivityForResult(intent, REQUEST_PREVIEW_CODE);
    }

    private void takephoto(){
        View pop_view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.pop_head_info,null);

        final PopupWindow popupWindow = new PopupWindow(pop_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        popupWindow.showAsDropDown(view_pop);
//                }else {
//                    popupWindow.showAsDropDown(view_line);
//                }

        TextView tvExit = (TextView) pop_view.findViewById(R.id.tv_pop_exit);
        TextView tvTakePhoto = (TextView) pop_view.findViewById(R.id.tv_takephoto);
        TextView tvSystemPhoto = (TextView) pop_view.findViewById(R.id.tv_systemphoto);

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

                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(RegisterActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegisterActivity.this,
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
                        Uri photoUri = FileProvider.getUriForFile(RegisterActivity.this, "com.datalife.datalife_company.fileprovider", outputImage);
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
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, DataLifeUtil.IMAGEBUNDLE);

                PhotoPickerIntent intent = new PhotoPickerIntent(RegisterActivity.this);
                intent.setSelectModel(SelectModel.MULTI);
                intent.setShowCarema(true); // 是否显示拍照
                intent.setMaxTotal(3); // 最多选择照片数量，默认为6
                intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent, REQUEST_CAMERA_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //获取图片路径
        if (requestCode == IDatalifeConstant.IMAGEBUNDLE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
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
                Uri photoUri = FileProvider.getUriForFile(this, "com.datalife.datalife_company.fileprovider", new File(mFilePath));
                startPhotoZoom(photoUri);
            }else {
                Uri inputUri = Uri.fromFile(new File(mFilePath));
                startPhotoZoom(inputUri);
            }
        }else if (requestCode == 4 && resultCode == Activity.RESULT_OK){
            final File file = new File(getExternalCacheDir(), "crop" + clickitem + ".jpg");
            Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
            if (clickitem == 1) {
                iv_photo.setVisibility(View.VISIBLE);
                iv_photo.setImageBitmap(bm);
                rl_take_photo1.setVisibility(View.VISIBLE);
            }else if (clickitem == 2){
                iv_photo1.setVisibility(View.VISIBLE);
                iv_photo1.setImageBitmap(bm);
                rl_take_photo2.setVisibility(View.VISIBLE);
            }else if (clickitem == 3){
                iv_photo2.setVisibility(View.VISIBLE);
                iv_photo2.setImageBitmap(bm);
            }

            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {

                            String str = FileImageUpload.uploadFile(file, RetrofitHelper.CertificatesImageUrl);
                            Bundle bundle = new Bundle();
                            bundle.putString("str",str);
                            Message message = new Message();
                            message.setData(bundle);
                            message.what = 123;
                            handler.sendMessage(message);
                        }
                    }
            ).start();

        }else if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK){
            setPic(data);
        }else if (requestCode == REQUEST_PREVIEW_CODE && resultCode == Activity.RESULT_OK){
            setPic(data);
        }

    }

    private void setPic(Intent data){
        imagePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);

        if (imagePaths == null || imagePaths.size() == 0){
            ll_pic_layout.setVisibility(View.VISIBLE);
            rl_take_photo1.setVisibility(View.GONE);
            rl_take_photo2.setVisibility(View.GONE);
            iv_photo.setVisibility(View.GONE);
        }else {
            for (int i = 0; i < imagePaths.size(); i++) {
                String pic = imagePaths.get(i);
                if (i == 0) {
                    Glide.with(RegisterActivity.this).load(pic)
                            .placeholder(R.mipmap.default_error)
                            .error(R.mipmap.default_error)
                            .into(iv_photo);
                    iv_photo.setVisibility(View.VISIBLE);
                    ll_pic_layout.setVisibility(View.GONE);
                    rl_take_photo1.setVisibility(View.VISIBLE);
                    rl_take_photo2.setVisibility(View.GONE);
                    iv_photo1.setVisibility(View.GONE);
                } else if (i == 1) {
                    Glide.with(RegisterActivity.this).load(pic)
                            .placeholder(R.mipmap.default_error)
                            .error(R.mipmap.default_error)
                            .into(iv_photo1);
                    rl_take_photo2.setVisibility(View.VISIBLE);
                    iv_photo1.setVisibility(View.VISIBLE);
                    iv_photo2.setVisibility(View.GONE);
                } else if (i == 2) {
                    Glide.with(RegisterActivity.this).load(pic)
                            .placeholder(R.mipmap.default_error)
                            .error(R.mipmap.default_error)
                            .into(iv_photo2);
                    iv_photo2.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void startPhotoZoom(Uri paramUri)
    {
        File headFile = new File(getExternalCacheDir(), "crop.jpg");
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
    protected void onDestroy() {
        super.onDestroy();
        mRegisterPresenter.onStop();
    }

    @Override
    public void showPromptMessage(int resId) {
        toast(resId);
    }

    @Override
    public void uploadImageSuccess(String uploadImageBean) {
        toast(uploadImageBean);
    }

    @Override
    public void uploadImageFail(String msg) {
        toast(msg);
    }

    @Override
    public void registerCompanySuccess(LoginBean msg) {
//        progressDialog
//        toast(msg.getMobile());
//        mRegisterPresenter.login(username,password,DeviceData.getUniqueId(this));
        String datalife = null;
        try {
            datalife = IDatalifeConstant.serialize(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, MODE_PRIVATE);
        sharedPreferences.edit().putString("sessionid",sessionid).putBoolean(IDatalifeConstant.LOGIN,true).putString("logininfo",datalife).commit();
        if (progressDialog1.isShowing()) {
            progressDialog1.dismiss();
        }

        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void registerCompanyFail(String msg) {
        toast(msg);
        if (progressDialog1.isShowing()) {
            progressDialog1.dismiss();
        }
    }

}
