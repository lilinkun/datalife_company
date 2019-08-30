package com.datalife.datalife_company.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.SimplebackActivity;
import com.datalife.datalife_company.adressselectorlib.AddressPickerView;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseFragment;
import com.datalife.datalife_company.bean.ImageUploadResultBean;
import com.datalife.datalife_company.bean.MachineBindMemberBean;
import com.datalife.datalife_company.bean.NavUserInfo;
import com.datalife.datalife_company.bean.ProvinceBean;
import com.datalife.datalife_company.contract.AddFamilyUserContract;
import com.datalife.datalife_company.dao.FamilyUserInfo;
import com.datalife.datalife_company.db.DBManager;
import com.datalife.datalife_company.http.RetrofitHelper;
import com.datalife.datalife_company.interf.FragmentCallBack;
import com.datalife.datalife_company.interf.OnTitleBarClickListener;
import com.datalife.datalife_company.interf.onTitleBarSetListener;
import com.datalife.datalife_company.presenter.AddUserPresener;
import com.datalife.datalife_company.util.DeviceData;
import com.datalife.datalife_company.util.FileImageUpload;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.MyCalendar;
import com.datalife.datalife_company.widget.MyDateLinear;
import com.datalife.datalife_company.widget.MyHeightAndWeight;
import com.datalife.datalife_company.widget.PopWindowSexSelectBy;
import com.datalife.datalife_company.widget.RoundImageView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.datalife.datalife_company.adressselectorlib.AddressPickerView.TYPE_PROVINCE;

/**
 * Created by LG on 2019/7/17.
 */

public class AddUserFragment extends BaseFragment implements MyDateLinear.MyDateLinearListener,OnTitleBarClickListener,AddFamilyUserContract{


    private PopWindowSexSelectBy popWindowSexSelectBy;
    private MyCalendar calendar;

    private ArrayList<String> height_arrayList = new ArrayList<String>();
    private ArrayList<String> height_arrayList_right = new ArrayList<String>();
    private ArrayList<String> weight_arrayList = new ArrayList<String>();
    NavUserInfo navUserInfo = null;
    FamilyUserInfo familyUserInfo = null;

    private int state;
    private final int DEFAULT = 0;
    private final int MYHEIGHT = 1;
    private final int MYWEIGHT = 2;
    private final int MYDATE = 4;
    private String last_time, my_height,my_weight;
    private int years;
    private int wm_width, wm_height;

    //sex 0代表男 1代表女
    private String Gender = "0";

    @BindView(R.id.myheight)
    MyHeightAndWeight myHeight;
    @BindView(R.id.myWeight)
    MyHeightAndWeight myWeight;
    @BindView(R.id.mydate)
    MyDateLinear myDateLinear;
    //    @BindView(R.id.text_sex)
//    TextView mAddSex;
    @BindView(R.id.text_age)
    TextView mAddAge;
    @BindView(R.id.text_height)
    TextView mAddHeight;
    @BindView(R.id.text_weight)
    TextView mAddWeight;
    @BindView(R.id.ll_height)
    LinearLayout ll_height;
    @BindView(R.id.et_username)
    EditText mAddUserName;
    @BindView(R.id.ic_adduserhead)
    RoundImageView mRoundImageView;
    @BindView(R.id.rg_sex)
    RadioGroup mSexRadioGroup;
    @BindView(R.id.rb_man)
    RadioButton mManRadioButton;
    @BindView(R.id.rb_woman)
    RadioButton mWomanRadioButton;
    @BindView(R.id.text_address)
    TextView text_address;
    @BindView(R.id.im_bg)
    ImageView im_bg;
    @BindView(R.id.ll_address)
    LinearLayout ll_address;
    @BindView(R.id.text_tel)
    EditText text_tel;
    @BindView(R.id.rl_adduser)
    RelativeLayout rl_adduser;
    @BindView(R.id.view_line)
    View view_line;

    private onTitleBarSetListener mListener;
    private FragmentCallBack listener;
    private int clickItem = 1;

    private AddressPickerView addressView;
    private String mProvinceCode;
    private String mCityCode;
    private String mAreaCode;
    private String mZipCode;
    private String isDefault = "0";

    private boolean mUpdateMemberInfo = false;

