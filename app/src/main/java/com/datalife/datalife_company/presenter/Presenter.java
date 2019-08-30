package com.datalife.datalife_company.presenter;

import android.content.Context;

import com.datalife.datalife_company.mvp.IView;

/**
 * Created by LG on 2019/7/16.
 */

public interface Presenter {
    void onCreate(Context context,IView view);

    void onStart();

    void onStop();

    void pause();

}
