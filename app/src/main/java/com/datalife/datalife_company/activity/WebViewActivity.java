package com.datalife.datalife_company.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BaseActivity;
import com.datalife.datalife_company.widget.CustomTitleBar;
import com.datalife.datalife_company.widget.ProgressWebView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册注意事项页面
 * Created by LG on 2018/1/28.
 */
public class WebViewActivity extends BaseActivity {

    @BindView(R.id.wv_usage_protocol)
    WebView mUsageProtocolWv;
//    @BindView(R.id.tv_protocol)
//    TextView mProtocolTv;
    @BindView(R.id.layout_loaddingfail)
    RelativeLayout mLoaddingFailLayout;
    @BindView(R.id.btn_loadding)
    Button mLoaddingBtn;
    @BindView(R.id.titlebar)
    CustomTitleBar mCustomTitleBar;
    @BindView(R.id.progress_wv)
    ProgressWebView progress_wv;

    private String url = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web;
    }

    @OnClick({R.id.iv_head_left,R.id.btn_loadding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_head_left:

                if (mUsageProtocolWv.canGoBack()) {

                } else {
                    finish();
                }

                break;

            case R.id.btn_loadding:
                if (mLoaddingFailLayout != null && mLoaddingFailLayout.isShown()) {
                    mLoaddingFailLayout.setVisibility(View.GONE);
                }
                mUsageProtocolWv.loadUrl(url);

                break;
        }
    }

    @Override
    protected void initEventAndData() {

        url = getIntent().getStringExtra("url");

        // mUsageProtocolWv.loadUrl(getIntent().getStringExtra("https://www.baidu.com/"));

        if (getIntent().getStringExtra("type").equals("register")){
            mCustomTitleBar.setTitleName(R.string.user_protocol);
        }else if (getIntent().getStringExtra("type").equals("gene")){
            mCustomTitleBar.setTitleName(R.string.gene_report);
        }else if(getIntent().getStringExtra("type").equals("news")){
            mCustomTitleBar.setTitleName(R.string.information_details);
        }else if (getIntent().getStringExtra("type").equals("help")){
            mCustomTitleBar.setTitleName(R.string.me_help);
        }

        if (getIntent() != null && url != null) {
            mUsageProtocolWv.loadUrl(url);
            //如果不设置WebViewClient，请求会跳转系统浏览器
            mUsageProtocolWv.setWebViewClient(new MyWebViewClient());

            mUsageProtocolWv.setWebChromeClient(new WebChromeClient(){
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        //加载完毕进度条消失
                        progress_wv.setVisibility(View.GONE);
                    } else {
                        //更新进度
                        progress_wv.setProgress(newProgress);
                    }
                }
            });
        }
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            mLoaddingFailLayout.setVisibility(View.VISIBLE);
        }
    }
}