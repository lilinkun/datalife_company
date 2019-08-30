package com.datalife.datalife_company.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.RecordActivity;
import com.datalife.datalife_company.adapter.EcgRecordAdapter;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseRecordFragment;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.contract.RecordContract;
import com.datalife.datalife_company.custom.MyMarkerView;
import com.datalife.datalife_company.presenter.RecordPresenter;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.MeasureNorm;
import com.datalife.datalife_company.widget.LX_LoadListView;
import com.datalife.datalife_company.widget.MyDateLinear;
import com.datalife.datalife_company.widget.SmallStateView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by LG on 2019/7/24.
 */

public class RecordEcgFragment extends BaseRecordFragment implements RecordContract,LX_LoadListView.OnLoadMoreListener {

    @BindView(R.id.lv_ecg_record)
    LX_LoadListView mEcgListView;

    EcgRecordAdapter ecgRecordAdapter;
    int pageIndex = 1;
    private RecordPresenter recordPresenter = new RecordPresenter();

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_ecgrecord;
    }

    @Override
    protected void initEventAndData() {

        mEcgListView.setLoadMoreListener(this);

        recordPresenter.onCreate(getContext(),this);
        getRecordData();

    }

    @Override
    public void loadMore() {
        pageIndex++;
        onBtRecordLisener.onEcgIntent(1,pageIndex);
    }

    @Override
    public void onSuccess(ArrayList<MeasureRecordBean> newsInfos) {
        mEcgListView.setVisibility(View.VISIBLE);
        ArrayList<MeasureRecordBean> measureRecordBeans = newsInfos;
        if (ecgRecordAdapter == null) {
            ecgRecordAdapter = new EcgRecordAdapter(getActivity(), measureRecordBeans);
            mEcgListView.setAdapter(ecgRecordAdapter);
        }else {
            ecgRecordAdapter.setValue(measureRecordBeans);
            ecgRecordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFail(String str) {
        if (str.contains("查无数据")) {
            mEcgListView.setVisibility(View.GONE);
        }
    }


    public void getRecordData(){
        if (recordPresenter != null){
            recordPresenter.onGetListValue(String.valueOf(1), RecordActivity.ShowCount,RecordActivity.memberID,IDatalifeConstant.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.ECGINT),"","", ProApplication.SESSIONID);
        }
    }
}
