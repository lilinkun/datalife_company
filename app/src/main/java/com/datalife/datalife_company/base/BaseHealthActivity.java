package com.datalife.datalife_company.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.util.UToast;
import com.datalife.datalife_company.widget.Eyes;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LG on 2019/7/16.
 */

public abstract class BaseHealthActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.bg_toolbar_title));
        unbinder = ButterKnife.bind(this);
        //初始化事件跟获取数据以及一些准备工作, 由于使用了ButterKnife, findViewById和基本的Click事件都不会在这里
        initEventAndData();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.scaledDensity = displayMetrics.density;
    }

    protected abstract int getLayoutId();

    protected abstract void initEventAndData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        unbinder.unbind();
    }

    protected void toast(@StringRes int resId) {
        UToast.show(getActivity(), resId);
    }

    protected void toast(@StringRes int resId, int duration) {
        UToast.show(getActivity(), resId, duration);
    }

    protected void toast(@NonNull String text) {
        UToast.show(getActivity(), text);
    }

    protected void toast(@NonNull String text, int duration) {
        UToast.show(getActivity(), text, duration);
    }

    private Activity getActivity() {
        return this;
    }

    protected void setUpActionBar(Toolbar toolbar) {
        setUpActionBar(toolbar, null);
    }

    protected void setUpActionBar(Toolbar toolbar, String title) {
        setUpActionBar(toolbar, title, null, true);
    }

    protected void setUpActionBar(Toolbar toolbar, String title, String subTitle, boolean showHomeIcon) {
        setSupportActionBar(toolbar);
        final Toolbar.OnMenuItemClickListener menuItemClick = setUpMenuItemClick();
        if (menuItemClick != null) toolbar.setOnMenuItemClickListener(menuItemClick);
        setUpActionBar(title, subTitle, showHomeIcon);
    }

    protected void setUpActionBar(String title, String subTitle, boolean showHomeIcon) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showHomeIcon);
            if (!TextUtils.isEmpty(title)) actionBar.setTitle(title);
            if (!TextUtils.isEmpty(subTitle)) actionBar.setSubtitle(subTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected Toolbar.OnMenuItemClickListener setUpMenuItemClick() {
        return null;
    }

    protected void showProgressDialog(int messageId) {
        final String message = getResources().getString(messageId);
        showProgressDialog(message);
    }

    protected void showProgressDialog(CharSequence message) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(message);
        } else {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }


}


