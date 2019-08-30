package com.datalife.datalife_company.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.datalife.datalife_company.R;
import com.datalife.datalife_company.app.AppManager;
import com.datalife.datalife_company.mvp.IView;
import com.datalife.datalife_company.util.UToast;
import com.datalife.datalife_company.widget.Eyes;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by LG on 2018/1/12.
 */

public abstract class BaseFragment extends Fragment implements IView {

//        protected MainActivity mActivity;
        public Unbinder unbinder;

        @Override
        public void onAttach(Context context) {
                super.onAttach(context);
                Eyes.setStatusBarColor(getActivity(), ContextCompat.getColor(getActivity(), R.color.bg_toolbar_title));
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View v = inflater.inflate(getlayoutId(), container, false);
                unbinder =  ButterKnife.bind(this,v);
                //初始化事件和获取数据, 在此方法中获取数据不是懒加载模式
                initEventAndData();
                DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                displayMetrics.scaledDensity = displayMetrics.density;
                return v;
        }

        @Override
        public void onDestroy() {
                super.onDestroy();
//                otherevent();
        }

        protected void otherevent(){
                AppManager.getAppManager().AppExit();
        }

        protected abstract int getlayoutId();

        protected abstract void initEventAndData();

        protected void toast(@NonNull String text) {
                UToast.show(getActivity(), text);
        }

        public void uploadData() {

        }

}
