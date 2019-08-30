package com.datalife.datalife_company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.base.BaseHealthActivity;
import com.datalife.datalife_company.bean.MeasureEcgBean;
import com.datalife.datalife_company.widget.EcgWaveView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by LG on 2019/7/23.
 */

public class ECGLargeChartActivity extends BaseHealthActivity {

    @BindView(R.id.ecg_large_chart)
    EcgWaveView ecgLargeChart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ecg_large_chart;
    }

    @Override
    protected void initEventAndData() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("心电图");
            actionBar.setSubtitle("ECG chart");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        int pagerSpeed = intent.getIntExtra("pagerSpeed", 1);
        float calibration = intent.getFloatExtra("calibration", 1.0f);
        MeasureEcgBean model = (MeasureEcgBean) intent.getSerializableExtra("model");

        ecgLargeChart.setPagerSpeed(pagerSpeed);
        ecgLargeChart.setCalibration(calibration);

        showChart(model.getWave());
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

    private void showChart(String wave) {
        Observable.just(wave)
                .subscribeOn(Schedulers.newThread()) // 指定 subscribe() 发生在子线程
                .map(new Func1<String, String[]>() {
                    @Override
                    public String[] call(String s) {
                        return s.split(",");
                    }
                })
                .map(new Func1<String[], List<Integer>>() {
                    @Override
                    public List<Integer> call(String[] strings) {
                        final List<Integer> list = new ArrayList<>();
                        for (String str : strings) {
                            list.add(Integer.valueOf(str));
                        }
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在UI程
                .subscribe(new Subscriber<List<Integer>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Integer> list) {
                        ecgLargeChart.preparePoints(list);
                    }
                });

    }
}