    PopupWindow popupWindow;
    private static final int IMAGEBUNDLE = 0x221;
    private static final int REQUEST_CAMERA = 0x222;
    private String mFilePath;
    private  Uri cropImageUri;
    private String headImageUrl = "1";
    AddUserPresener mAddUserPresener = new AddUserPresener();


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 123){
                String str = msg.getData().getString("str");
                if (str!= null && str.length() > 0) {
                    Gson gson = new Gson();
                    ImageUploadResultBean imageUploadResultBean = gson.fromJson(str, ImageUploadResultBean.class);
                    headImageUrl = imageUploadResultBean.getUrl().get(0);
        //          userInfoPresenter.uploadImage(imageUploadResultBean.getUrl().get(0), ProApplication.SESSIONID);
                }
            }
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_adduser;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (onTitleBarSetListener) context;
        mListener.setRightText(getActivity().getResources().getString(R.string.text_change_ok));
        mListener.setRightTextVisibility(View.VISIBLE);
        listener = (SimplebackActivity) context;
    }

    @Override
    protected void initEventAndData() {
        mAddUserPresener.onCreate(getActivity(),this);

        mAddUserPresener.getLocalData("1", TYPE_PROVINCE);

        mFilePath = Environment.getExternalStorageDirectory()+ "/test/" + "temp.jpg";// 获取SD卡路径

        Display display = IDatalifeConstant.display(getActivity());
        wm_width = display.getWidth();
        wm_height = display.getHeight();
        initHeightArrayList();

        calendar = calendar.getInstance();
        myDateLinear.setMyDateLinearListener(this);
        Calendar c = Calendar.getInstance();
        myDateLinear.setMaxYear(calendar.get(Calendar.YEAR));
//        myDateLinear.setMinYear(1950);
        myDateLinear.init();
        myDateLinear.init(1998,
                1,
                1);

        myHeight.setMyDateLinearListener(this);
        myHeight.setArrayListYear(height_arrayList);
        myHeight.setArrayListMonth(height_arrayList_right);
        myHeight.setSelectLeftId(61);
        myHeight.setSelectRightId(2);
        myHeight.setTitle_one(getResources().getString(R.string.user_height));
        myHeight.setTitle_two("cm");
        myHeight.init();

        myWeight.setMyDateLinearListener(this);
        myWeight.setArrayListYear(weight_arrayList);
        myWeight.setArrayListMonth(height_arrayList_right);
        myWeight.setSelectLeftId(51);
        myWeight.setSelectRightId(1);
        myWeight.setTitle_one(getResources().getString(R.string.my_weight));
        myWeight.setTitle_two("kg");
        myWeight.init();

        mSexRadioGroup.check(R.id.rb_man);

        if (getActivity().getIntent() != null ) {
            if (getActivity().getIntent().getBundleExtra(SimplebackActivity.BUNDLE_KEY_ARGS) != null) {
                if (getActivity().getIntent().getBundleExtra(SimplebackActivity.BUNDLE_KEY_ARGS).getSerializable("familyUserInfo") != null) {
                    familyUserInfo = (FamilyUserInfo) getActivity().getIntent().getBundleExtra(SimplebackActivity.BUNDLE_KEY_ARGS).getSerializable("familyUserInfo");
//                    toast(familyUserInfo.getMember_Name());
                    mAddUserName.setText(familyUserInfo.getMember_Name());
                    mAddHeight.setText(familyUserInfo.getMember_Stature() + "cm");
                    mAddWeight.setText(familyUserInfo.getMember_Weight() + "kg");
                    mAddAge.setText(familyUserInfo.getMember_DateOfBirth());
                    text_tel.setText(familyUserInfo.getMember_Phone());
                    text_address.setText(familyUserInfo.getAddressName());
                    mProvinceCode = familyUserInfo.getMember_Province();
                    mCityCode = familyUserInfo.getMember_City();
                    mAreaCode = familyUserInfo.getMember_District();
                    headImageUrl = familyUserInfo.getMember_Portrait();

                    if (familyUserInfo.getMember_Sex().equals("0")){
                        mManRadioButton.setChecked(true);
                        mWomanRadioButton.setChecked(false);
                    }else if (familyUserInfo.getMember_Sex().equals("1")){
                        mManRadioButton.setChecked(false);
                        mWomanRadioButton.setChecked(true);
                    }

                    mUpdateMemberInfo = true;
                    if (familyUserInfo.getMember_Portrait().toString().trim().length() > 1) {
                        {
                            Picasso.with(getActivity()).load(familyUserInfo.getMember_Portrait()).into(mRoundImageView);
                        }
                    }else if (familyUserInfo.getMember_Portrait().toString().length() == 1) {
                        IDatalifeConstant.GetPIC(getActivity(), familyUserInfo.getMember_Portrait(), mRoundImageView);
                    }
                }
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAddUserPresener.onStop();
    }

    @OnClick({R.id.ll_age,R.id.ll_weight,R.id.ll_height,R.id.ic_adduserhead,R.id.et_username,R.id.ll_address})
    public void onClick(View view){
        switch (view.getId()){

            case R.id.et_username:
                hideLinear();

                break;

            case R.id.ll_age:
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                        (getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                hideLinear();
//                myDateLinear.init(calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH) + 1,
//                        calendar.get(Calendar.DAY_OF_MONTH));
                myDateLinear.init(2000,1,1);
                setMyVisible(myDateLinear, MYDATE);

                myDateLinear.setListYear();
                break;

            case R.id.ll_weight:
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                        (getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                hideLinear();

                setMyVisible(myWeight, MYWEIGHT);

                break;

            case R.id.ll_height:

                /*InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()){
                    imm.hideSoftInputFromInputMethod(mAddUserName.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }*/
                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                        (getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                hideLinear();
                setMyVisible(myHeight, MYHEIGHT);
                break;

            case R.id.ic_adduserhead:

                View v = LayoutInflater.from(getActivity()).inflate(R.layout.pop_head_info,null);

                final PopupWindow popupWindow = new PopupWindow(v, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    popupWindow.showAsDropDown(rl_adduser);
                }else {
                    popupWindow.showAsDropDown(view_line);
                }

                TextView tvExit = (TextView) v.findViewById(R.id.tv_pop_exit);
                TextView tvTakePhoto = (TextView) v.findViewById(R.id.tv_takephoto);
                TextView tvSystemPhoto = (TextView) v.findViewById(R.id.tv_systemphoto);

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
                            startActivityForResult(intent, REQUEST_CAMERA);
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
                        startActivityForResult(intent, IMAGEBUNDLE);
                    }
                });

                myDateLinear.setVisibility(View.GONE);
                myWeight.setVisibility(View.GONE);
                myHeight.setVisibility(View.GONE);

                break;

            case R.id.ll_address:

                if (im_bg != null) {
                    im_bg.setVisibility(View.VISIBLE);
                }
                final PopupWindow popupWindow1 = new PopupWindow(getActivity());
                View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_address_picker, null, false);
                addressView = rootView.findViewById(R.id.apvAddress);
                addressView.setOnAddressPickerSure(new AddressPickerView.OnAddressPickerSureListener() {
                    @Override
                    public void onSureClick(String address, String provinceCode, String cityCode, String districtCode, String zipCode) {
                        text_address.setText(address);
                        mProvinceCode = provinceCode;
                        mCityCode = cityCode;
                        mAreaCode = districtCode;
                        mZipCode = zipCode;
                        popupWindow1.dismiss();
                    }
                });
                popupWindow1.setContentView(rootView);
                popupWindow1.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow1.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow1.setBackgroundDrawable(new BitmapDrawable());
                popupWindow1.setFocusable(true);
                popupWindow1.setOutsideTouchable(true);
                popupWindow1.showAsDropDown(ll_height);
                popupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (im_bg != null && im_bg.isShown()) {
                            im_bg.setVisibility(View.GONE);
                        }
                    }
                });

                break;
        }
    }


    /**
     * edittext值
     * @param textView
     * @return
     */
    public String dataStr(TextView textView){
        if (textView == null  || textView.getText().toString().trim().length() == 0){
            return "";
        }else{
            return textView.getText().toString();
        }
    }

    public void initHeightArrayList() {
        for (int i = 100; i <= 220; i++) {
            String str = i + "";
            height_arrayList.add(str);
        }
        for (int i = 0; i <= 9; i++) {
            String str = "." + i;
            height_arrayList_right.add(str);
        }
        for (int i = 10; i <= 130; i++) {
            String str = i + "";
            weight_arrayList.add(str);
        }
    }


    /**
     * edittext是否为空
     * @param editText
     * @return
     */
    public boolean isEmpty(EditText editText){
        if(editText == null  || editText.getText().toString().trim().length() == 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * testview是否为空
     * @param textView
     * @return
     */
    public boolean isTextEmpty(TextView textView) {
        if (textView == null || textView.getText().toString().trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setMyVisible(View view, int state) {
        if (myDateLinear.getVisibility() == View.VISIBLE || myHeight.getVisibility() == View.VISIBLE ) {
            return;
        }
        this.state = state;
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fenxiang_weiyi_input);
        view.setAnimation(animation);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public int getscreenWidth() {
        return wm_width;
    }

    @Override
    public int getscreenHeight() {
        return wm_height;
    }

    @Override
    public void cancle() {
        if (state == MYDATE) {
            setMyInvisible(myDateLinear);
            state = DEFAULT;
        } else if (state == MYHEIGHT) {
            setMyInvisible(myHeight);
            state = DEFAULT;
        } else if (state == MYWEIGHT) {
            setMyInvisible(myWeight);
            state = DEFAULT;
        }
    }

    @Override
    public void sure(String dateStr) {
        if (state == MYDATE) {
            setMyInvisible(myDateLinear);
            state = DEFAULT;
            String after_date = myDateLinear.getSelcet_my_year() + "-"
                    + myDateLinear.getSelcet_my_month() + "-"
                    + myDateLinear.getSelcet_my_day();
            String str_date = after_date;
            last_time = str_date;
            calendar.set(Calendar.YEAR, myDateLinear.getSelcet_my_year());
            calendar.set(Calendar.MONTH, myDateLinear.getSelcet_my_month() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, myDateLinear.getSelcet_my_day());

            Date now = new Date();
            Date born = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                born = df.parse(str_date);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            String str = df.format(born);
            long days = (now.getTime() - born.getTime()) / (1000 * 60 * 60 * 24);//得到总天数
            years = (int) days / 365;
            Log.e("years:", years + "");
            mAddAge.setText(str);

        } else if (state == MYHEIGHT) {
            setMyInvisible(myHeight);
            state = DEFAULT;
            my_height = myHeight.getData();
            mAddHeight.setText((int) Double.parseDouble(my_height) + "cm");
        } else if (state == MYWEIGHT) {
            setMyInvisible(myWeight);
            state = DEFAULT;
            my_weight = myWeight.getData();
            mAddWeight.setText((int) Double.parseDouble(my_weight) + "kg");
        }
    }


    private void hideLinear() {

        if (state == MYHEIGHT) {
            setMyInvisible(myHeight);
            state = DEFAULT;
        } else if (state == MYDATE) {
            setMyInvisible(myDateLinear);
            state = DEFAULT;
        } else if (state == MYWEIGHT) {
            setMyInvisible(myWeight);
            state = DEFAULT;
        }
    }

    @Override
    public void realTime(String data) {
        if (state == MYDATE) {
            String after_date = myDateLinear.getSelcet_my_year() + "-"
                    + myDateLinear.getSelcet_my_month() + "-"
                    + myDateLinear.getSelcet_my_day();
            String str_date = after_date;
            last_time = str_date;
            calendar.set(Calendar.YEAR, myDateLinear.getSelcet_my_year());
            calendar.set(Calendar.MONTH, myDateLinear.getSelcet_my_month() - 1);
            calendar.set(Calendar.DAY_OF_MONTH, myDateLinear.getSelcet_my_day());

            Date now = new Date();
            Date born = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                born = df.parse(str_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            long days = (now.getTime() - born.getTime()) / (1000 * 60 * 60 * 24);//得到总天数
            years = (int) days / 365;
            Log.e("years:", years + "");
//            mAddAge.setText(years + " ");

        } else if (state == MYHEIGHT) {
            my_height = myHeight.getData();
            mAddHeight.setText((int) Double.parseDouble(my_height) + "cm");
        } else if (state == MYWEIGHT) {
            my_weight = myWeight.getData();
            mAddWeight.setText((int) Double.parseDouble(my_weight) + "kg");
        }
    }

    public void setMyInvisible(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fenxiang_weiyi_output);
        view.setAnimation(animation);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onBackClick() {
        getActivity().finish();
    }

    @Override
    public void onMoreClick() {
        navUserInfo = new NavUserInfo();
        if (!mUpdateMemberInfo) {
            familyUserInfo = new FamilyUserInfo();
        }
        if (isEmpty(mAddUserName)){
            toast("请输入用户名");
            return;
        }else if (isTextEmpty(mAddAge)) {
            toast("请输入年龄");
            return;
        }else if(isTextEmpty(mAddHeight)) {
            toast("请输入身高");
            return;
//        } else if(isTextEmpty(mAddSex)) {
//            toast("请输入性别");
//            return;
        }else if(isTextEmpty(mAddWeight)) {
            toast("请输入体重");
            return;
        }else if(text_tel.getText().toString().trim().length() == 0){
            toast("请输入电话号码");
        }else if(isTextEmpty(text_address)){
            toast("请选择成员地址");
        }else{
            navUserInfo.setAge(dataStr(mAddAge));
            navUserInfo.setUsername(mAddUserName.getText().toString());
            navUserInfo.setHeight(IDatalifeConstant.getPlusUnit(dataStr(mAddHeight)));

            int sexid = mSexRadioGroup.getCheckedRadioButtonId();
            switch (sexid){
                case R.id.rb_man:
                    navUserInfo.setSex(getActivity().getResources().getString(R.string.my_sex_nan));
                    Gender = "0";
                    break;

                case R.id.rb_woman:
                    navUserInfo.setSex(getActivity().getResources().getString(R.string.my_sex_nv));
                    Gender = "1";
                    break;
            }

//            navUserInfo.setSex(dataStr(mAddSex));
            navUserInfo.setWeight(IDatalifeConstant.getPlusUnit(dataStr(mAddWeight)));

            familyUserInfo.setCreateDate(dataStr(mAddAge));
            familyUserInfo.setMember_DateOfBirth(dataStr(mAddAge));
            familyUserInfo.setMember_Sex(Gender);
            familyUserInfo.setMember_Name(mAddUserName.getText().toString());
            familyUserInfo.setMember_Stature(Double.parseDouble(IDatalifeConstant.getPlusUnit(dataStr(mAddHeight))));
            familyUserInfo.setMember_Weight(Double.parseDouble(IDatalifeConstant.getPlusUnit(dataStr(mAddWeight))));
            familyUserInfo.setMember_Portrait(headImageUrl);
            familyUserInfo.setMember_Phone(text_tel.getText().toString() + "");
            familyUserInfo.setMember_Province(mProvinceCode);
            familyUserInfo.setMember_City(mCityCode);
            familyUserInfo.setMember_District(mAreaCode);
        }
        String sessionid = DeviceData.getUniqueId(getActivity());
        if (!mUpdateMemberInfo) {
            mAddUserPresener.addFamilyUser(familyUserInfo.getMember_Name(), familyUserInfo.getMember_Portrait(), IDatalifeConstant.getPlusUnit(dataStr(mAddHeight)), IDatalifeConstant.getPlusUnit(dataStr(mAddWeight)), familyUserInfo.getCreateDate(), familyUserInfo.getMember_Sex(), "0", "1", sessionid, familyUserInfo.getMember_Phone(), familyUserInfo.getMember_Province(), familyUserInfo.getMember_City(), familyUserInfo.getMember_District());
        }else {
            mAddUserPresener.updateFamilyUser(familyUserInfo.getMember_Id()+"",familyUserInfo.getMember_Name(), familyUserInfo.getMember_Portrait(), IDatalifeConstant.getPlusUnit(dataStr(mAddHeight)), IDatalifeConstant.getPlusUnit(dataStr(mAddWeight)), familyUserInfo.getCreateDate(), familyUserInfo.getMember_Sex(), "0", "1", sessionid, familyUserInfo.getMember_Phone(), familyUserInfo.getMember_Province(), familyUserInfo.getMember_City(), familyUserInfo.getMember_District());
        }

    }

    @Override
    public void onsuccess() {
        mAddUserPresener.getFamilyDataList(ProApplication.SESSIONID,"1","100");
        mAddUserPresener.getBindMemberList("1","60","", ProApplication.SESSIONID);
    }

    @Override
    public void onfail(String failMsg) {
        toast(failMsg + "");
    }

    @Override
    public void onBackFamilyListDataSuccess(ArrayList<FamilyUserInfo> listResultBean) {
        if(listResultBean.size() > 0) {
            DBManager.getInstance(getActivity()).deleteAllFamilyUserInfoBean();
            for (int i = 0; i < listResultBean.size(); i++) {
                DBManager.getInstance(getActivity()).insertMember(listResultBean.get(i));
            }
            Intent intent = new Intent();
            intent.putExtra("familyUserInfo",familyUserInfo);
            getActivity().setResult(Activity.RESULT_OK,intent);
            getActivity().finish();
        }
    }

    @Override
    public void onBackFamilyListDataFail(String str) {
        Log.e("MainActivity:" , str);
    }

    @Override
    public void onSuccess(List<MachineBindMemberBean> machineBindMemberBeans) {
        DBManager.getInstance(getActivity()).deleteAllMachineBindBean();

        Collections.reverse(machineBindMemberBeans);

        for (MachineBindMemberBean machineBindMemberBean : machineBindMemberBeans) {
            DBManager.getInstance(getActivity()).insertMachineBindMember(machineBindMemberBean);
        }
    }

    @Override
    public void onFail(String failMsg) {

    }

    @Override
    public void getDataSuccess(ArrayList<ProvinceBean> provinceBeans, int id) {
        addressView.getDataSuccess(provinceBeans, id);
    }

    @Override
    public void getDataFail(String msg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGEBUNDLE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getActivity().getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
//            showImage(imagePath);
            c.close();

            startPhotoZoom(selectedImage);
        }else if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK){
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
            mRoundImageView.setImageBitmap(bm);

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
}

