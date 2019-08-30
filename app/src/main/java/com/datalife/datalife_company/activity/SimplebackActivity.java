package com.datalife.datalife_company.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.bean.SimpleBackPage;
import com.datalife.datalife_company.interf.FragmentCallBack;
import com.datalife.datalife_company.interf.OnTitleBarClickListener;
import com.datalife.datalife_company.interf.onTitleBarSetListener;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.widget.CustomTitleBar;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LG on 2019/7/16.
 */

public class SimplebackActivity extends BaseActivity implements FragmentCallBack,onTitleBarSetListener {

    protected int mPageValue = -1;
    public final static String BUNDLE_KEY_PAGE = "BUNDLE_KEY_PAGE";
    public final static String BUNDLE_KEY_FORGET = "BUNDLE_KEY_FORGET";
    public final static String BUNDLE_KEY_ARGS = "BUNDLE_KEY_ARGS";
    public final static String BUNDLE_KEY_STYLE = "BUNDLE_KEY_STYLE";
    public final static int RESULT_EQUIPMANAGER = 0X111;
    public final static int RESULT_SETTING = 0X222;
    public final static int RESULT_USERINFO = 0X333;
    public final static int RESULT_ADDUSER = 0x444;
    public final static int RESULT_MEMBER = 0x555;
    public final static int RESULT_BINDMEMBER = 0x666;
    public final static int RESULT_REPORT = 0X777;
    private static final String TAG = "FLAG_TAG";
    protected WeakReference<Fragment> mFragment;

    @BindView(R.id.titlebar)
    CustomTitleBar mTitleBar;

    private SimpleBackPage page;
    private OnTitleBarClickListener mListener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    public void initView() {

        Intent intent = getIntent();
        if (mPageValue == -1) {
            mPageValue = intent.getIntExtra(BUNDLE_KEY_PAGE, 0);
        }
        initFromIntent(mPageValue, getIntent());
    }

    protected void initFromIntent(int pageValue, Intent data) {
        if (data == null) {
            throw new RuntimeException(
                    "you must provide a page info to display");
        }
        page = SimpleBackPage.getPageByValue(pageValue);
        if (page == null) {
            throw new IllegalArgumentException("can not find page by value:"
                    + pageValue);
        }

        mTitleBar.setTitleName(page.getTitle());

        try {
            Fragment fragment = (Fragment) page.getClz().newInstance();
            if(fragment instanceof OnTitleBarClickListener){
                mListener = (OnTitleBarClickListener)fragment;
            }else {
                mListener = null;
            }

            Bundle args = data.getBundleExtra(BUNDLE_KEY_ARGS);
            /*if (data.getIntExtra(BUNDLE_KEY_STYLE,0) != 0){
                int code = data.getIntExtra(BUNDLE_KEY_STYLE,0);
                if (args == null){
                    args = new Bundle();
                }
                if (code== ModifyPasswordFragment.LOGINPASSWORD){
                    mTitleBar.setTitleName(R.string.modify_login_password);
                }

                args.putInt(BUNDLE_KEY_STYLE,code);
            }*/
            if (args != null) {
                fragment.setArguments(args);
            }

            FragmentTransaction trans = getSupportFragmentManager()
                    .beginTransaction();
            trans.replace(R.id.fra_content, fragment, TAG);
            trans.commitAllowingStateLoss();

            mFragment = new WeakReference<>(fragment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(
                    "generate fragment error. by value:" + pageValue);
        }
    }

    @Override
    public void callbackfun(Bundle bundle) {
        if (bundle != null){
            if (bundle.getInt(IDatalifeConstant.TYPE) == IDatalifeConstant.TYPE_BACK){
                setResult(RESULT_OK);
                finish();
            }
        }else {
            finish();
        }
    }

    public void finishApp(){
        setResult(RESULT_OK);
        finish();
    }

    @OnClick({R.id.iv_head_left,R.id.tv_head_right})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_head_left:

                if(mListener != null){
                    mListener.onBackClick();
                }else {
                    finish();
                }
                break;

            case R.id.tv_head_right:
                if(mListener != null){
                    mListener.onMoreClick();
                }
                break;
        }
    }

    @Override
    public void setRightDrawable(int resId) {
        mTitleBar.setRightDrawable(resId);
    }

    @Override
    public void setRightDrawable(Drawable drawable) {
        mTitleBar.setRightDrawable(drawable);
    }

    @Override
    public void setRightText(int resId) {
        mTitleBar.setRightText(resId);
    }

    @Override
    public void setRightText(String text) {
        mTitleBar.setRightText(text);
    }

    @Override
    public void setRightDrawableVisibility(int visibility) {
        mTitleBar.setRightDrawableVisibility(visibility);
    }

    @Override
    public void setRightTextVisibility(int visibility) {
        mTitleBar.setRightTextVisibility(visibility);
    }

    @Override
    public void setVisible(int visible) {
        mTitleBar.setVisibility(visible);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
