package com.datalife.datalife_company.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.datalife.datalife_company.R;
import com.datalife.datalife_company.activity.RecordActivity;
import com.datalife.datalife_company.app.ProApplication;
import com.datalife.datalife_company.base.BaseRecordFragment;
import com.datalife.datalife_company.bean.MeasureRecordBean;
import com.datalife.datalife_company.contract.RecordContract;
import com.datalife.datalife_company.custom.MyMarkerView;
import com.datalife.datalife_company.presenter.RecordPresenter;
import com.datalife.datalife_company.util.IDatalifeConstant;
import com.datalife.datalife_company.util.MeasureNorm;
import com.datalife.datalife_company.util.MyCalendar;
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

import static com.datalife.datalife_company.activity.RecordActivity.memberID;

/**
 * Created by LG on 2019/7/24.
 */

public class RecordBtFragment extends BaseRecordFragment implements OnChartValueSelectedListener,RecordContract {

    @BindView(R.id.chart1)
    LineChart mChart;
    @BindView(R.id.tv_local_temp)
    TextView mLocalTemp;
    @BindView(R.id.tv_avg_value)
    TextView mAvgValue;
    @BindView(R.id.stateview)
    SmallStateView smallStateView;
    @BindView(R.id.tv_history_last)
    TextView mBtLastValue;
    @BindView(R.id.tv_history_last_date)
    TextView mHrLastDate;
    @BindView(R.id.mTvMonthDay)
    TextView mTvMonthDay;
    @BindView(R.id.tv_createtime)
    TextView tv_createtime;

    private XAxis xAxis;
    public ArrayList<MeasureRecordBean> measureRecordBeans = null;
    private int wm_width, wm_height;
    private String mDateNowStr;
    private String mDateNowWeekStr;
    private MyCalendar calendar;
    private RecordPresenter recordPresenter = new RecordPresenter();

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getlayoutId() {
        return R.layout.fragment_btrecord;
    }

    @Override
    protected void initEventAndData() {

        recordPresenter.onCreate(getActivity(),this);
        getRecordData();
        Date date = new Date();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
        calendar1.add(Calendar.DAY_OF_MONTH, -30);
        Date date1 = calendar1.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mDateNowStr = simpleDateFormat.format(date);
        mDateNowWeekStr = simpleDateFormat.format(date1);

        WindowManager wm = (WindowManager) getActivity().getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        wm_width = wm.getDefaultDisplay().getWidth();
        wm_height = wm.getDefaultDisplay().getHeight();

        calendar = calendar.getInstance();

        mChart.setOnChartValueSelectedListener(this);// no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDrawBorders(false);
        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);
        mChart.setPinchZoom(true);

        // add data
//        setData(12, 50);
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view,handler);
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv);

        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(mTfLight);
        l.setTextColor(getResources().getColor(R.color.bg_line_chart));
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setEnabled(true);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisMaximum(39);
        leftAxis.setAxisMinimum(35);
        leftAxis.setLabelCount(4);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setEnabled(false);
        rightAxis.setAxisMaximum(39);
        rightAxis.setAxisMinimum(35);
        rightAxis.setLabelCount(4);
    }

    @Override
    public void onSuccess(ArrayList<MeasureRecordBean> measureRecordBean){
        this.measureRecordBeans = measureRecordBean;

        tv_createtime.setText(measureRecordBeans.get(measureRecordBeans.size()-1).getCreateDate());

        setData(measureRecordBeans.size());
        mChart.invalidate();
    }

    @Override
    public void onFail(String str) {
        mChart.setData(null);
        mChart.invalidate();
    }


    private void setData(int count) {

        if (count >= 2){
            DecimalFormat df   = new DecimalFormat("######0.0");
            double v1 = Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue1());
            double v2 = Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-2).getCheckValue1());
            double s = v1 -v2;
            String str = df.format(s) + "";
            mBtLastValue.setText(str);
            mHrLastDate.setText(measureRecordBeans.get(measureRecordBeans.size()-2).getCreateDate().substring(0,10));
        }

        if (count > 0){
            mLocalTemp.setText(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue1());
            smallStateView.setValue(IDatalifeConstant.getWM(getActivity()),Double.parseDouble(measureRecordBeans.get(measureRecordBeans.size()-1).getCheckValue1()), MeasureNorm.getBT(),"");
            mTvMonthDay.setText(mDateNowWeekStr);
        }


        //设置一页最大显示个数为6，超出部分就滑动
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        float ratio = 1f;
        if (count >= 6){
            ratio = (float) count/(float) 6;
            mChart.moveViewToX(count-1);
        }
        /**
         * 先将缩放比设置成0后，再去设置你想要的缩放比。
         * 若不这样做的话，在当前页面重新加载数据时，你所设置的缩放比会失效，并且出现你意向不到的显示问题。
         * 如果你的图表只在页面加载一次的话不需要这么做。
         */
        mChart.zoom(0,1f,0,0);
        mChart.zoom(ratio,1f,0,0);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        for (int i = 0; i < measureRecordBeans.size(); i++) {
            yVals1.add(new Entry(i +0.3f, Float.parseFloat(measureRecordBeans.get(i).getCheckValue1()),getResources().getDrawable(R.mipmap.ic_line_spot)));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "体温");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.bg_line_chart));
            set1.setDrawCircleHole(true);
            set1.setLineWidth(2f);
            set1.setCircleRadius(7f);
//            set1.setCircleSize(7f);
            set1.setFillAlpha(65);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setDrawCircleHole(false);
            set1.setDrawValues(false);
            set1.setFillDrawable(getResources().getDrawable(R.drawable.ic_blue_fill));
            set1.setDrawFilled(true);
            set1.setDrawIcons(true);


            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            // set data
            mChart.setData(data);

        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
    }


    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    public void getRecordData(){
        if (recordPresenter != null){
            recordPresenter.onGetListValue(String.valueOf(1),RecordActivity.ShowCount,RecordActivity.memberID,IDatalifeConstant.MACHINE_HEALTH,String.valueOf(IDatalifeConstant.BTINT),"","", ProApplication.SESSIONID);
        }
    }

}
