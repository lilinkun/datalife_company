package com.datalife.datalife_company.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.LoginBean;
import com.datalife.datalife_company.contract.LoginContract;
import com.datalife.datalife_company.dao.WxUserInfo;
import com.datalife.datalife_company.presenter.LoginPresenter;
import com.datalife.datalife_company.util.Code;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.widget.RoundImageView;
import com.tencent.mm.opensdk.openapi.IWXAPI;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/16.
 */

public class LoginActivity extends BaseActivity implements LoginContract{
    @BindView(R.id.et_account)//账号
            EditText inputEmail;
    @BindView(R.id.et_psd)//密码
            EditText inputPassword;
    @BindView(R.id.ic_vcode)//验证码图片
            ImageView iv_vcode;
    @BindView(R.id.et_vcode)
    EditText et_vcode;
    @BindView(R.id.ll_vcode)
    LinearLayout ll_vcode;//验证码layout
    @BindView(R.id.ic_psd)
    ImageView iv_psd;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.login_layout)
    LinearLayout loginlayout;
    @BindView(R.id.ic_login_back)
    ImageView mLoginBack;
    @BindView(R.id.line_login_account)
    View lineAccount;
    @BindView(R.id.line_login_psd)
    View linePsd;
    @BindView(R.id.line_login_vcode)
    View lineVcode;
    @BindView(R.id.tv_login_account)
    TextView mTvLoginAccout;
    @BindView(R.id.tv_login_psd)
    TextView mTvLoginPsd;
    @BindView(R.id.iv_wx_login)
    ImageView wxLoginImage;
    @BindView(R.id.tv_hello)
    TextView wxTvHello;
    @BindView(R.id.tv_content)
    TextView wxTvContent;
    @BindView(R.id.tv_register_wx)
    TextView tv_wx_register;
    @BindView(R.id.tv_register)
    TextView tv_register;
    @BindView(R.id.ll_third_login)
    LinearLayout llThirdLogin;
    @BindView(R.id.riv_head)
    RoundImageView mRivHead;

    LoginPresenter loginPresenter = new LoginPresenter();

    private boolean isClose = true;

    private String realCode;

    private String SESSIONID = "";
    private ProgressDialog progressDialog;
    IWXAPI iwxapi = null;
    private WxUserInfo wxUserInfo = null;
    private int loginNum = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initEventAndData() {
        loginPresenter.onCreate(this,this);

        if (getIntent().getBooleanExtra("restart",false)){
            SharedPreferences sharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, Context.MODE_PRIVATE);
            if (sharedPreferences != null && !sharedPreferences.getString("account","").equals("")) {
                loginlayout.setVisibility(View.GONE);
                loginPresenter.login(sharedPreferences.getString("account", ""), sharedPreferences.getString("password", ""), IDatalifeConstant.getUniqueId(this));
            }
        }
        SESSIONID = IDatalifeConstant.getUniqueId(this);
        if (loginNum != 0){
            ll_vcode.setVisibility(View.VISIBLE);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.login_loading));
        progressDialog.setCancelable(true);

        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                linePsd.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                lineAccount.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                lineVcode.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                mTvLoginPsd.setVisibility(View.VISIBLE);
                inputPassword.setHint("");
                inputEmail.setHint(R.string.hint_login_name);
                if (inputEmail.getText().toString().trim().length() == 0){
                    mTvLoginAccout.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(inputPassword.getText()) && !TextUtils.isEmpty(inputEmail.getText())) {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_click);
                }else {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_unclick);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputPassword.setHint(R.string.hint_psd_name);
            }
        });

        inputEmail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lineAccount.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                linePsd.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                lineVcode.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                mTvLoginAccout.setVisibility(View.VISIBLE);
                inputEmail.setHint("");
                inputPassword.setHint(R.string.hint_psd_name);
                if (inputPassword.getText().toString().trim().length() == 0){
                    mTvLoginPsd.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lineAccount.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                linePsd.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                lineVcode.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                mTvLoginAccout.setVisibility(View.VISIBLE);
//                inputEmail.setHint(null);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(inputPassword.getText()) && !TextUtils.isEmpty(inputEmail.getText())) {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_click);
                }else {
                    loginButton.setBackgroundResource(R.mipmap.ic_login_unclick);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputEmail.setHint(R.string.hint_login_name);
            }
        });

        et_vcode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lineVcode.setBackgroundColor(getResources().getColor(R.color.bg_toolbar_title));
                lineAccount.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                linePsd.setBackgroundColor(getResources().getColor(R.color.list_divider_color));
                if (inputEmail.getText().toString().trim().length() == 0){
                    mTvLoginAccout.setVisibility(View.INVISIBLE);
                }
                if (inputPassword.getText().toString().trim().length() == 0){
                    mTvLoginPsd.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
    }

    @OnClick({R.id.btn_login,R.id.tv_register,R.id.ic_vcode,R.id.ic_psd,R.id.ic_login_back,R.id.iv_wx_login,R.id.tv_register_wx,R.id.et_vcode})
    protected void otherViewClick(View view) {
        switch (view.getId()){

            case R.id.tv_register:
                /*if (wxUserInfo != null) {
                    loginPresenter.registerText(this, SESSIONID, wxUserInfo.getOpenid(),wxUserInfo.getUnionid());
                }else {
                    loginPresenter.registerText(this, SESSIONID, "","");
                }*/
                break;

            case R.id.tv_register_wx:
                if (wxUserInfo != null) {
                    loginPresenter.registerText(this, SESSIONID, wxUserInfo.getOpenid(),wxUserInfo.getUnionid());
                }else {
                    loginPresenter.registerText(this, SESSIONID, "","");
                }
                break;
            case R.id.btn_login:

                if (loginButton.getText().toString().equals(getResources().getString(R.string.bind))){
                    if (wxUserInfo != null){
                        loginPresenter.bindUser(getAccount(), getPwd(),wxUserInfo.getOpenid(),wxUserInfo.getUnionid(), SESSIONID);
                    }
                }else {
                    if (loginNum >= 1) {
                        String phoneCode = et_vcode.getText().toString().toLowerCase();
                        if (phoneCode.trim().length() == 0) {
                            toast(R.string.input_vcode);
                        } else if (phoneCode.equals(realCode)) {
                            progressDialog.show();
                            loginPresenter.login(getAccount(), getPwd(), SESSIONID);
                        } else {
                            iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
                            realCode = Code.getInstance().getCode().toLowerCase();
                            toast(R.string.toast_vcode_error);
                        }
                    } else {
                        progressDialog.show();
                        loginPresenter.login(getAccount(), getPwd(), SESSIONID);
                    }
                }

                break;

            case R.id.ic_vcode:
                iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;

            case R.id.ic_psd:
                if (isClose){
                    iv_psd.setImageResource(R.mipmap.ic_open_psd);
                    inputPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    inputPassword.setSelection(inputPassword.getText().toString().length());
                    isClose = false;
                }else{
                    iv_psd.setImageResource(R.mipmap.ic_close_psd);
                    inputPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    inputPassword.setSelection(inputPassword.getText().toString().length());
                    isClose = true;
                }
                break;

            case R.id.ic_login_back:
                Intent intent = new Intent();
                intent.setClass(this,MainActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.iv_wx_login:


                break;

            case R.id.et_vcode:

                break;
        }

    }

    public String getAccount() {
        return inputEmail.getText().toString().trim();
    }

    public String getPwd() {
        return inputPassword.getText().toString().trim();
    }

    @Override
    public void loginSuccess(LoginBean mLoginBean) {
        LoginBean loginBean = mLoginBean;
        String datalife = null;
        try {
            datalife = IDatalifeConstant.serialize(loginBean);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(IDatalifeConstant.LOGIN, MODE_PRIVATE);
        sharedPreferences.edit().putString("sessionid",SESSIONID).putBoolean(IDatalifeConstant.LOGIN,true)
                .putString("logininfo",datalife).putString("account",getAccount()).putString("password",getPwd()).commit();

        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
        finish();
        progressDialog.dismiss();
    }

    @Override
    public void loginFail(String failMsg) {
        loginNum++;
        ll_vcode.setVisibility(View.VISIBLE);
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        iv_vcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    @Override
    public void loginMsg(int msg) {
        toast(msg);
    }
}
