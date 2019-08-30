package com.datalife.datalife_company.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.MainActivity;
import com.datalife.datalife_company.activity.MeasureDevActivity;
import com.datalife.datalife_company.activity.SimplebackActivity;
import com.datalife.datalife_company.base.BaseFragment;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.UIHelper;
import com.datalife.datalife_company.zxing.android.CaptureActivity;
import com.datalife.datalife_company.zxing.encode.CodeCreator;
import com.google.zxing.WriterException;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by LG on 2019/7/16.
 */
public class HomeFragment extends BaseFragment{

    @BindView(R.id.ic_qrcode)
    ImageView ic_qrcode;

    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_company_home;
    }

    @Override
    protected void initEventAndData() {

            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher2);
            Bitmap bitmap = CodeCreator.createQRCodeWithLogo(MainActivity.STOREID, bitmap1);
            ic_qrcode.setImageBitmap(bitmap);

    }

    @OnClick({R.id.ll_wx_scan,R.id.text_search,R.id.ic_qrcode})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ll_wx_scan:

                //动态权限申请
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    goScan();
                }

                break;
            case R.id.ic_qrcode:

            case R.id.text_search:

                Intent intent = new Intent(getActivity(), MeasureDevActivity.class);
                startActivity(intent);

                break;

        }
    }

    /**
     * 跳转到扫码界面扫码
     */
    private void goScan(){

        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //返回的BitMap图像
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

                toast("你扫描到的内容是：" + content);

                Intent intent = new Intent(getActivity(), MeasureDevActivity.class);
                startActivity(intent);

            }
        }
    }

}
